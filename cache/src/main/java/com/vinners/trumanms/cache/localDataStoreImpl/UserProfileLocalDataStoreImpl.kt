package com.vinners.trumanms.cache.localDataStoreImpl


import com.vinners.trumanms.cache.LocalDatabase
import com.vinners.trumanms.cache.dao.ProfileDao
import com.vinners.trumanms.cache.entities.CachedProfile
import com.vinners.trumanms.cache.entityMappers.ProfileEntityMapper
import com.vinners.trumanms.data.dataStores.userProfile.UserProfileLocalDataStore
import com.vinners.trumanms.data.models.auth.RegisterRequest
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class UserProfileLocalDataStoreImpl @Inject constructor(
    private val profileDao: ProfileDao,
    private val profileEntityMapper: ProfileEntityMapper,
    private val localDatabase: LocalDatabase,
    private val userSessionManager: UserSessionManager
) : UserProfileLocalDataStore {

    override fun getProfile(): Observable<Profile> {
        return profileDao
            .profile
            .map { profileList ->
                profileList.firstOrNull()
            }.map { t: CachedProfile ->
                profileEntityMapper.mapFromCached(t)
            }
    }

    override suspend fun getCachedProfile(): Profile? {
        val cachedProfile = profileDao.getCachedProfile() ?: return null
        return profileEntityMapper.mapFromCached(cachedProfile)
    }

    override suspend fun markProfilePictureAsUpdated() {
        checkNotNull(getCachedProfile()) { "UserProfileLocalDataStoreImpl: Not Profile Present In Local DB, Hence Cannot Update Profile Picture" }
        profileDao.markProfilePictureAsUpdated()
    }

    override suspend fun updateProfilePic(profilePic: String?) {
        checkNotNull(getCachedProfile()) { "UserProfileLocalDataStoreImpl: Not Profile Present In Local DB, Hence Cannot Update Profile Picture" }
        profileDao.updateProfilePic(profilePic)
    }

    override suspend fun updateAadharNo(aadharNo: String?) {
        checkNotNull(getCachedProfile()) { "UserProfileLocalDataStoreImpl: Not Profile Present In Local DB, Hence Cannot Update Aadhar" }
        profileDao.updateAdhar(aadharNo)
    }

    override suspend fun updateBankData(
        accountNo: String?,
        bankName: String?,
        nameOnBank: String?,
        mobileOnBank: String?,
        ifsc: String?
    ) {
        checkNotNull(getCachedProfile()) { "UserProfileLocalDataStoreImpl: Not Profile Present In Local DB, Hence Cannot Update Bank Details" }
        profileDao.updateBankData(accountNo, bankName, nameOnBank, mobileOnBank, ifsc)
    }

    override suspend fun updateProfile(type: RegisterRequest) {
        val cachedProfile = profileDao.getCachedProfile()
        if (cachedProfile == null) {
            //There's no profile Data in local database , so we'll insert new Data
            profileDao.insert(
                CachedProfile(
                    firstName = type.firstName,
                    lastName = type.lastName,
                    mobile = type.mobile,
                    email = type.email,
                    agencyType = type.agencyType,
                    userType = type.userType,
                    whatsAppMobileNumber = type.whatsAppMobileNumber,
                    dob = type.dob,
                    gender = type.gender,
                    district = type.district,
                    cityId = type.cityId,
                    pinCode = type.pinCode,
                    pinCodeId = type.pinCodeId,
                    education = type.education,
                    experience = type.experience,
                    languages = type.languages,
                    twoWheeler = type.twoWheeler,
                    workCategory = type.workCategory,
                    teamSize = type.teamSize,
                    websitePage = type.websitePage,
                    facebookPage = type.facebookPage,
                    designationName = type.designation,
                    agencyName = type.agencyName,
                    yearInBusiness = type.yearsInBusiness,
                    bankName = type.bankName,
                    nameOnBank = type.nameOnBank,
                    mobileOnBank = type.mobileOnBank,
                    ifsc = type.ifsc,
                    accountNo = type.accountNo,
                    stateId = type.stateId,
                    state = type.state,
                    districtId = type.districtId,
                    profilePic = type.profilePic,
                    aadharNo = type.adhar
                )
            )
        } else {

            val profile = cachedProfile.copy(
                firstName = type.firstName,
                lastName = type.lastName,
                mobile = type.mobile,
                email = type.email,
                agencyType = type.agencyType,
                userType = type.userType,
                whatsAppMobileNumber = type.whatsAppMobileNumber,
                dob = type.dob,
                gender = type.gender,
                district = type.district,
                cityId = type.cityId,
                pinCode = type.pinCode,
                pinCodeId = type.pinCodeId,
                education = type.education,
                experience = type.experience,
                languages = type.languages,
                twoWheeler = type.twoWheeler,
                workCategory = type.workCategory,
                teamSize = type.teamSize,
                websitePage = type.websitePage,
                facebookPage = type.facebookPage,
                designationName = type.designation,
                agencyName = type.agencyName,
                yearInBusiness = type.yearsInBusiness,
                bankName = type.bankName,
                nameOnBank = type.nameOnBank,
                mobileOnBank = type.mobileOnBank,
                ifsc = type.ifsc,
                accountNo = type.accountNo,
                stateId = type.stateId,
                state = type.state,
                districtId = type.districtId,
                profilePic = type.profilePic,
                aadharNo = type.adhar
            )
            profile.id = cachedProfile.id
            profile.profilePicUpdated = cachedProfile.profilePicUpdated
            //Updating old Data with new data
            profileDao.update(profile)
        }
    }

    override suspend fun logout() {
        userSessionManager.logOut()
        localDatabase.clearAllTables()
    }
}