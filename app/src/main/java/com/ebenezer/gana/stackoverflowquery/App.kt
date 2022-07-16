package com.ebenezer.gana.stackoverflowquery

import android.app.Application
import com.ebenezer.gana.stackoverflowquery.data.model.repository.StackOverflowRepository
import com.ebenezer.gana.stackoverflowquery.data.model.repository.StackOverflowRepositoryImpl
import com.ebenezer.gana.stackoverflowquery.data.network.buildApiService

class App:Application() {

    companion object {
        private val apiService by lazy {
            buildApiService()
        }

        val repository: StackOverflowRepository
                by lazy { StackOverflowRepositoryImpl(apiService) }
    }
}