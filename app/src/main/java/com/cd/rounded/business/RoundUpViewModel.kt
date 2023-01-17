package com.cd.rounded.business

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cd.rounded.domain.Account
import com.cd.rounded.domain.FeedItem
import com.cd.rounded.domain.RoundUpRepo
import com.cd.rounded.domain.SavingsGoal
import com.cd.rounded.tools.AmountUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoundUpViewModel @Inject constructor(
    private val repo: RoundUpRepo
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    // The UI collects from this StateFlow to get its state updates
    val state: MutableStateFlow<State> = _state

    private val _accounts = mutableStateListOf<Account>()
    //Value of the account holder' listed accounts
    val accounts = _accounts

    private val _savingsGoal = mutableStateListOf<SavingsGoal>()
    // Value of the account holder' listed saving goals
    val savingsGoal = _savingsGoal

    private val _selectedWeek = mutableStateOf(0)
    // Value of the selected week to load transactions for and perform roundup functions on,
    val selectedWeek = _selectedWeek

    private val _feedItems = mutableStateListOf<FeedItem>()
    // Value of the week' transactions loaded
    val feedItems = _feedItems

    private val _roundUpAmount = mutableStateOf(0.0)
    // Value of the round up amount calculated from the loaded week' transactions
    val roundUpAmount = _roundUpAmount

    private val _savingsGoalAmount = mutableStateOf(0.0)
    // Value of the Savings Goal balance
    val savingsGoalAmount  = _savingsGoalAmount

    init {
        //On load, ask the user for a valid access token
        _state.value = State.AccessToken
    }

    /**
     * Function to set the state to loading (shows a loader to the customer while we make the requests needed for the roundup screen,
     * Called once a valid access token has been supplied or once the user has made a transfer and wishes to return to the round up screen for another
     */
    fun doOnLoad() {
        viewModelScope.launch {
            _state.value = State.Loading
            getAccounts()
        }
    }

    /**
     * Function to get the Accounts, upon a successful response,
     * will look to asynchronously get the transactions for the current week
     * and the savings goal information.
     */
    private fun getAccounts() {
        viewModelScope.launch {
            repo.getAccounts()
                .onSuccess {
                    _accounts.addAll(it)
                    val transactions = async { getTransactionsForWeek(0) }
                    val savingsAccount = async { getSavingsAccountInfo() }
                    listOf(transactions, savingsAccount).awaitAll()
                }
                .onFailure {
                    _state.value = State.Error(
                        it.cause,
                    )
                }
        }
    }

    /**
     * Function that requests the transactions for a given week from the server.
     * Filters any transactions that are payments "IN" and any other "RoundUps".
     * @param week int, 0 = current week, 1 = previous week, 2 = 2 weeks ago, etc.
     */
    fun getTransactionsForWeek(week: Int = 0) {
        viewModelScope.launch {
            _state.value = State.Loading
            _feedItems.clear()
            repo.getTransactionsForTheWeek(
                accountUid = accounts.first().accountUid ?: "",
                categoryUid = accounts.first().defaultCategory ?: "",
                week
            )
                .onSuccess { feedItemsList ->
                    _feedItems.addAll(
                        feedItemsList.filter { feedItem ->
                            feedItem.direction == "OUT" &&
                            feedItem.source!="INTERNAL_TRANSFER"
                        }
                    )
                    calculateRoundUpAmount()
                }
                .onFailure {
                    _state.value = State.Error(
                        it.cause,
                    )
                }
        }
    }

    /**
     * Function that calculates the RoundUp Amount to display to the customer, based on their
     * transactions for the week. Converts minor units received from server into Double
     */
    private fun calculateRoundUpAmount() {
        viewModelScope.launch {
            _state.value = State.Loading
            _roundUpAmount.value = 0.0
            var roundUp = 0.0
            feedItems.forEach { item ->
                val itemAmount = item.amount?.minorUnits
                itemAmount?.let { itemAmountInt ->
                    val amountDecimal = AmountUtils.convertMinorUnitsToDecimal(itemAmountInt)
                    val amountDouble = AmountUtils.convertMinorDecimalToDouble(amountDecimal)
                    val roundUpItemAmount =
                        AmountUtils.convertMinorUnitsToRoundedAmount(amountDouble)
                    val feedItemRoundUp = roundUpItemAmount - amountDouble
                    roundUp += feedItemRoundUp
                }
            }
            _roundUpAmount.value = roundUp
            _state.value = State.Loaded
        }
    }

    /**
     * Function that retrieves the savings goal information for the customer
     * to see their current balance. Converts minor units received from server into Double
     */
    private fun getSavingsAccountInfo() {
        viewModelScope.launch {
            repo.getSavingsAccountInfo(
                accountUid = accounts.first().accountUid ?: ""
            )
                .onSuccess {
                    _savingsGoal.addAll(it)
                    it.first().totalSaved?.minorUnits?.let { savingsMinorUnits ->
                        _savingsGoalAmount.value =
                            AmountUtils.convertMinorDecimalToDouble(
                                AmountUtils.convertMinorUnitsToDecimal(
                                    savingsMinorUnits
                                )
                            )
                    }
                }

                .onFailure {
                    _state.value = State.Error(
                        it.cause,
                    )
                }
        }
    }

    /**
     * Function to process the transfer of the RoundUp Amount to the savings goal.
     */
    fun makeTransferToSavingsGoal() {
        viewModelScope.launch {
            _state.value = State.Loading
            repo.addRoundUpToSavingsGoal(
                accountUid = accounts.first().accountUid ?: "",
                savingsGoalUid = savingsGoal.first().savingsGoalUid ?: "",
                roundUpAmount = roundUpAmount.value
            )
                .onSuccess {
                    if(it) _state.value = State.TransferSuccess
                }
                .onFailure {
                    _state.value = State.Error(
                        it.cause,
                    )
                }
        }
    }

    sealed class State {

        /**
         * ACCESS TOKEN required
         */
        object AccessToken : State()

        /**
         * Loaded, user able to see details and perform actions
         */
        object Loaded : State()

        /**
         * Fetching data from backend, or performing backend functions, show loader
         */
        object Loading : State()

        /**
         * Something is not cool
         */
        data class Error(
            val throwable: Throwable? = null,
        ) : State()

        /**
         * RoundUp transfer to Savings Goal Successfully Made
         */
        object TransferSuccess : State()
    }

}