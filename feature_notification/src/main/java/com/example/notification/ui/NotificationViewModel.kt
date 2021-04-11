package com.example.notification.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.notification.Notification
import com.vinners.trumanms.data.repository.NotificationRepositry
import kotlinx.coroutines.launch
import javax.inject.Inject

interface NotificationEvents {
    val notificationState: LiveData<Lce<List<Notification>>>

    val readNotificationState: LiveData<Lse>
}

class NotificationViewModel @Inject constructor(
    private val notificationRepositry: NotificationRepositry
) : ViewModel(), NotificationEvents {

    private val _notificationState = MutableLiveData<Lce<List<Notification>>>()
    override val notificationState: LiveData<Lce<List<Notification>>> = _notificationState

    private val _readNotificationState = MutableLiveData<Lse>()
    override val readNotificationState: LiveData<Lse> = _readNotificationState

    fun getNotification(offset: String) {
        viewModelScope.launch {
            _notificationState.value = Lce.Loading
            try {
                val response = notificationRepositry.getNotification(offset)
                _notificationState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _notificationState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun readNotification(type: String, notificationId: String){
        viewModelScope.launch {
            _readNotificationState.value = Lse.Loading
            try {
                notificationRepositry.readNotification(type, notificationId)
                _readNotificationState.postValue(Lse.Success)
            }catch (e: Exception){
                _readNotificationState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }
}