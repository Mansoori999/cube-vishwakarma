package com.vinners.trumanms.feature_help.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.help.Help
import com.vinners.trumanms.data.models.help.Query
import com.vinners.trumanms.data.models.help.QueryRequest
import com.vinners.trumanms.data.repository.HelpRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

interface HelpEvents {
    val helpState: LiveData<Lce<List<Help>>>

    val queryState: LiveData<Lce<List<Query>>>

    val submitQuery: LiveData<Lse>
}

class HelpViewModel @Inject constructor(
    private val helpRepository: HelpRepository
) : ViewModel(), HelpEvents {

    private val _helpState = MutableLiveData<Lce<List<Help>>>()
    override val helpState: LiveData<Lce<List<Help>>> = _helpState

    private val _queryState = MutableLiveData<Lce<List<Query>>>()
    override val queryState: LiveData<Lce<List<Query>>> = _queryState

    private val _submitQuery = MutableLiveData<Lse>()
    override val submitQuery: LiveData<Lse> = _submitQuery

    fun getHelpList() {
        viewModelScope.launch {
            _helpState.value = Lce.Loading
            try {
                val response = helpRepository.getQueAns()
                _helpState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _helpState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun submitQuery(queryRequest: QueryRequest,imageFile: String?) {
        viewModelScope.launch {
            _submitQuery.value = Lse.Loading
            try {
                helpRepository.submitQuery(queryRequest,imageFile)
                _submitQuery.postValue(Lse.Success)
            } catch (e: Exception) {
                _submitQuery.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun getQuery() {
        viewModelScope.launch {
            _queryState.value = Lce.Loading
            try {
                val response = helpRepository.getQuery()
                _queryState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _queryState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
}