package com.example.feature_profile.ui.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.Documents
import com.vinners.trumanms.data.repository.ProfileRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DocumentsEvents {
    val documentsStates: LiveData<Lce<List<Documents>>>
}

class DocumentsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel(), DocumentsEvents {

    private val _documentsStates = MutableLiveData<Lce<List<Documents>>>()
    override val documentsStates: LiveData<Lce<List<Documents>>> = _documentsStates

    fun getDocuments() {
        viewModelScope.launch {
            _documentsStates.value = Lce.Loading
            try {
                val response = profileRepository.getDocuments()
                _documentsStates.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _documentsStates.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
}