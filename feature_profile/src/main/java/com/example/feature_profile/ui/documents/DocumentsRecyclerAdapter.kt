package com.example.feature_profile.ui.documents

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_profile.R
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.StorageHelper
import com.vinners.trumanms.core.dowloader.FileDownloader
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.Documents

class DocumentsRecyclerAdapter(
    private val appInfo: AppInfo
) :
    RecyclerView.Adapter<DocumentsRecyclerAdapter.DocumentsViewHolder>() {
    private var policyList = listOf<Documents>()
    private var context: Context? = null

    fun setDocumentsList(policyList: List<Documents>) {
        this.policyList = policyList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.documents_recycler_items, parent, false)
        return DocumentsViewHolder((view))
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        holder.bindTo(policyList[position])
    }

    override fun getItemCount(): Int {
        return policyList.size
    }

    inner class DocumentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val downLoadBtn = itemView.findViewById<Button>(R.id.policy_download_action)
        private val viewBtn = itemView.findViewById<Button>(R.id.policy_view_action)
        private val policyTitle = itemView.findViewById<TextView>(R.id.policy_title)
        private val category = itemView.findViewById<TextView>(R.id.policy_category)
        private val downloadStatus = itemView.findViewById<TextView>(R.id.policy_download_status)
        private val progersBar = itemView.findViewById<ProgressBar>(R.id.policy_progress_bar)

        init {
            downLoadBtn.setOnClickListener {
                if (policyList[adapterPosition].filePath.isNullOrEmpty().not()) {
                    val policyFile = StorageHelper.accessExternalPublicFile(context,
                        "truman_certificate_" + adapterPosition+".pdf"
                    )
                    if (policyList[adapterPosition].isDownloaded) {
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
                        progersBar.setVisibilityVisible()
                        downloadStatus.text = "Downloading..."
                        FileDownloader()
                            .download(appInfo.getFullAttachmentUrl(policyList[adapterPosition].filePath!!))
                            .saveTo(policyFile)
                            .addSuccessListener {
                                progersBar.setVisibilityGone()
                                policyList[adapterPosition].isDownloaded = true
                                downloadStatus.text = "Downloaded"
                                downLoadBtn.text = "View"
                                // val imag = context?.resources?.getDrawable(R.drawable.ic_view)
                                // imag?.setBounds(0, 0, 40, 40)
                                //downLoadBtn.setCompoundDrawables(imag, null, null, null)
                            }
                            .addFailureListener {
                                progersBar.setVisibilityGone()
                                downloadStatus.text = "Not Downloaded"
                            }
                            .start()
                    }
                }else
                    Toast.makeText(context,"File not found", Toast.LENGTH_SHORT).show()
            }
            viewBtn.setOnClickListener {
                if (policyList[adapterPosition].filePath.isNullOrEmpty().not()){
                 val intent = Intent(context, ViewDocumentsOnlineActivity::class.java)
                  intent.putExtra(ViewDocumentsOnlineActivity.URL, policyList[adapterPosition].filePath)
                  context?.startActivity(intent)
                  }else
                    Toast.makeText(context,"File not found", Toast.LENGTH_SHORT).show()
            }
        }

        fun bindTo(documents: Documents) {
            policyTitle.text = documents.name
            category.text = documents.categoryName
        }
    }
}