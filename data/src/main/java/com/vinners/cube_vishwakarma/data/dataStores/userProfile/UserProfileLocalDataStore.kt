package com.vinners.cube_vishwakarma.data.dataStores.userProfile


import com.vinners.cube_vishwakarma.data.models.auth.RegisterRequest
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow


interface UserProfileLocalDataStore {

    /**
     * Returns An Instance of [Flow] that emit profile object object when you subscribe to it
     * or profile in local changes
     */
    fun getProfile(): Observable<Profile>

    suspend fun getCachedProfile(): Profile?

    suspend fun markProfilePictureAsUpdated()

    suspend fun updateProfilePic(profilePic: String?)

    suspend fun updateAadharNo(aadharNo: String?)

    suspend fun updateProfile(request: RegisterRequest)

    suspend fun updateBankData( accountNo: String?,
                                bankName: String?,
                                nameOnBank: String?,
                                mobileOnBank: String?,
                                ifsc: String?)

    suspend fun logout()

}