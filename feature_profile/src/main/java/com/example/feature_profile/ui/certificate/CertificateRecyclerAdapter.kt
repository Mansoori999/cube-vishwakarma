package com.example.feature_profile.ui.certificate

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.feature_profile.R
import com.example.feature_profile.ui.documents.ViewDocumentsOnlineActivity
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.core.StorageHelper
import com.vinners.trumanms.core.dowloader.FileDownloader
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.Documents
import com.vinners.trumanms.data.models.certificate.Certificate

interface CertificateClickListener{
    fun onDownloadCertificate(certificate: Certificate,position: Int)
    fun onViewClick(certificate: Certificate,position: Int)
}
class CertificateRecyclerAdapter(
    private val appInfo: AppInfo
) :
    RecyclerView.Adapter<CertificateRecyclerAdapter.CertificateViewHolder>() {
    private var certificateList = listOf<Certificate>()
    lateinit var certificateClickListener: CertificateClickListener
    private var context: Context? = null


   fun updateList(certificateList: List<Certificate>){
       this.certificateList = certificateList
       notifyDataSetChanged()
   }
    fun setListener(certificateClickListener: CertificateClickListener){
        this.certificateClickListener = certificateClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateViewHolder {
        this.context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.certificate_recycler_items, parent, false)
        return CertificateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CertificateViewHolder, position: Int) {
        holder.bindTo(certificateList[position])
    }

    override fun getItemCount(): Int {
           return certificateList.size
    }

    inner class CertificateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val downLoadBtn = itemView.findViewById<Button>(R.id.policy_download_action)
        private val viewBtn = itemView.findViewById<Button>(R.id.policy_view_action)
        private val companyLogo = itemView.findViewById<ImageView>(R.id.companyIcon)
        private val firmName = itemView.findViewById<TextView>(R.id.companyHeading)
        private val jobId = itemView.findViewById<TextView>(R.id.jobId)
        private val jobLocation = itemView.findViewById<TextView>(R.id.jobLocation)
        private val workDuration = itemView.findViewById<TextView>(R.id.workDuration)
        private val dateOfIssue = itemView.findViewById<TextView>(R.id.dateOfIssue)
        private val jobDescrirtion = itemView.findViewById<TextView>(R.id.jobDescription)

        init {
            downLoadBtn.setOnClickListener {
                    certificateClickListener.onDownloadCertificate(certificateList[adapterPosition],adapterPosition)
             /*       val policyFile = StorageHelper.createExternalPublicFile(
                        AppConstants.PUBLIC_FILE_FOLDER,
                        "truman_certificate_" + adapterPosition,
                        ".pdf"
                    )
                    if (certificateList[adapterPosition].isDownloaded) {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                val fileUri = FileProvider.getUriForFile(
                                    context!!,
                                    appInfo.packageName + ".provider",
                                    policyFile
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                fileUri
                            } else {
                                Uri.fromFile(policyFile)
                            }
                            intent.setDataAndType(uri, "application/pdf")
                            context?.startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        downLoadBtn.showProgress {
                            buttonText ="Loading"
                            progressColor = R.color.white
                        }
                        downLoadBtn.isEnabled = false
                        FileDownloader()
                            .download(appInfo.getCertificateFullUrl(certificateList[adapterPosition].applicationid.toString()))
                            .saveTo(policyFile)
                            .addSuccessListener {
                                downLoadBtn.hideProgress(
                                    "Downloaded"
                                )
                                downLoadBtn.isEnabled = true
                                // progersBar.setVisibilityGone()
                                certificateList[adapterPosition].isDownloaded = true
                                downLoadBtn.text = "Downloaded"
                                // val imag = context?.resources?.getDrawable(R.drawable.ic_view)
                                // imag?.setBounds(0, 0, 40, 40)
                                //downLoadBtn.setCompoundDrawables(imag, null, null, null)
                            }
                            .addFailureListener {
                                downLoadBtn.hideProgress(
                                    "Download"
                                )
                                downLoadBtn.isEnabled = true
                                Toast.makeText(context,it.localizedMessage,Toast.LENGTH_SHORT).show()
                                // progersBar.setVisibilityGone()
                                // downloadStatus.text = "Not Downloaded"
                            }
                            .start()
                    }
*/

            }
            viewBtn.setOnClickListener {

                certificateClickListener.onViewClick(certificateList[adapterPosition],adapterPosition)
              /*  if (certificateList[adapterPosition].filePath.isNullOrEmpty().not()) {
                    val intent = Intent(context, ViewDocumentsOnlineActivity::class.java)
                    intent.putExtra(
                        ViewDocumentsOnlineActivity.URL,
                        certificateList[adapterPosition].filePath
                    )
                    context?.startActivity(intent)
                }
                else
                    Toast.makeText(context,"File not found",Toast.LENGTH_SHORT).show()*/
            }
        }
        fun bindTo(certificate: Certificate) {
            firmName.text = certificate.clientName
            jobLocation.text = "${certificate.city}, India"
            workDuration.text = "Work Duration: ${certificate.workDuration}"
            jobId.text = certificate.certificateNo
            jobDescrirtion.text = certificate.description
            if(certificate.certificateIssueDate.isNullOrEmpty().not()){
                dateOfIssue.text = DateTimeHelper.getFancyDateFromString(certificate.certificateIssueDate)
            }
            if (!certificate.logo.isNullOrBlank())
                companyLogo.load(appInfo.getFullAttachmentUrl(certificate.logo!!))
        }
    }
}