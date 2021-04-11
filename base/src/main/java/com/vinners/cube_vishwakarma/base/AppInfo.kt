package com.vinners.cube_vishwakarma.base

data class AppInfo(
    val debugBuild: Boolean,
    val appVersion: String,
    val lastCommit: String,
    val packageName: String,
    val baseApiUrl: String
) {

    fun getFullAttachmentUrl(attachmentEndPoint: String): String {
        return "$baseApiUrl/file/$attachmentEndPoint"
    }
    fun getCertificateFullUrl(attachmentEndPoint: String): String{
        return "$baseApiUrl/api/application/downloadcertficate?applicationid=$attachmentEndPoint"
    }
}