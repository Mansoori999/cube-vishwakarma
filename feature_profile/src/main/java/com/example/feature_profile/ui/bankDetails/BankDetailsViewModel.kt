package com.example.feature_profile.ui.bankDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.bank.Bank
import com.vinners.trumanms.data.models.bank.BankDetails
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.models.profile.ProfileDetails
import com.vinners.trumanms.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface BankDetailsEvents {
    val bankDetailsState: LiveData<Lce<BankDetails>>

    val profileState: LiveData<Lce<Profile?>>

    val uploadBankDetailsState: LiveData<Lse>
}

class BankDetailsViewModel @Inject constructor(
    private val userrProfileRepository: ProfileRepository
) : ViewModel(), BankDetailsEvents {

    private val _bankDetailsState = MutableLiveData<Lce<BankDetails>>()
    override val bankDetailsState: LiveData<Lce<BankDetails>> = _bankDetailsState

    private val _profileState = MutableLiveData<Lce<Profile?>>()
    override val profileState: LiveData<Lce<Profile?>> = _profileState

    private val _uploadBankDetailsState = MutableLiveData<Lse>()
    override val uploadBankDetailsState: LiveData<Lse> = _uploadBankDetailsState



    fun getBankDetails(ifsc: String) {
        viewModelScope.launch {
            _bankDetailsState.value = Lce.Loading
            try {
                val response = userrProfileRepository.getBankDetails(ifsc)
                _bankDetailsState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _bankDetailsState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun getCachedProfile() {
        _profileState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profile = userrProfileRepository.getCachedProfile()
                _profileState.postValue(Lce.content(profile))
            } catch (e: Exception) {
                _profileState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun uploadBankDetails(bankKey: String?, bank: Bank, imageFile: String?,imageFile_2: String?) {
        _uploadBankDetailsState.value = Lse.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userrProfileRepository.updateBankDetails(bankKey, bank, imageFile,imageFile_2)
                _uploadBankDetailsState.postValue(Lse.Success)
            } catch (e: Exception) {
                _uploadBankDetailsState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }


}