package com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.jobs.Application
import com.vinners.trumanms.data.models.notes.Notes
import com.vinners.trumanms.data.models.notes.NotesRequest
import com.vinners.trumanms.data.repository.JobsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface InHandJobsEvents {
    val inHandJobsState: LiveData<Lce<List<Application>>>

    val abandonTaskState: LiveData<Lse>

    val statusTaskState: LiveData<Lse>

    val notesState: LiveData<Lce<List<Notes>>>

    val submitNotesState: LiveData<Lse>
}

class InHandJobsViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
) : ViewModel(), InHandJobsEvents {

    private val _inHandJobsState = MutableLiveData<Lce<List<Application>>>()
    override val inHandJobsState: LiveData<Lce<List<Application>>> = _inHandJobsState

    private val _abandonTaskState = MutableLiveData<Lse>()
    override val abandonTaskState: LiveData<Lse> = _abandonTaskState

    private val _statusTaskState = MutableLiveData<Lse>()
    override val statusTaskState: LiveData<Lse> = _statusTaskState

    private val _notesState = MutableLiveData<Lce<List<Notes>>>()
    override val notesState: LiveData<Lce<List<Notes>>> = _notesState

    private val _submitNotesState = MutableLiveData<Lse>()
    override val submitNotesState: LiveData<Lse> = _submitNotesState

    fun getInHandJobsList() {
        viewModelScope.launch {
            _inHandJobsState.value = Lce.Loading
            try {
                val response = jobsRepository.getInHandJobs()
                _inHandJobsState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _inHandJobsState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun abandonTask(applicationId: String, type: String, reason: String) {
        viewModelScope.launch {
            _abandonTaskState.value = Lse.Loading
            try {
                jobsRepository.abandonTask(applicationId, type, reason)
                _abandonTaskState.postValue(Lse.Success)
            } catch (e: Exception) {
                _abandonTaskState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun updateStatus(applicationId: String, type: String, status: String) {
        viewModelScope.launch {
            _statusTaskState.value = Lse.Loading
            try {
                jobsRepository.updateStatusTask(applicationId, type, status)
                _statusTaskState.postValue(Lse.Success)
            } catch (e: Exception) {
                _statusTaskState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun getNotes(applicationId: String) {
        viewModelScope.launch {
            _notesState.value = Lce.Loading
            try {
                val response = jobsRepository.getNotes(applicationId)
                _notesState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _notesState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun submitNotes(notesRequest: NotesRequest) {
        viewModelScope.launch {
            _submitNotesState.value = Lse.Loading
            try {
                jobsRepository.submitNotes(notesRequest)
                _submitNotesState.postValue(Lse.Success)
            } catch (e: Exception) {
                _submitNotesState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }
}