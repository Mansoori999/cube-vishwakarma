package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyComplaintSharedViewModel : ViewModel() {

    val _complaintListState = MutableLiveData<List<MyComplaintList>>()
    val getComplaints: List<MyComplaintList>? = _complaintListState.value

    fun setcomplaints(value: List<MyComplaintList>) {
        _complaintListState.value = value
    }

    //shared viewMoel
//    private val data: MutableLiveData<Lce<<List<MyComplaintList>>> = MutableLiveData()
//    fun data(): LiveData<Lce<<List<MyComplaintList>>> = data

    private val data =  MutableLiveData<Lce<List<MyComplaintList>>>()
    fun data(): LiveData<Lce<List<MyComplaintList>>> = data
    fun updateData(message: List<MyComplaintList>) {
        data.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                data.postValue(Lce.content(message))
            }catch (e : Exception){
                data.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

//    fun updateData(message: List<MyComplaintList>) {
//        data.value = message
//    }

}