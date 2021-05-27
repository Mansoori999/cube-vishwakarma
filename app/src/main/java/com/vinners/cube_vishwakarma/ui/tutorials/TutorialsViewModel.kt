package com.vinners.cube_vishwakarma.ui.tutorials

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOutletList
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.models.tutorials.TutorialsResponse
import com.vinners.cube_vishwakarma.data.repository.TutorialsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface TutorialsEvents {


    val tutorialsState: LiveData<Lce<List<TutorialsResponse>>>


}

class TutorialsViewModel @Inject constructor(
        private val tutorialsRepository: TutorialsRepository
) :ViewModel(),TutorialsEvents{

    private val _tutorialsState =  MutableLiveData<Lce<List<TutorialsResponse>>>()
    override val tutorialsState: LiveData<Lce<List<TutorialsResponse>>> = _tutorialsState
    fun getTutorialsData() {
        _tutorialsState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = tutorialsRepository.getTutorialsData()
                _tutorialsState.postValue(Lce.content(response))
            }catch (e : Exception){
                _tutorialsState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }


}