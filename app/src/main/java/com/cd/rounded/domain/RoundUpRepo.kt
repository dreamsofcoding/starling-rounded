package com.cd.rounded.domain

import com.cd.rounded.backend.StarlingService
import com.cd.rounded.tools.AmountUtils
import com.cd.rounded.tools.DateUtils
import javax.inject.Inject

class RoundUpRepo @Inject constructor(
    private val starlingService: StarlingService
) {
    /**
     * Function to retrieve the customer' accounts from the server and return the response
     * as a List of Accounts to the ViewModel. Reports Errors specific errors if encountered
     */
    suspend fun getAccounts(): Result<List<Account>> = kotlin.runCatching {
        val accountList = mutableListOf<Account>()
        val response = starlingService.getAllAccounts()
        return if (response.isSuccessful) {
            if (!response.body()?.accounts.isNullOrEmpty()) {
                response.body()?.accounts?.let { accountList.addAll(it) }
                Result.success(
                    accountList
                )
            } else {
                Result.failure(
                    IllegalStateException(
                        "No valid accounts found"
                    )
                )
            }
        } else Result.failure(IllegalStateException("Accounts call failed"))

    }

    /**
     * Function that retrieves the transactions for the week specified and returns the response
     * as a List of feed items to the viewmodel.
     * @param accountUid UID of the account being queried
     * @param categoryUid UID for the category of the transactions
     * @param week int, 0 = current week, 1 = previous week, 2 = 2 weeks ago, etc.
     */
    suspend fun getTransactionsForTheWeek(
        accountUid: String,
        categoryUid: String,
        week: Int
    ): Result<MutableList<FeedItem>> = kotlin.runCatching {
        val max = DateUtils.getMinMaxTimesFromWeek(week).max
        val min = DateUtils.getMinMaxTimesFromWeek(week).min

        val feedList = mutableListOf<FeedItem>()
        val response = starlingService.getTransactionFeed(
            account = accountUid,
            category = categoryUid,
            minTimeStamp = min,
            maxTimeStamp = max
        )
        return if (response.isSuccessful) {
            if (!response.body()?.feedItems.isNullOrEmpty()) {
                response.body()?.feedItems?.let { feedList.addAll(it) }
                Result.success(
                    feedList
                )
            } else Result.failure(
                IllegalStateException(
                    "No valid transactions found in the timeframe queried"
                )
            )
        } else Result.failure(IllegalStateException("Transaction Feed call failed"))

    }

    /**
     * Function that retrieves the savings goals of the account
     * as a List of savings Goal to the viewmodel.
     * @param accountUid UID of the account being queried
     */
    suspend fun getSavingsAccountInfo(
        accountUid: String
    ): Result<List<SavingsGoal>> = kotlin.runCatching {
        val savingsGoalList = mutableListOf<SavingsGoal>()
        val response = starlingService.getSavingGoals(
            account = accountUid
        )
        return if (response.isSuccessful) {
            if(!response.body()?.savingsGoalList.isNullOrEmpty()){
                response.body()?.savingsGoalList?.let { savingsGoalList.addAll(it) }
                    Result.success(
                        savingsGoalList
                    )
            }
            else Result.failure(
                IllegalStateException(
                    "No valid Saving Goals found"
                )
            )
        } else Result.failure(IllegalStateException("Savings Goal call failed"))

    }

    /**
     * Function that makes the transfer of the RoundUp Amount to the Savings Goal account.
     * Returns a Boolean to the viewmodel for confirmation of success
     * @param accountUid UID of the account being queried
     * @param savingsGoalUid UID of the account being queried
     * @param roundUpAmount The amount of RoundUp that is to be transferred
     */
    suspend fun addRoundUpToSavingsGoal(
        accountUid: String,
        savingsGoalUid: String,
        roundUpAmount: Double
    ): Result<Boolean> {
        val minorUnitRoundUp =
            SavingsGoalTransferAmount(
                amount = Amount(
                    currency = "GBP",
                    minorUnits = AmountUtils.convertDoubleToMinorDecimal(roundUpAmount)?.toInt()
                )
            )
        val response = starlingService.putSavingAmount(
            account = accountUid,
            savingsGoal = savingsGoalUid,
            addAmount = minorUnitRoundUp
        )

        return if (response.isSuccessful) {
            Result.success(true)
        } else Result.failure(IllegalStateException("Transaction Feed call failed"))
    }
}