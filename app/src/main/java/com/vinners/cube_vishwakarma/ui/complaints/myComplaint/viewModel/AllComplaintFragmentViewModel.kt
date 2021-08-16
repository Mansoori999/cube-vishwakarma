package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.complaints.*
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.repository.MyComplaintRepository
import com.vinners.cube_vishwakarma.data.repository.ProfileRepository
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