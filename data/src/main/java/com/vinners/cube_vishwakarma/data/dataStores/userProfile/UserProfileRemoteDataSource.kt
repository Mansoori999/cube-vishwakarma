package com.vinners.cube_vishwakarma.data.dataStores.userProfile

import com.vinners.cube_vishwakarma.data.models.Documents
import com.vinners.cube_vishwakarma.data.models.auth.LoginResponse
import com.vinners.cube_vishwakarma.data.models.bank.Bank
import com.vinners.cube_vishwakarma.data.models.bank.BankDetails
import com.vinners.cube_vishwakarma.data.models.certificate.Certificate
import com.vinners.cube_vishwakarma.data.models.jobHistory.JobHistory
import com.vinners.cube_vishwakarma.data.models.profile.*
import java.io.File
import com.vinners.cube_vishwakarma.data.models.refer.ReferResponse

interface UserProfileRemoteDataSource {


    suspend fun updateProfileData(profileRequest: UpdateProfileRequest)

    suspend fun getProfile(): UserProfile

    suspend fun getBankDetails(ifsc: String): BankDetails

    suspend fun uploadBankDetails(bankKey: String?,bank: Bank,imageUrl: String?,imageUrl_2: String?)

    suspend fun updateAdharAndPan(bankKey: String?,bank: Bank,imageUrl: String?,imageUrl_2: String?)

    suspend fun getDocuments(): List<Documents>

    suspend fun getJobHistoryList(pageNo: Int): List<JobHistory>

    suspend fun getCertficates(): List<Certificate>

    suspend fun getProfileDetails(): ProfileDetails

    suspend fun updateProfilePic(imageUrl: String?)

    suspend fun downloadCertificate(applicationId: String,folderToSaveFile: File): File

    suspend fun getShortReferralLink(
         fullReferUrl: String
    ): ReferResponse

    suspend fun getAppVersion(appVersion: String): AppVersion

    // for changed password
    suspend fun changedUserPassword(newpassword: String): String

    // refresh profile

    suspend fun refreshProfileData(): LoginResponse

}