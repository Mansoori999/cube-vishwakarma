package com.example.feature_profile.ui.certificate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.certificate.Certificate
import com.vinners.trumanms.data.repository.ProfileRepository
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

interface CertificateEvents {
    val certificateState: LiveData<Lce<List<Certificate>>>

    val downloadCertificateState: LiveData<Lce<File>>
}

class CertificateViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel(), CertificateEvents {

    private val _certificateState = MutableLiveData<Lce<List<Certificate>>>()
    override val certificateState: LiveData<Lce<List<Certificate>>> = _certificateState

    private val _certificateDownloadState = MutableLiveData<Lce<File>>()
    override val downloadCertificateState: LiveData<Lce<File>> = _certificateDownloadState

    fun getCertificate() {
        viewModelScope.launch {
            _certificateState.value = Lce.Loading
            try {
                val response = profileRepository.getCertificate()
                _certificateState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _certificateState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun downloadCertificate(applicationId: String,folderToSaveFile: File){
        viewModelScope.launch {
            _certificateDownloadState.value = Lce.Loading
            try {
              val file =  profileRepository.downloadCertificate(applicationId, folderToSaveFile)
                _certificateDownloadState.postValue(Lce.Content(file))
            }catch (e: Exception){
             _certificateDownloadState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
}