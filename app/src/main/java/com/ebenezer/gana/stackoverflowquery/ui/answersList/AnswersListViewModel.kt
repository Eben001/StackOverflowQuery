package com.ebenezer.gana.stackoverflowquery.ui.answersList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.stackoverflowquery.App.Companion.repository
import com.ebenezer.gana.stackoverflowquery.data.model.response.Answer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnswersListViewModel : ViewModel() {

    private val _answersList = MutableLiveData<List<Answer>>()
    val answersList:LiveData<List<Answer>> = _answersList

    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error:LiveData<String?> = _error

    var questionId = 0
    var page = 0

    fun getNextPage(questionId:Int?){
        questionId?.let {
            this.questionId = it
            page++
            getAnswers()
        }
    }

    private fun getAnswers() = try {
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getAnswers(questionId, page)
            if(response.isSuccessful){
                val answers = response.body()
                answers?.let {
                    withContext(Dispatchers.Main){
                        _answersList.value = answers.items
                        _loading.value = false
                        _error.value = null


                    }
                }
            }
        }
    }catch (error:Throwable){
        onError(error.localizedMessage)
    }

    private fun onError(message:String?) {
        _error.value = message
        _loading.value = false

    }


}