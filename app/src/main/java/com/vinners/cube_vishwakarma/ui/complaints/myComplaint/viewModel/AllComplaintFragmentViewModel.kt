package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.*
import com.vinners.cube_vishwakarma.data.repository.MyComplaintRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


interface ComplaintEvents {

    val complaintListState: LiveData<Lce<List<MyComplaintList>>>

    val complaintDetailsState :LiveData<Lce<MyComplainDetailsList>>

    val upDateListState:LiveData<Lce<List<String>>>

    val allocateUserListState: LiveData<Lce<List<AllocateUserResponse>>>

    val requestforAllocatedUserListState: LiveData<Lce<List<String>>>

    val complaintDaoListState: LiveData<Lce<List<MyComplaintList>>>

    val complaintDaoROANDSaidListState: LiveData<Lce<List<MyComplaintList>>>

    val complaintDaoROORSaidListState: LiveData<Lce<List<MyComplaintList>>>

    val complaintDaoAllANDListState: LiveData<Lce<List<MyComplaintList>>>

    val complaintDaoWithSubAminORListState: LiveData<Lce<List<MyComplaintList>>>

    val complaintDaoWithSubadminAndROListState: LiveData<Lce<List<MyComplaintList>>>
}
class AllComplaintFragmentViewModel @Inject constructor(
        private val myComplaintRepository: MyComplaintRepository

): ViewModel(),ComplaintEvents {

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

    //GET ALl Complaints From DataBase
    private val _complaintDaoListState = MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintDaoListState: LiveData<Lce<List<MyComplaintList>>> = _complaintDaoListState

    fun getComplaintDaoList(){
        _complaintDaoListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.getAllMyComplaintsDao()
                _complaintDaoListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintDaoListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }
    private val _complaintDaoROANDSaidListState = MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintDaoROANDSaidListState: LiveData<Lce<List<MyComplaintList>>> = _complaintDaoROANDSaidListState

    fun getComplaintBYIDROANDSaid(roid:List<Int> , said:List<Int>){
        _complaintDaoROANDSaidListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.getComplaintBYIDAND(roid,said)
                _complaintDaoROANDSaidListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintDaoROANDSaidListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    private val _complaintDaoROORSaidListState = MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintDaoROORSaidListState: LiveData<Lce<List<MyComplaintList>>> = _complaintDaoROORSaidListState

    fun getComplaintBYIDWithOR(roid:List<Int> , said:List<Int>){
        _complaintDaoROORSaidListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.getComplaintBYIDWithOR(roid,said)
                _complaintDaoROORSaidListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintDaoROORSaidListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    private val _complaintDaoAllANDListState = MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintDaoAllANDListState: LiveData<Lce<List<MyComplaintList>>> = _complaintDaoAllANDListState

    fun getComplaintByAllIDAND(roid:List<Int> , said:List<Int>,subadmin:List<Int>){
        _complaintDaoAllANDListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.getComplaintByAllIDAND(roid,said,subadmin)
                _complaintDaoAllANDListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintDaoAllANDListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    private val _complaintDaoWithSubAminORListState = MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintDaoWithSubAminORListState: LiveData<Lce<List<MyComplaintList>>> = _complaintDaoWithSubAminORListState

    fun getComplaintByIDWithSubAminOR(roid:List<Int> , said:List<Int>,subadmin:List<Int>){
        _complaintDaoWithSubAminORListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.getComplaintByIDWithSubAminOR(roid,said,subadmin)
                _complaintDaoWithSubAminORListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintDaoWithSubAminORListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    private val _complaintDaoWithSubadminAndROListState = MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintDaoWithSubadminAndROListState: LiveData<Lce<List<MyComplaintList>>> = _complaintDaoWithSubadminAndROListState

    fun getComplaintByIDWithSubadminAndRO(roid:List<Int> , subadmin:List<Int>){
        _complaintDaoWithSubadminAndROListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.getComplaintByIDWithSubadminAndRO(roid,subadmin)
                _complaintDaoWithSubadminAndROListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintDaoWithSubadminAndROListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

//shared viewMoel
    private val data: MutableLiveData<List<MyComplaintList>> = MutableLiveData()
    fun data(): LiveData<List<MyComplaintList>> = data

    fun updateData(message: List<MyComplaintList>) {
        data.value = message
    }
    /*Complaint Details */

    private val _complaintDetailsState = MutableLiveData<Lce<MyComplainDetailsList>>()
    override val complaintDetailsState: LiveData<Lce<MyComplainDetailsList>> = _complaintDetailsState

    fun getComplainDetails(id : String){
        _complaintDetailsState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.getComplaintDetails(id)
                _complaintDetailsState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintDetailsState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    /* Upload Complaint*/
    private val _upDateListState = MutableLiveData<Lce<List<String>>>()
    override val upDateListState: LiveData<Lce<List<String>>> = _upDateListState
//    letterPic: File?,
//    measurementPic:File?,
//    layoutPic:File?

fun upDateComplaints(
            statusremarks: String,
            status: String,
            id: Int,
            letterPic: File?,
            measurementPic:File?,
            layoutPic:File?
    ){
        _upDateListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.upDateComplaints(statusremarks,status,id,
                    letterPic?.absolutePath, measurementPic?.absolutePath, layoutPic?.absolutePath
                )
                _upDateListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _upDateListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    /* Upload Complaint*/
    private val _allocateUserListState = MutableLiveData<Lce<List<AllocateUserResponse>>>()
    override val allocateUserListState: LiveData<Lce<List<AllocateUserResponse>>> = _allocateUserListState

    fun allocateUserForComplaint(){
        _allocateUserListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.allocateUserForComplaint()
                _allocateUserListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _allocateUserListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    /* Upload Complaint*/
    private val _requestforAllocatedUserListState = MutableLiveData<Lce<List<String>>>()
    override val requestforAllocatedUserListState: LiveData<Lce<List<String>>> = _requestforAllocatedUserListState

    fun requestforAllocatedUserForComplaint(supervisorid:String?,enduserid:String?,foremanid: String?,compid: String){
        _requestforAllocatedUserListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.requestAllocatedUserForComplaint(supervisorid,enduserid,foremanid,compid)
                _requestforAllocatedUserListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _requestforAllocatedUserListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }


}