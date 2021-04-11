package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.SingleLiveEvent
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.stateAndCity.*
import com.vinners.trumanms.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface StateEvents{
    val state: LiveData<Lce<List<State>>>

    val city: LiveData<Lce<List<City>>>

    val pincode: LiveData<Lce<List<Pincode>>>

    val workCategory: LiveData<Lce<List<WorkCategory>>>

    val cityAndPincode: LiveData<Lce<CityAndPincode>>
}

class StateCityViewModel @Inject constructor(
private val authRepository: AuthRepository
): ViewModel(),StateEvents {

    private val _state = SingleLiveEvent<Lce<List<State>>>()
    override val state: LiveData<Lce<List<State>>> = _state

    private val _city = SingleLiveEvent<Lce<List<City>>>()
    override val city: LiveData<Lce<List<City>>> = _city

    private val _pincode = SingleLiveEvent<Lce<List<Pincode>>>()
    override val pincode: LiveData<Lce<List<Pincode>>> = _pincode

    private val _workCategory = SingleLiveEvent<Lce<List<WorkCategory>>>()
    override val workCategory: LiveData<Lce<List<WorkCategory>>> = _workCategory

    private val _cityAndPincode = MutableLiveData<Lce<CityAndPincode>>()
    override val cityAndPincode: LiveData<Lce<CityAndPincode>> = _cityAndPincode

    fun getState() {
        viewModelScope.launch {
            _state.value = Lce.Loading
            try {
                val response = authRepository.getState()
                _state.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _state.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
    fun getCity(stateId: String?) {

        viewModelScope.launch {
            _city.value = Lce.Loading
            try {
                val response = authRepository.getCity(stateId)
                _city.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _city.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun getPincode(districtId: String?) {
        _pincode.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.getPincode(districtId)
                _pincode.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _pincode.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun getWorkCategory() {
        _workCategory.value = Lce.loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.getWorkCategory()
                _workCategory.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _workCategory.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun getCityAndPincode(stateId: String?){
        viewModelScope.launch {
            _cityAndPincode.value = Lce.Loading
            try {
                val response = authRepository.getCityAndPincode(stateId)
                _cityAndPincode.postValue(Lce.Content(response))
            }catch (e: Exception){
                _cityAndPincode.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
}