package com.vinners.cube_vishwakarma.data.repository

import android.content.SharedPreferences
import com.vinners.core.logger.Logger
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileLocalDataStore
import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileRemoteDataSource
import com.vinners.cube_vishwakarma.data.models.Documents
import com.vinners.cube_vishwakarma.data.models.auth.LoginResponse
import com.vinners.cube_vishwakarma.data.models.auth.RegisterRequest
import com.vinners.cube_vishwakarma.data.models.bank.Bank
import com.vinners.cube_vishwakarma.data.models.bank.BankDetails
import com.vinners.cube_vishwakarma.data.models.certificate.Certificate
import com.vinners.cube_vishwakarma.data.models.jobHistory.JobHistory
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.models.profile.ProfileDetails
import com.vinners.cube_vishwakarma.data.models.profile.UserProfile
import java.io.File
import com.vinners.cube_vishwakarma.data.models.refer.ReferResponse
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val logger: Logger,
    private val appInfo: AppInfo,
    private val userSessionManager: UserSessionManager,
    @Named("session_dependent_pref") private val profilePrefs: SharedPreferences,
    private val userProfileLocalDataStore: UserProfileLocalDataStore,
    private val userProfileRemoteDataSource: UserProfileRemoteDataSource,

) {

    /**
     * Return A [Flow]
     */
    fun watchWalletInfo() = userProfileLocalDataStore.getProfile()

    suspend fun getProfile(): UserProfile{
        return userProfileRemoteDataSource.getProfile()
    }

    suspend fun getCachedProfile(): Profile?{
      return  userProfileLocalDataStore.getCachedProfile()
    }

    suspend fun updateProfilePic(imageUrl: String?){
        userProfileRemoteDataSource.updateProfilePic(imageUrl)
//        userProfileLocalDataStore.updateProfilePic(imageUrl)
//        userProfileLocalDataStore.markProfilePictureAsUpdated()
    }

    suspend fun getBankDetails(ifsc: String): BankDetails{
        return userProfileRemoteDataSource.getBankDetails(ifsc)
    }

    suspend fun updateBankDetails(bankKey: String?,bank: Bank,imageFile: String?,imageFile_2: String?){
        userProfileRemoteDataSource.uploadBankDetails(bankKey, bank, imageFile,imageFile_2)
        userProfileLocalDataStore.updateBankData(
            bank.accountNo,
            bank.bankName,
            bank.nameOnBank,
            bank.bankBranch,
            bank.ifsc
        )
    }

    suspend fun updateAdharAndPan(bankKey: String?,bank: Bank,imageFile: String?,imageFile_2: String?){
        userProfileRemoteDataSource.updateAdharAndPan(bankKey,bank,imageFile,imageFile_2)
//        userProfileLocalDataStore.updateAadharNo(bank.adharNo)
    }


    suspend fun generateUserReferralLink(): String {

        val userMobileNo = userSessionManager.mobile
            ?: throw IllegalStateException("ProfileRepository: generateUserReferralLink() : User does not have mobile number")
        val referFullUrl = "https://play.google.com/store/apps/details?id=com.vinners.cube_vishwakarma&referrer=utm_source%3D${userMobileNo}%26utm_medium%3Dplay_store%26utm_term%3Dreferral%26utm_campaign%3Dreferral_scheme"

        return  try {
            val generatedUserReferralLink = userProfileRemoteDataSource.getShortReferralLink(referFullUrl)
            generatedUserReferralLink.url?.shortLink ?: referFullUrl
        } catch (e : Exception){
            referFullUrl
        }
    }

    suspend fun getDocuments(): List<Documents> {
        return userProfileRemoteDataSource.getDocuments()
    }

    suspend fun getJobHistory(pageNo: Int): List<JobHistory>{
        return userProfileRemoteDataSource.getJobHistoryList(pageNo)
    }

    suspend fun getCertificate(): List<Certificate>{
        return userProfileRemoteDataSource.getCertficates()
    }

    suspend fun getProfileDetails(): ProfileDetails{
        return userProfileRemoteDataSource.getProfileDetails()
    }

    suspend fun downloadCertificate(applicationId: String, folderToSaveFile: File): File {
        return userProfileRemoteDataSource.downloadCertificate(applicationId, folderToSaveFile)
    }

    suspend fun logout() {
        userProfileLocalDataStore.logout()
    }

    suspend fun changedUserPassword(newpassword: String): String{
        return userProfileRemoteDataSource.changedUserPassword(newpassword)
    }

    suspend fun refreshProfileData(): LoginResponse {
        val loginResponse = userProfileRemoteDataSource.refreshProfileData()
        userProfileLocalDataStore.updateProfile(
            RegisterRequest(
                name = loginResponse.name,
                nickname = loginResponse.nickname,
                mobile = loginResponse.mobile,
                email = loginResponse.email,
                alternatemobile = loginResponse.alternatemobile,
                userType = loginResponse.userType,
                logintype = loginResponse.logintype,
                dob = loginResponse.dob,
                gender = loginResponse.gender,
                loginid = loginResponse.loginid,
                city = loginResponse.city,
                pincode = loginResponse.pincode,
                pic = loginResponse.pic,
                education = loginResponse.education,
                aadhaarno = loginResponse.aadhaarno,
                aadhaarpic = loginResponse.aadhaarpic,
                pan = loginResponse.pan,
                panpic = loginResponse.panpic,
                dlnumber = loginResponse.dlnumber,
                dlpic = loginResponse.dlpic,
                pfnumber = loginResponse.pfnumber,
                designation = loginResponse.designation,
                esicnumber = loginResponse.esicnumber,
                voterid = loginResponse.voterid,
                voteridpic = loginResponse.voteridpic,
                bankname = loginResponse.bankname,
                nameonbank = loginResponse.nameonbank,
                bankbranch = loginResponse.bankbranch,
                ifsc = loginResponse.ifsc,
                accountno = loginResponse.accountno,
                emergencymobile = loginResponse.emergencymobile,
                state = loginResponse.state,
                emergencyname = loginResponse.emergencyname,
                emergencyrelation = loginResponse.emergencyrelation,
                referencename = loginResponse.referencename,

                referencemobile = loginResponse.referencemobile,
                referencerelation = loginResponse.referencerelation,
                address = loginResponse.address,
                createdby = loginResponse.createdby,
                createdon = loginResponse.createdon,
                authToken = loginResponse.authToken,
                employment = loginResponse.employment,
                managerid = loginResponse.managerid,
                deviceid = loginResponse.deviceid,
                doj = loginResponse.doj,
                dol = loginResponse.dol,
                empcode = loginResponse.empcode
            )
        )
        return loginResponse
//        return userProfileRemoteDataSource.refreshProfileData()
    }
    companion object {
        private const val LAST_MONEY_OPERATION_TIME = "last_wallet_operation"
        private const val FIVE_SECONDS = 5000
    }


}