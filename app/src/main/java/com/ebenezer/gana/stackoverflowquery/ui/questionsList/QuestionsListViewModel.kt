package com.ebenezer.gana.stackoverflowquery.ui.questionsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.stackoverflowquery.App.Companion.repository
import com.ebenezer.gana.stackoverflowquery.data.model.response.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionsListViewModel : ViewModel() {
    private val _questionsList = MutableLiveData<List<Question>>()
    val questionsList: LiveData<List<Question>> = _questionsList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    private var page = 0
    fun getNextPage() {
        page++
        getQuestions()
    }

    fun getFirstPage() {
        page = 1
        getQuestions()
    }

    private fun getQuestions() = try {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getQuestions(page)
            if (response.isSuccessful) {
                val questions = response.body()
                questions?.let {
                    withContext(Dispatchers.Main) {
                        _questionsList.value = questions.items
                        _loading.value = false

                    }
                }
            }


        }
    } catch (error: Throwable) {
        _error.value = error.localizedMessage
        _loading.value = false


    }

}