package com.vinners.cube_vishwakarma.ui.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.data.repository.DocumentsRepository
import com.vinners.cube_vishwakarma.data.repository.MyComplaintRepository
import com.vinners.cube_vishwakarma.data.repository.OutletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


interface DocumentsEvents {


    val documentState: LiveData<Lce<List<DocumentsResponse>>>

    val complaintListState: LiveData<Lce<List<MyComplaintList>>>

    val outletListState: LiveData<Lce<List<OutletsList>>>

    val outletState: LiveData<Lce<List<OutletsList>>>

    val outletStateWithOR: LiveData<Lce<List<OutletsList>>>



}

class DocumentsViewModel @Inject constructor(
    private val documentsRepository: DocumentsRepository,
    private val myComplaintRepository: MyComplaintRepository,
    private val outletRepository: OutletRepository
):ViewModel(),DocumentsEvents {

    private var complaintList: List<MyComplaintList>? = null
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

    fun getComplaintList(): List<MyComplaintList>? {
        return complaintList
    }

    private val _complaintListState = MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintListState: LiveData<Lce<List<MyComplaintList>>> = _complaintListState

    fun getComplaintList(userid:String){
        _complaintListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.getComplaint(userid)
                _complaintListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    private val _outletListState = MutableLiveData<Lce<List<OutletsList>>>()
    override val outletListState: LiveData<Lce<List<OutletsList>>> = _outletListState

    fun getOutletData() {
        _outletListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = outletRepository.getOutletData()
                _outletListState.postValue(Lce.content(response))
            } catch (e: Exception) {
                _outletListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    //    //databae getDataByID
    private val _outletState = MutableLiveData<Lce<List<OutletsList>>>()
    override val outletState: LiveData<Lce<List<OutletsList>>> = _outletState
    fun getOutletsById(roid:List<Int> , said:List<Int>){
        _outletState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = outletRepository.getOutletsBYID(roid,said)
                _outletState.postValue(Lce.content(response))
            }catch (e:Exception){
                _outletState.postValue(Lce.error(e.localizedMessage))
            }
        }
    }

    private val _outletStateWithOR = MutableLiveData<Lce<List<OutletsList>>>()
    override val outletStateWithOR: LiveData<Lce<List<OutletsList>>> = _outletStateWithOR
    fun getOutletsByIdWithOR(roid:List<Int> , said:List<Int>){
        _outletStateWithOR.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = outletRepository.getOutletsBYIDWithOR(roid,said)
                _outletStateWithOR.postValue(Lce.content(response))
            }catch (e:Exception){
                _outletStateWithOR.postValue(Lce.error(e.localizedMessage))
            }
        }
    }
}
