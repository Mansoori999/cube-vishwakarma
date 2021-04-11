package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileRemoteDataSource
import com.vinners.cube_vishwakarma.data.models.Documents
import com.vinners.cube_vishwakarma.data.models.bank.Bank
import com.vinners.cube_vishwakarma.data.models.bank.BankDetails
import com.vinners.cube_vishwakarma.data.models.certificate.Certificate
import com.vinners.cube_vishwakarma.data.models.jobHistory.JobHistory
import com.vinners.cube_vishwakarma.data.models.profile.*
import com.vinners.cube_vishwakarma.data.models.refer.ReferResponse
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.IfscService
import com.vinners.cube_vishwakarma.remote.retrofitServices.ProfileService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.*
import javax.inject.Inject

class UserProfileRemoteDataStoreImpl @Inject constructor(
    private val profileService: ProfileService,
    private val ifscService: IfscService
) : UserProfileRemoteDataSource {
    override suspend fun updateProfileData(profileRequest: UpdateProfileRequest) {

    }

    override suspend fun getProfile(): UserProfile {
        return profileService.getProfile().bodyOrThrow().first()
    }

    override suspend fun getBankDetails(ifsc: String): BankDetails {
        return ifscService.getBankDetails(ifsc).bodyOrThrow()
    }

    override suspend fun updateProfilePic(imageUrl: String?) {
        var imagePart: MultipartBody.Part? = null
        if (imageUrl.isNullOrEmpty().not()) {
            val imageFile = File(imageUrl)
            val requestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData("file", imageFile.name, requestBody)
        }
        profileService.updateProfilePic(imagePart).bodyOrThrow()
    }

    override suspend fun uploadBankDetails(
        bankKey: String?,
        bank: Bank,
        imageUrl: String?,
        imageUrl_2: String?
    ) {
        var imageFilePart: MultipartBody.Part? = null
        var imageFilePart_2: MultipartBody.Part? = null
        if (imageUrl.isNullOrEmpty().not()) {
            val checkqueFile = File(imageUrl!!)
            val checkqueRequestFile =
                checkqueFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            if (bankKey.equals("bank"))
                imageFilePart = MultipartBody.Part.createFormData(
                    "check",
                    checkqueFile.name,
                    checkqueRequestFile
                )
            else if (bankKey.equals("pan"))
                imageFilePart = MultipartBody.Part.createFormData(
                    "pan",
                    checkqueFile.name,
                    checkqueRequestFile
                )
            else if (imageUrl.isNullOrEmpty().not() && imageUrl_2.isNullOrEmpty().not()) {
                val imageFile = File(imageUrl)
                val imageFileRequest =
                    imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                imageFilePart = MultipartBody.Part.createFormData(
                    "adhaarFront",
                    imageFile.name,
                    imageFileRequest
                )
                val imageFile_2 = File(imageUrl_2)
                val imageFileRequest_2 =
                    imageFile_2.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                imageFilePart_2 = MultipartBody.Part.createFormData(
                    "adhaarBack",
                    imageFile_2.name,
                    imageFileRequest_2
                )
            }
        }
        profileService.updloadBankDetails(bankKey, bank, imageFilePart, imageFilePart_2)
    }

    override suspend fun updateAdharAndPan(
        bankKey: String?,
        bank: Bank,
        imageUrl: String?,
        imageUrl_2: String?
    ) {
        var imageFilePart: MultipartBody.Part? = null
        var imageFilePart_2: MultipartBody.Part? = null
        if (bankKey.equals("pan") && imageUrl.isNullOrEmpty().not()) {
            val checkqueFile = File(imageUrl!!)
            val checkqueRequestFile =
                checkqueFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageFilePart = MultipartBody.Part.createFormData(
                "pan",
                checkqueFile.name,
                checkqueRequestFile
            )
        }
        if (bankKey.equals("adhar") && imageUrl.isNullOrEmpty().not() && imageUrl_2.isNullOrEmpty()
                .not()
        ) {
            val imageFile = File(imageUrl)
            val imageFileRequest =
                imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageFilePart = MultipartBody.Part.createFormData(
                "adhaarFront",
                imageFile.name,
                imageFileRequest
            )
            val imageFile_2 = File(imageUrl_2)
            val imageFileRequest_2 =
                imageFile_2.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imageFilePart_2 = MultipartBody.Part.createFormData(
                "adhaarBack",
                imageFile_2.name,
                imageFileRequest_2
            )
        }
        profileService.updloadBankDetails(bankKey, bank, imageFilePart, imageFilePart_2)
    }

    override suspend fun getDocuments(): List<Documents> {
        return profileService.getDocuments().bodyOrThrow()
    }

    override suspend fun getJobHistoryList(pageNo: Int): List<JobHistory> {
        return profileService.getJobHistory(pageNo).bodyOrThrow()
    }

    override suspend fun getCertficates(): List<Certificate> {
        return profileService.getCertificates().bodyOrThrow()
    }

    override suspend fun getProfileDetails(): ProfileDetails {
        return profileService.getProfileDetails().bodyOrThrow().first()
    }

    override suspend fun getShortReferralLink(fullReferUrl: String): ReferResponse {
        return profileService.getShortReferralLink(fullReferUrl = fullReferUrl).bodyOrThrow()
    }

    override suspend fun downloadCertificate(applicationId: String, folderToSaveFile: File): File {
        val requestBody = profileService.downloadCertificate(applicationId).bodyOrThrow()
        val certificateFile = saveResume(applicationId, folderToSaveFile, requestBody)
        return certificateFile
    }

    private fun saveResume(
        resumeRemoteEndPoint: String,
        folderToResumeSaveIn: File,
        body: ResponseBody
    ): File {
        // val fileName = resumeRemoteEndPoint.substring(resumeRemoteEndPoint.lastIndexOf("/") + 1)
        val resumeFile = folderToResumeSaveIn

        try {

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)
                inputStream = body.byteStream()
                outputStream = FileOutputStream(resumeFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                }

                outputStream.flush()

            } catch (e: IOException) {
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
        }
        return resumeFile
    }

    override suspend fun getAppVersion(appVersion: String): AppVersion {
        return profileService.getAppVersion(appVersion).bodyOrThrow().first()
    }
}