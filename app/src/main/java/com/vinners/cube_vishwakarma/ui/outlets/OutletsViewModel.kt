package com.vinners.cube_vishwakarma.ui.outlets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplainDetailsList
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.data.repository.OutletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject



interface OutletEvents {

    val outletListState: LiveData<Lce<List<OutletsList>>>

    val outletDetailsListState : LiveData<Lce<OutletDetailsList>>



}
class OutletsViewModel @Inject constructor(
private val outletRepository: OutletRepository
): ViewModel(),OutletEvents {

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

    /* Outlet Details*/
    private val _outletDetailsListState = MutableLiveData<Lce<OutletDetailsList>>()
    override val outletDetailsListState: LiveData<Lce<OutletDetailsList>> = _outletDetailsListState

    fun getOutletDetailsData(outletid : String) {
        _outletDetailsListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = outletRepository.getOutletDetails(outletid)
                _outletDetailsListState.postValue(Lce.content(response))
            } catch (e: Exception) {
                _outletDetailsListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }


}