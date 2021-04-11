package com.example.feature_profile.ui.kyc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.bank.Bank
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.models.profile.ProfileDetails
import com.vinners.trumanms.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface KycEvents {
    val kycState: LiveData<Lse>

    val profileDetailsState: LiveData<Lce<ProfileDetails>>
}

class KycViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel(), KycEvents {

    private val _kycState = MutableLiveData<Lse>()
    override val kycState: LiveData<Lse> = _kycState

    private val _profileDetalState = MutableLiveData<Lce<ProfileDetails>>()
    override val profileDetailsState: LiveData<Lce<ProfileDetails>> = _profileDetalState

    fun uploadDocumentss(docKey: String?, bank: Bank, imageFile: String?, imageFile_2: String?) {
        _kycState.value = Lse.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
               profileRepository.updateAdharAndPan(docKey, bank, imageFile,imageFile_2)
                _kycState.postValue(Lse.Success)
            } catch (e: Exception) {
                _kycState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun getProfileDetails() {
        viewModelScope.launch {
            _profileDetalState.value = Lce.Loading
            try {
                val response = profileRepository.getProfileDetails()
                _profileDetalState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _profileDetalState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
}