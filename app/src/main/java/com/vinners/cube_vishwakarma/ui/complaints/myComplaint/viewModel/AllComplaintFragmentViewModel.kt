package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.complaints.ComplaintUserIdRequet
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplainDetailsList
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.complaints.UpDateComplaintList
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.repository.MyComplaintRepository
import com.vinners.cube_vishwakarma.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


interface ComplaintEvents {

    val complaintListState: LiveData<Lce<List<MyComplaintList>>>

    val complaintDetailsState :LiveData<Lce<MyComplainDetailsList>>

    val upDateListState:LiveData<Lce<List<String>>>


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

    fun upDateComplaints(statusremarks:String, status:String, id:Int,image: List<String>){
        _upDateListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = myComplaintRepository.upDateComplaints(statusremarks,status,id,image)
                _upDateListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _upDateListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

}