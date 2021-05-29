package com.vinners.cube_vishwakarma.ui.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse
import com.vinners.cube_vishwakarma.data.models.tutorials.TutorialsResponse
import com.vinners.cube_vishwakarma.data.repository.DocumentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


interface DocumentsEvents {


    val documentState: LiveData<Lce<List<DocumentsResponse>>>


}

class DocumentsViewModel @Inject constructor(
    private val documentsRepository: DocumentsRepository
):ViewModel(),DocumentsEvents {

    private val _documentState =  MutableLiveData<Lce<List<DocumentsResponse>>>()
    override val documentState: LiveData<Lce<List<DocumentsResponse>>> = _documentState
    fun getDocumentsData() {
        _documentState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = documentsRepository.getDocumentsData()
                _documentState.postValue(Lce.content(response))
            }catch (e : Exception){
                _documentState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }


}
