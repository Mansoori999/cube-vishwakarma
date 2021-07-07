package com.vinners.cube_vishwakarma.ui.outlets

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOutletList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.data.repository.OutletRepository
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.LoadMetaForAddComplaintState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mobile.androidbase.location.LocationUtils
import javax.inject.Inject



interface OutletEvents {

   val outletListState: LiveData<Lce<List<OutletsList>>>

    val outletState: LiveData<Lce<List<OutletsList>>>

    val outletDetailsListState : LiveData<Lce<OutletDetailsList>>

    val outletStateWithOR: LiveData<Lce<List<OutletsList>>>

    val  complaintsbyoutletListState: LiveData<Lce<List<MyComplaintList>>>
   val complaintStatusState : LiveData<Lce<List<MyComplaintList>>>

}
sealed class LoadMetaForAddOutletState {

    object LoadingLoadMetaForAddOutletState : LoadMetaForAddOutletState()


    data class MetaForAddOutletStateLoaded(
            val regionalAndSalesInfo: List<OutletsList>
    ) : LoadMetaForAddOutletState()

    data class ErrorInLoadingMetaForAddOutletLoaded(
            val error: String
    ) : LoadMetaForAddOutletState()
}
sealed class UploadEditOutletState {

    object EditOutletDataLoading : UploadEditOutletState()

    object OutletDataUpdated : UploadEditOutletState()

    data class ErrorInUpDateEditOutletData(
        val error: String
    ) : UploadEditOutletState()
}

sealed class UploadEditOutletGPSState {

    object EditOutletDataGPSLoading : UploadEditOutletGPSState()

    object OutletDataGPSUpdated : UploadEditOutletGPSState()

    data class ErrorInUpDateEditOutletGPSData(
        val error: String
    ) : UploadEditOutletGPSState()
}

class OutletsViewModel @Inject constructor(
    private val outletRepository: OutletRepository,
    private val geocoder: Geocoder
): ViewModel(),OutletEvents {

//    private val _loadRegionalAndSalesInfo = MutableLiveData<LoadMetaForAddOutletState>()
//    val loadRegionalAndSalesInfo: LiveData<LoadMetaForAddOutletState> = _loadRegionalAndSalesInfo
//
//    var outletRequestList: List<OutletsList>? = null
//
//    fun getOutletstData() = viewModelScope.launch {
//
//        try {
//            _loadRegionalAndSalesInfo.postValue(LoadMetaForAddOutletState.LoadingLoadMetaForAddOutletState)
//
//            outletRequestList = outletRepository.getOutletData()
//            _loadRegionalAndSalesInfo.postValue(
//                    LoadMetaForAddOutletState.MetaForAddOutletStateLoaded(
//                            outletRequestList!!
//                    )
//            )
//        } catch (e: Exception) {
//            _loadRegionalAndSalesInfo.postValue(
//                    LoadMetaForAddOutletState.ErrorInLoadingMetaForAddOutletLoaded(
//                            e.toString()
//                    )
//            )
//            e.printStackTrace()
//        }
//    }

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
//    private val _outletInsertState = MutableLiveData<Lce<List<OutletsList>>>()
//    override val outletInsertState: LiveData<Lce<List<OutletsList>>> = _outletInsertState
//
//    fun insertOutlets(outlets :List<OutletsList>){
//        _outletInsertState.value = Lce.Loading
//        viewModelScope.launch(Dispatchers.IO){
//            try {
//                val response = outletRepository.insertOutlets(outlets)
//                _outletState.postValue(Lce.content(response))
//            }catch (e:Exception){
//                _outletInsertState.postValue(Lce.error(e.localizedMessage))
//            }
//        }
//    }


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
        images: List<String>,
        pic:String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _uploadEditOutletDataState.postValue(UploadEditOutletState.EditOutletDataLoading)

            outletRepository.editOutlet(
               outletid,
               secondarymail,
                secondarymobile,
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

    private val _uploadEditOutletGPSDataState = MutableLiveData<UploadEditOutletGPSState>()
    val uploadEditOutletGPSDataState: LiveData<UploadEditOutletGPSState> = _uploadEditOutletGPSDataState

    fun editOutletGps(
        outletid: String?,
        latitude: Double,
        longitude: Double
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _uploadEditOutletGPSDataState.postValue(UploadEditOutletGPSState.EditOutletDataGPSLoading)

            val gps = "$latitude,$longitude"
            val fullAddressFromGps = LocationUtils.addressFromLocation(
                geoCoder = geocoder,
                latitude = latitude,
                longitude = longitude
            )

            outletRepository.editOutletGps(
                outletid,
                gps,
                fullAddressFromGps
            )

            _uploadEditOutletGPSDataState.postValue(UploadEditOutletGPSState.OutletDataGPSUpdated)
        } catch (e: Exception) {

            _uploadEditOutletGPSDataState.postValue(
                UploadEditOutletGPSState.ErrorInUpDateEditOutletGPSData(
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

    fun getComplaintWithStatus(status : String,startDate : String,endDate : String,regionalOfficeIds : String?,subadminIds:String?) {
        _complaintStatusState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = outletRepository.getComplaintWithStatus(status,startDate,endDate,regionalOfficeIds,subadminIds)
                _complaintStatusState.postValue(Lce.content(response))
            }catch (e : Exception){
                _complaintStatusState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }
}