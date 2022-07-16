package com.ebenezer.gana.stackoverflowquery.data.model.repository

import com.ebenezer.gana.stackoverflowquery.data.model.response.Answer
import com.ebenezer.gana.stackoverflowquery.data.model.response.Question
import com.ebenezer.gana.stackoverflowquery.data.model.response.ResponseWrapper
import com.ebenezer.gana.stackoverflowquery.data.network.StackOverflowApi
import retrofit2.Response

class StackOverflowRepositoryImpl(private val stackOverflowApi: StackOverflowApi) :
    StackOverflowRepository {

        override suspend fun getQuestions(page: Int): Response<ResponseWrapper<Question>> =
            stackOverflowApi.getQuestions(page)


    override suspend fun getAnswers(questionId: Int, page: Int): Response<ResponseWrapper<Answer>> =
        stackOverflowApi.getAnswers(questionId, page)
}