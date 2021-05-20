package com.vinners.cube_vishwakarma.cache.localDataStoreImpl


import com.vinners.cube_vishwakarma.cache.LocalDatabase
import com.vinners.cube_vishwakarma.cache.dao.ProfileDao
import com.vinners.cube_vishwakarma.cache.entities.CachedProfile
import com.vinners.cube_vishwakarma.cache.entityMappers.ProfileEntityMapper
import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileLocalDataStore
import com.vinners.cube_vishwakarma.data.models.auth.RegisterRequest
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
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

//    override suspend fun markProfilePictureAsUpdated() {
//        checkNotNull(getCachedProfile()) { "UserProfileLocalDataStoreImpl: Not Profile Present In Local DB, Hence Cannot Update Profile Picture" }
//        profileDao.markProfilePictureAsUpdated()
//    }
//
//    override suspend fun updateProfilePic(profilePic: String?) {
//        checkNotNull(getCachedProfile()) { "UserProfileLocalDataStoreImpl: Not Profile Present In Local DB, Hence Cannot Update Profile Picture" }
//        profileDao.updateProfilePic(profilePic)
//    }
//
//    override suspend fun updateAadharNo(aadharNo: String?) {
//        checkNotNull(getCachedProfile()) { "UserProfileLocalDataStoreImpl: Not Profile Present In Local DB, Hence Cannot Update Aadhar" }
//        profileDao.updateAdhar(aadharNo)
//    }

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
                        name = type.name,
                        nickname = type.nickname,
                        mobile = type.mobile,
                        email = type.email,
                        alternatemobile = type.alternatemobile,
                        logintype = type.logintype,
                        designation=type.designation,
                        userType = type.userType,
                        dob = type.dob,
                        gender = type.gender,
                        loginid = type.loginid,
                        pic = type.pic,
                        city = type.city,
                        pincode = type.pincode,
                        employment = type.employment,
                        education = type.education,
                        managerid = type.managerid,
                        deviceid = type.deviceid,
                        doj = type.doj,
                        dol = type.dol,
                        aadhaarno = type.aadhaarno,
                        aadhaarpic = type.aadhaarpic,
                        pan = type.pan,
                        panpic = type.panpic,
                        dlnumber = type.dlnumber,
                        dlpic = type.dlpic,
                        bankname = type.bankname,
                        nameonbank = type.nameonbank,
                        pfnumber = type.pfnumber,
                        ifsc = type.ifsc,
                        accountno = type.accountno,
                        esicnumber = type.esicnumber,
                        state = type.state,
                        voterid = type.voterid,
                        voteridpic = type.voteridpic,
                        bankbranch = type.bankbranch,
                        emergencymobile = type.emergencymobile,
                        emergencyname = type.emergencyname,
                        emergencyrelation = type.emergencyrelation,
                        referencename = type.referencename,
                        referencemobile = type.referencemobile,
                        referencerelation = type.referencerelation,
                        address = type.address,
                        createdon = type.createdon,
                        createdby = type.createdby


                )
            )
        } else {

            val profile = cachedProfile.copy(
                    name = type.name,
                    nickname = type.nickname,
                    mobile = type.mobile,
                    email = type.email,
                    alternatemobile = type.alternatemobile,
                    logintype = type.logintype,
                    designation=type.designation,
                    userType = type.userType,
                    dob = type.dob,
                    gender = type.gender,
                    loginid = type.loginid,
                    pic = type.pic,
                    city = type.city,
                    pincode = type.pincode,
                    employment = type.employment,
                    education = type.education,
                    managerid = type.managerid,
                    deviceid = type.deviceid,
                    doj = type.doj,
                    dol = type.dol,
                    aadhaarno = type.aadhaarno,
                    aadhaarpic = type.aadhaarpic,
                    pan = type.pan,
                    panpic = type.panpic,
                    dlnumber = type.dlnumber,
                    dlpic = type.dlpic,
                    bankname = type.bankname,
                    nameonbank = type.nameonbank,
                    pfnumber = type.pfnumber,
                    ifsc = type.ifsc,
                    accountno = type.accountno,
                    esicnumber = type.esicnumber,
                    state = type.state,
                    voterid = type.voterid,
                    voteridpic = type.voteridpic,
                    bankbranch = type.bankbranch,
                    emergencymobile = type.emergencymobile,
                    emergencyname = type.emergencyname,
                    emergencyrelation = type.emergencyrelation,
                    referencename = type.referencename,
                    referencemobile = type.referencemobile,
                    referencerelation = type.referencerelation,
                    address = type.address,
                    createdon = type.createdon,
                    createdby = type.createdby
            )
            profile.id = cachedProfile.id
//            profile.pic = cachedProfile.profilePicUpdated
            //Updating old Data with new data
            profileDao.update(profile)
        }
    }

    override suspend fun logout() {
        userSessionManager.logOut()
        localDatabase.clearAllTables()
    }
}