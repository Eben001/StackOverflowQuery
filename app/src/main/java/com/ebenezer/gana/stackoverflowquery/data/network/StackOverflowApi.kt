package com.ebenezer.gana.stackoverflowquery.data.network

import com.ebenezer.gana.stackoverflowquery.data.model.response.Answer
import com.ebenezer.gana.stackoverflowquery.data.model.response.Question
import com.ebenezer.gana.stackoverflowquery.data.model.response.ResponseWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackOverflowApi {

    @GET("/2.3/questions?order=desc&sort=votes&tagged=android&site=stackoverflow")
    suspend fun getQuestions(@Query("page") page: Int): Response<ResponseWrapper<Question>>

    @GET("/2.3/answers/{id}?order=desc&sort=votes&site=stackoverflow")
    suspend fun getAnswers(
        @Path("id") questionId: Int,
        @Query("page") page: Int
    ): Response<ResponseWrapper<Answer>>
}