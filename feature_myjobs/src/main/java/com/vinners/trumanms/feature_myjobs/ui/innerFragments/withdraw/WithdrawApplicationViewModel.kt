package com.vinners.trumanms.feature_myjobs.ui.innerFragments.withdraw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.data.repository.JobsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WithdrawApplicationViewModelStates {

    class WithDrawingFromJob(val jodId: String) : WithdrawApplicationViewModelStates()

    class WithDrawnFromJob(val jodId: String) : WithdrawApplicationViewModelStates()

    class ErrorWhileWithDrawingFromJob(
        val jodId: String,
        val error: String
    ) : WithdrawApplicationViewModelStates()
}

class WithdrawApplicationViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<WithdrawApplicationViewModelStates> = MutableLiveData()
    val viewState: LiveData<WithdrawApplicationViewModelStates> = _viewState

    fun withdrawFromJob(
        applicationId: String,
        jobId: String,
        withdrawalReason: String
    ) = viewModelScope.launch {
        _viewState.value = WithdrawApplicationViewModelStates.WithDrawingFromJob(jobId)


        try {
            jobsRepository.withdrawApplication(applicationId, withdrawalReason)
            _viewState.value = WithdrawApplicationViewModelStates.WithDrawnFromJob(jobId)
        } catch (e: Exception) {
            _viewState.value =
                WithdrawApplicationViewModelStates.ErrorWhileWithDrawingFromJob(
                    jodId = jobId,
                    error = e.message ?: "Error while withdrawing from Job"
                )
        }
    }
}