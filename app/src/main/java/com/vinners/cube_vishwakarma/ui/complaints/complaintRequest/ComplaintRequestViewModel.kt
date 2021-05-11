package com.vinners.cube_vishwakarma.ui.complaints.complaintRequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.*
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.data.repository.ComplaintRequestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoadMetaForAddComplaintState {

    object LoadingLoadMetaForAddComplaintState : LoadMetaForAddComplaintState()


    data class MetaForAddComplaintStateLoaded(
        val regionalAndSalesInfo: List<ComplaintOutletList>
    ) : LoadMetaForAddComplaintState()

    data class ErrorInLoadingMetaForAddComplaintLoaded(
        val error: String
    ) : LoadMetaForAddComplaintState()
}


interface ComplaintTypeEvents {

    val complaintTypeListState: LiveData<Lce<List<ComplaintTypeList>>>

    val orderbyListState :  LiveData<Lce<List<ComplaintOrderByList>>>

    val submitListState : LiveData<Lce<List<String>>>

    val viewComplaintRequestListState : LiveData<Lce<List<ComplaintRequestResponse>>>

}
class ComplaintRequestViewModel @Inject constructor(
private val complaintRequestRepository: ComplaintRequestRepository
) : ViewModel(),ComplaintTypeEvents {
    private val _loadRegionalAndSalesInfo = MutableLiveData<LoadMetaForAddComplaintState>()
    val loadRegionalAndSalesInfo: LiveData<LoadMetaForAddComplaintState> = _loadRegionalAndSalesInfo

    var complaintRequestList: List<ComplaintOutletList>? = null

    fun getNewComplaintData() = viewModelScope.launch {

        try {
            _loadRegionalAndSalesInfo.postValue(LoadMetaForAddComplaintState.LoadingLoadMetaForAddComplaintState)

            complaintRequestList = complaintRequestRepository.getComplaintList()
            _loadRegionalAndSalesInfo.postValue(
                LoadMetaForAddComplaintState.MetaForAddComplaintStateLoaded(
                    complaintRequestList!!
                )
            )
        } catch (e: Exception) {
            _loadRegionalAndSalesInfo.postValue(
                LoadMetaForAddComplaintState.ErrorInLoadingMetaForAddComplaintLoaded(
                    e.toString()
                )
            )
            e.printStackTrace()
        }
    }

    /* complaintType */

    private val _complaintTypeListState = MutableLiveData<Lce<List<ComplaintTypeList>>>()
    override val complaintTypeListState: LiveData<Lce<List<ComplaintTypeList>>> = _complaintTypeListState

    fun getComplaintTypeData(){
        _complaintTypeListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = complaintRequestRepository.getComplaintType()
                _complaintTypeListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintTypeListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    /* order By */
    private val _orderbyListState = MutableLiveData<Lce<List<ComplaintOrderByList>>>()
    override val orderbyListState: LiveData<Lce<List<ComplaintOrderByList>>> = _orderbyListState

    fun getOrderByData(){
        _orderbyListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = complaintRequestRepository.getOrderByList()
                _orderbyListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _orderbyListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    /* Submit complaint Request Data */

    private val _submitListState = MutableLiveData<Lce<List<String>>>()
    override val submitListState: LiveData<Lce<List<String>>> = _submitListState

    fun submitComplaintRequest(
            typeid :String,
            work:String,
            outletid:String,
            remarks:String,
            orderby:String){
        _submitListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = complaintRequestRepository.submitComplaintRequestData(typeid,work,outletid,remarks,orderby)
                _submitListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _submitListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    /* View complaint Request Data */

    private val _viewComplaintRequestListState = MutableLiveData<Lce<List<ComplaintRequestResponse>>>()
    override val viewComplaintRequestListState: LiveData<Lce<List<ComplaintRequestResponse>>> = _viewComplaintRequestListState

    fun getComplaintRequestView(startDate: String,endDate: String){
        _viewComplaintRequestListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = complaintRequestRepository.getViewComplaintRequest(startDate,endDate)
                _viewComplaintRequestListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _viewComplaintRequestListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }
}