package com.ebenezer.gana.stackoverflowquery.data.model.repository

import com.ebenezer.gana.stackoverflowquery.data.model.response.Answer
import com.ebenezer.gana.stackoverflowquery.data.model.response.Question
import com.ebenezer.gana.stackoverflowquery.data.model.response.ResponseWrapper
import retrofit2.Response

interface StackOverflowRepository {

    suspend fun getQuestions(page:Int): Response<ResponseWrapper<Question>>
    suspend fun getAnswers(questionId:Int, page:Int): Response<ResponseWrapper<Answer>>


}