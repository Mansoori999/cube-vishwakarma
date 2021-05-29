package com.vinners.cube_vishwakarma.ui.outlets

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.data.repository.OutletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mobile.androidbase.location.LocationUtils
import java.io.File
import javax.inject.Inject



interface OutletEvents {

    val outletListState: LiveData<Lce<List<OutletsList>>>

    val outletDetailsListState : LiveData<Lce<OutletDetailsList>>

    val  complaintsbyoutletListState: LiveData<Lce<List<MyComplaintList>>>
   val complaintStatusState : LiveData<Lce<List<MyComplaintList>>>

}

sealed class UploadEditOutletState {

    object EditOutletDataLoading : UploadEditOutletState()

    object OutletDataUpdated : UploadEditOutletState()

    data class ErrorInUpDateEditOutletData(
        val error: String
    ) : UploadEditOutletState()
}
class OutletsViewModel @Inject constructor(
    private val outletRepository: OutletRepository,
    private val geocoder: Geocoder
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

    /* Outlet Details*/
    private val _uploadEditOutletDataState = MutableLiveData<UploadEditOutletState>()
    val uploadEditOutletDataState: LiveData<UploadEditOutletState> = _uploadEditOutletDataState

    fun upDateEditOutletData(
        outletid: String?,
        secondarymail: String?,
        secondarymobile: String?,
        latitude: Double,
        longitude: Double,
        images: List<String>,
        pic:String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _uploadEditOutletDataState.postValue(UploadEditOutletState.EditOutletDataLoading)

            val gps = "$latitude,$longitude"
            val fullAddressFromGps = LocationUtils.addressFromLocation(
                geoCoder = geocoder,
                latitude = latitude,
                longitude = longitude
            )

            outletRepository.editOutlet(
               outletid,
               secondarymail,
                secondarymobile,
                gps,
                fullAddressFromGps,
                images,
                pic
            )

            _uploadEditOutletDataState.postValue(UploadEditOutletState.OutletDataUpdated)
        } catch (e: Exception) {

            _uploadEditOutletDataState.postValue(
                UploadEditOutletState.ErrorInUpDateEditOutletData(
                    e.toString()
                )
            )
            e.printStackTrace()
        }

    }

    /* Outlet complaints*/
    private val _complaintsbyoutletListState = MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintsbyoutletListState: LiveData<Lce<List<MyComplaintList>>> = _complaintsbyoutletListState

    fun getComplaintByOutletId(outletid : String) {
        _complaintsbyoutletListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = outletRepository.getComplaintsByOutletid(outletid)
                _complaintsbyoutletListState.postValue(Lce.content(response))
            } catch (e: Exception) {
                _complaintsbyoutletListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    /* dashboard Complaint  */

    private val _complaintStatusState =  MutableLiveData<Lce<List<MyComplaintList>>>()
    override val complaintStatusState: LiveData<Lce<List<MyComplaintList>>> = _complaintStatusState

    fun getComplaintWithStatus(status : String,startDate : String,endDate : String,regionalOfficeIds : String?) {
        _complaintStatusState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = outletRepository.getComplaintWithStatus(status,startDate,endDate,regionalOfficeIds)
                _complaintStatusState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintStatusState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }
}