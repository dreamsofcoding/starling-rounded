package com.cd.rounded.backend

import com.cd.rounded.Constants.ACCESS_TOKEN
import com.cd.rounded.domain.AccountsResponse
import com.cd.rounded.domain.FeedItemsResponse
import com.cd.rounded.domain.SavingsGoalTransferAmount
import com.cd.rounded.domain.SavingsGoalsResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.OffsetDateTime
import java.util.UUID

//Base Url
private const val BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/"
//Customer' accounts API
private const val ACCOUNTS_API = "accounts"
//Transaction Feed Api, requires Account, Category and Time period
private const val TRANSACTIONS_FEED_API = "feed/account/{account}/category/{category}/transactions-between"
//Customer' savings goals API
private const val SAVING_GOALS_API = "account/{account}/savings-goals"
//Add Money to Saving Goal API
private const val ADD_MONEY_SAVINGS_GOAL_API = "account/{account}/savings-goals/{savings-goal}/add-money/{transfer}"
//Access Token
private val BEARER_TOKEN = "Bearer $ACCESS_TOKEN"


class Retrofit (private val okHttpClient: OkHttpClient) {

    fun createStarlingService(): StarlingService {
        val client = okHttpClient.newBuilder()
            .addInterceptor(StarlingTokenInterceptor())
            .build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build().create(StarlingService::class.java)
    }
}

private class StarlingTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original = chain.request()
        val originalUrl = original.url()
        val url = originalUrl.newBuilder().build()

        val requestBuilder = original.newBuilder()
            .url(url)
            .header("Authorization", BEARER_TOKEN)
            .header("User-Agent", "Coding Dreams")

        return chain.proceed(requestBuilder.build())
    }
}

interface StarlingService {

    /**
     * GET Call to retrieve the customer' accounts. Returns a list of accounts in a
     */
    @GET(ACCOUNTS_API)
    @Headers("Content-Type: application/json")
    suspend fun getAllAccounts(): Response<AccountsResponse>

    /**
     * GET Call to retrieve the customer' transactions for a specified week period.
     * @param account string UID of the account being queried.
     * @param category string UID of the account category
     * @param minTransactionTimestamp offsetDateTime, in ISO8601DateFormat.
     * @param week offsetDateTime, in ISO8601DateFormat.
     */
    @GET(TRANSACTIONS_FEED_API)
    @Headers("Content-Type: application/json")
    suspend fun getTransactionFeed(
        @Path("account") account: String,
        @Path("category") category: String,
        @Query("minTransactionTimestamp") minTimeStamp: OffsetDateTime,
        @Query("maxTransactionTimestamp") maxTimeStamp: OffsetDateTime,
        ): Response<FeedItemsResponse>

    /**
     * GET Call to retrieve the customer' saving goals.
     * @param account string UID of the account being queried.
     */
    @GET(SAVING_GOALS_API)
    @Headers("Content-Type: application/json")
    suspend fun getSavingGoals(
        @Path("account") account: String,
    ): Response<SavingsGoalsResponse>

    /**
     * PUT Call to credit the customer' saving goal with the roundup amount. Requires a
     * random UID for the transfer to be accepted.
     * @param account string UID of the account being queried.
     * @param savingsGoal string UID of the account category
     * @param transferUid string UID of the transfer (Randomly generated on the fly per request)
     * @param addAmount amount object specifying currency and minor units of the roundup.
     */
    @PUT(ADD_MONEY_SAVINGS_GOAL_API)
    @Headers("Content-Type: application/json")
    suspend fun putSavingAmount(
        @Path("account") account: String,
        @Path("savings-goal") savingsGoal: String,
        @Path("transfer") transferUid: String? = UUID.randomUUID().toString(),
        @Body addAmount: SavingsGoalTransferAmount
    ): Response<JsonObject>

}