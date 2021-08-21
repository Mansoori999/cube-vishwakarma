package com.vinners.cube_vishwakarma.ui.nearBy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponse
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponseItem
import com.vinners.cube_vishwakarma.data.repository.NearByRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


interface eventNearByMap{
    val nearbyMapListState: LiveData<Lce<List<NearByResponseItem>>>
}
class NearByViewModel@Inject constructor(
private val nearByRepository: NearByRepository
):ViewModel(),eventNearByMap {


    private val _nearbyMapListState = MutableLiveData<Lce<List<NearByResponseItem>>>()
    override val nearbyMapListState: LiveData<Lce<List<NearByResponseItem>>> = _nearbyMapListState

    fun getNearByOutletByMap(
        latitude: Double,
        longitude: Double,
        range:String,
        type:String){
        _nearbyMapListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val gpsaddress = "$latitude,$longitude"
                val response = nearByRepository.getNearByMap(gpsaddress,range,type)
                _nearbyMapListState.postValue(Lce.content(response))
            }catch (e : Exception){
                _nearbyMapListState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }
}