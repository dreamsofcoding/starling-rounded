package com.cd.rounded.business

import androidx.lifecycle.viewmodel.compose.viewModel
import com.cd.rounded.domain.Account
import com.cd.rounded.domain.Amount
import com.cd.rounded.domain.AssociatedFeedRoundUp
import com.cd.rounded.domain.FeedItem
import com.cd.rounded.domain.RoundUpRepo
import com.cd.rounded.domain.SavingsGoal
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.doubles.shouldNotBeZero
import io.kotest.matchers.ints.shouldBeZero
import io.kotest.matchers.ints.shouldNotBeZero
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


class RoundUpViewModelTest : StringSpec() {

    private val roundUpRepo: RoundUpRepo = mock()
    private lateinit var viewModel: RoundUpViewModel

    override fun beforeAny(testCase: TestCase) {
        super.beforeAny(testCase)
        viewModel = RoundUpViewModel(
            roundUpRepo
        )
    }

    init {
        getAccountTests()
    }

    private fun getAccountTests() {
        "show loading when customer account fetch is started" {
            whenever(roundUpRepo.getAccounts())
            viewModel.state.value.shouldBe {
                RoundUpViewModel.State.Loading
            }
        }

        "show no valid accounts found error when trying to fetch customer account" {
            launch {
                whenever(roundUpRepo.getAccounts())
                    .thenReturn(
                        Result.success(
                            listOf()
                        )
                    )
                viewModel.accounts.size.shouldBeZero()
            }
        }

        "show account call failed error when trying to fetch customer account" {
            whenever(roundUpRepo.getAccounts())
                .thenReturn(
                    Result.failure(
                        IllegalStateException("Accounts call failed")
                    )
                )
        }

        "successful get of customer' transactions" {
            checkAll(
                Arb.account(),
                Arb.int(0..10),
                Arb.feedItem()
            ) { customerAccount: Account,
                weekNumber: Int,
                feedItems: FeedItem ->

                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    ).thenReturn(
                        Result.success(
                            listOf(customerAccount)
                        )
                    )

                    whenever(
                        roundUpRepo.getTransactionsForTheWeek(
                            accountUid = viewModel.accounts.first().accountUid ?: "",
                            categoryUid = viewModel.accounts.first().defaultCategory ?: "",
                            weekNumber
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf(feedItems)
                        )
                    )

                    viewModel.feedItems.size.shouldNotBeZero()
                }
            }
        }

        "failure to get customer' transactions" {
            checkAll(
                Arb.account(),
                Arb.int(0..10),
                Arb.feedItem()
            ) { customerAccount: Account,
                weekNumber: Int,
                feedItems: FeedItem ->

                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    ).thenReturn(
                        Result.success(
                            listOf(customerAccount)
                        )
                    )

                    whenever(
                        roundUpRepo.getTransactionsForTheWeek(
                            accountUid = viewModel.accounts.first().accountUid ?: "",
                            categoryUid = viewModel.accounts.first().defaultCategory ?: "",
                            weekNumber
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf()
                        )
                    )

                    viewModel.feedItems.size.shouldBeZero()
                }
            }
        }

        "failure on the get customer' transactions API call" {
            checkAll(
                Arb.account(),
                Arb.int(0..10),
                Arb.feedItem()
            ) { customerAccount: Account,
                weekNumber: Int,
                feedItems: FeedItem ->

                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    ).thenReturn(
                        Result.success(
                            listOf(customerAccount)
                        )
                    )

                    whenever(
                        roundUpRepo.getTransactionsForTheWeek(
                            accountUid = viewModel.accounts.first().accountUid ?: "",
                            categoryUid = viewModel.accounts.first().defaultCategory ?: "",
                            weekNumber
                        )
                    ).thenReturn(
                        Result.failure(
                            IllegalStateException("Transaction Feed call failed")
                        )
                    )

                    viewModel.feedItems.size.shouldBeZero()
                }
            }
        }

        "successful get of customer' savings account" {
            checkAll(
                Arb.account(),
                Arb.savingsGoal()
            ) { customerAccount: Account,
                savingsAccount : SavingsGoal ->
                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    ).thenReturn(
                        Result.success(
                            listOf(customerAccount)
                        )
                    )

                    whenever(
                        roundUpRepo.getSavingsAccountInfo(
                            viewModel.accounts.first().accountUid ?: "",
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf(savingsAccount)
                        )
                    )

                    viewModel.savingsGoal.size.shouldNotBeZero()
                }
            }
        }

        "failure to get customer' savings account" {
            checkAll(
                Arb.account(),
            ) { customerAccount: Account ->
                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    ).thenReturn(
                        Result.success(
                            listOf(customerAccount)
                        )
                    )

                    whenever(
                        roundUpRepo.getSavingsAccountInfo(
                            viewModel.accounts.first().accountUid ?: "",
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf()
                        )
                    )

                    viewModel.savingsGoal.size.shouldBeZero()
                }
            }
        }

        "failure of the get customer' savings account API call" {
            checkAll(
                Arb.account(),
            ) { customerAccount: Account ->
                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    ).thenReturn(
                        Result.success(
                            listOf(customerAccount)
                        )
                    )

                    whenever(
                        roundUpRepo.getSavingsAccountInfo(
                            viewModel.accounts.first().accountUid ?: "",
                        )
                    ).thenReturn(
                        Result.failure(IllegalStateException("Savings Goal call failed"))
                    )

                    viewModel.savingsGoal.size.shouldNotBeZero()
                }
            }
        }

        "show loaded state when customer data has successfully loaded" {
            checkAll(
                Arb.account(),
                Arb.int(0..10),
                Arb.feedItem(),
                Arb.savingsGoal()
            ) {
                    customerAccount: Account,
                weekNumber : Int,
                feedItems : FeedItem,
                savingsAccount : SavingsGoal->

                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    )
                        .thenReturn(
                            Result.success(
                                listOf(customerAccount)
                            )
                        )
                    viewModel.accounts.size.shouldNotBeZero()

                    whenever(
                        roundUpRepo.getTransactionsForTheWeek(
                            accountUid = viewModel.accounts.first().accountUid ?: "",
                            categoryUid = viewModel.accounts.first().defaultCategory ?: "",
                            weekNumber
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf(feedItems)
                        )
                    )
                    viewModel.feedItems.size.shouldNotBeZero()

                    whenever(
                        roundUpRepo.getSavingsAccountInfo(
                            viewModel.accounts.first().accountUid ?: "",
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf(savingsAccount)
                        )
                    )

                    viewModel.savingsGoal.size.shouldNotBeZero()

                    viewModel.roundUpAmount.value.shouldNotBeZero()

                    viewModel.state.value.shouldBe {
                        RoundUpViewModel.State.Loading
                    }
                }
            }
        }

        "make transfer successfully" {
            checkAll(
                Arb.account(),
                Arb.int(0..10),
                Arb.feedItem(),
                Arb.savingsGoal()
            ) {
                    customerAccount: Account,
                    weekNumber : Int,
                    feedItems : FeedItem,
                    savingsAccount : SavingsGoal->

                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    )
                        .thenReturn(
                            Result.success(
                                listOf(customerAccount)
                            )
                        )
                    viewModel.accounts.size.shouldNotBeZero()

                    whenever(
                        roundUpRepo.getTransactionsForTheWeek(
                            accountUid = viewModel.accounts.first().accountUid ?: "",
                            categoryUid = viewModel.accounts.first().defaultCategory ?: "",
                            weekNumber
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf(feedItems)
                        )
                    )
                    viewModel.feedItems.size.shouldNotBeZero()

                    whenever(
                        roundUpRepo.getSavingsAccountInfo(
                            viewModel.accounts.first().accountUid ?: "",
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf(savingsAccount)
                        )
                    )

                    viewModel.savingsGoal.size.shouldNotBeZero()

                    viewModel.roundUpAmount.value.shouldNotBeZero()

                    whenever(roundUpRepo.addRoundUpToSavingsGoal(
                        accountUid = viewModel.accounts.first().accountUid ?: "",
                        savingsGoalUid = viewModel.savingsGoal.first().savingsGoalUid ?: "",
                        viewModel.roundUpAmount.value
                    ))
                        .thenReturn(
                            Result.success(true)
                        )
                }
            }
        }

        "make transfer failure" {
            checkAll(
                Arb.account(),
                Arb.int(0..10),
                Arb.feedItem(),
                Arb.savingsGoal()
            ) {
                    customerAccount: Account,
                    weekNumber : Int,
                    feedItems : FeedItem,
                    savingsAccount : SavingsGoal->

                launch {
                    whenever(
                        roundUpRepo.getAccounts()
                    )
                        .thenReturn(
                            Result.success(
                                listOf(customerAccount)
                            )
                        )
                    viewModel.accounts.size.shouldNotBeZero()

                    whenever(
                        roundUpRepo.getTransactionsForTheWeek(
                            accountUid = viewModel.accounts.first().accountUid ?: "",
                            categoryUid = viewModel.accounts.first().defaultCategory ?: "",
                            weekNumber
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf(feedItems)
                        )
                    )
                    viewModel.feedItems.size.shouldNotBeZero()

                    whenever(
                        roundUpRepo.getSavingsAccountInfo(
                            viewModel.accounts.first().accountUid ?: "",
                        )
                    ).thenReturn(
                        Result.success(
                            mutableListOf(savingsAccount)
                        )
                    )

                    viewModel.savingsGoal.size.shouldNotBeZero()

                    viewModel.roundUpAmount.value.shouldNotBeZero()

                    whenever(roundUpRepo.addRoundUpToSavingsGoal(
                        accountUid = viewModel.accounts.first().accountUid ?: "",
                        savingsGoalUid = viewModel.savingsGoal.first().savingsGoalUid ?: "",
                        viewModel.roundUpAmount.value
                    ))
                        .thenReturn(
                            Result.failure(IllegalStateException("Transaction Feed call failed"))
                        )
                }
            }
        }
    }


    /** Generators */
    fun Arb.Companion.account() = arbitrary {
        Account(
            accountUid = Arb.string().toString(),
            accountType = Exhaustive.enum<AccountType>().toArb().sample(it).value.toString(),
            defaultCategory = Arb.string().toString(),
            currency = Exhaustive.enum<Currency>().toArb().sample(it).value.toString(),
            createdAt = Arb.Companion.dateTime().toString(),
            name = Arb.string().toString(),

            )
    }

    fun Arb.Companion.dateTime() = arbitrary {
        OffsetDateTime.now()
    }

    fun Arb.Companion.feedItem() = arbitrary {
        FeedItem(
            feedItemUid = Arb.string().toString(),
            categoryUid = Arb.string().toString(),
            amount = Arb.amount().sample(it).value,
            sourceAmount = Arb.amount().sample(it).value,
            direction = Exhaustive.enum<Direction>().toArb().sample(it).value.toString(),
            updatedAt = Arb.dateTime().sample(it).value.toString(),
            transactionTime = Arb.dateTime().sample(it).value.toString(),
            settlementTime = Arb.dateTime().sample(it).value.toString(),
            source = Arb.string().toString(),
            sourceSubType = Arb.string().toString(),
            status = Arb.string().toString(),
            transactingApplicationUserUid = Arb.string().toString(),
            counterPartyType = Arb.string().toString(),
            counterPartyName = Arb.string().toString(),
            counterPartyUid = Arb.string().toString(),
            counterPartySubEntityUid = Arb.string().toString(),
            reference = Arb.string().toString(),
            country = Arb.string().toString(),
            spendingCategory = Arb.string().toString(),
            hasAttachment = Arb.string().toString(),
            batchPaymentDetails = Arb.string().toString(),
            roundUpDetails = Arb.associatedFeedRoundUp().sample(it).value

        )
    }

    fun Arb.Companion.amount() = arbitrary {
        Amount(
            currency = Exhaustive.enum<Currency>().toArb().sample(it).value.toString(),
            minorUnits = Arb.int().sample(it).value
        )
    }

    fun Arb.Companion.associatedFeedRoundUp() = arbitrary {
        AssociatedFeedRoundUp(
            goalCategoryUid = Arb.toString().toString(),
            associatedFeedRoundUpAmount = Arb.amount().sample(it).value
        )
    }

    fun Arb.Companion.savingsGoal() = arbitrary {
        SavingsGoal(
            savingsGoalUid = Arb.string().toString(),
            name = Arb.string().toString(),
            totalSaved = Arb.amount().sample(it).value
        )
    }

    enum class AccountType {
        PRIMARY, ADDITIONAL, LOAN, FIXED_TERM_DEPOSIT
    }

    enum class Currency {
        UNDEFINED, AED, AFN, ALL, AMD, ANG, AOA, ARS, AUD, AWG, AZN, BAM, BBD, BDT, BGN, BHD, BIF, BMD, BND, BOB, BOV, BRL, BSD, BTN, BWP, BYN, BYR, BZD, CAD, CDF, CHE, CHF, CHW, CLF, CLP, CNY, COP, COU, CRC, CUC, CUP, CVE, CZK, DJF, DKK, DOP, DZD, EGP, ERN, ETB, EUR, FJD, FKP, GBP, GEL, GHS, GIP, GMD, GNF, GTQ, GYD, HKD, HNL, HRK, HTG, HUF, IDR, ILS, INR, IQD, IRR, ISK, JMD, JOD, JPY, KES, KGS, KHR, KMF, KPW, KRW, KWD, KYD, KZT, LAK, LBP, LKR, LRD, LSL, LTL, LYD, MAD, MDL, MGA, MKD, MMK, MNT, MOP, MRO, MRU, MUR, MVR, MWK, MXN, MXV, MYR, MZN, NAD, NGN, NIO, NOK, NPR, NZD, OMR, PAB, PEN, PGK, PHP, PKR, PLN, PYG, QAR, RON, RSD, RUB, RUR, RWF, SAR, SBD, SCR, SDG, SEK, SGD, SHP, SLL, SLE, SOS, SRD, SSP, STD, STN, SVC, SYP, SZL, THB, TJS, TMT, TND, TOP, TRY, TTD, TWD, TZS, UAH, UGX, USD, USN, USS, UYI, UYU, UZS, VEF, VES, VND, VUV, WST, XAF, XAG, XAU, XBA, XBB, XBC, XBD, XCD, XDR, XOF, XPD, XPF, XPT, XSU, XTS, XUA, XXX, YER, ZAR, ZMW, ZWL
    }

    enum class Direction {
        IN, OUT
    }
}