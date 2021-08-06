package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.repository.MyComplaintRepository
import javax.inject.Inject

class MyComplaintSharedViewModel @Inject constructor(): ViewModel() {

//    private val _complaintListState = MutableLiveData<List<MyComplaintList>>()
//   val complaintListState: LiveData<List<MyComplaintList>> = _complaintListState
//
//    fun saveAllComplaints(complaints: List<MyComplaintList>){
//        _complaintListState.value = complaints
//    }

    val _complaintListState = MutableLiveData<List<MyComplaintList>>()
    val getComplaints: List<MyComplaintList>? = _complaintListState.value

    fun setcomplaints(value: List<MyComplaintList>) {
        _complaintListState.value = value
    }
}