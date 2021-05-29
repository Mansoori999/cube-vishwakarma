package com.vinners.cube_vishwakarma.ui.documents

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintsClickListener
import javax.inject.Inject

interface  DocumentsClickListener {

    fun OnDocumentsClick(documentsResponse: DocumentsResponse)
}

class DocumentsRecyclerAdapter() : RecyclerView.Adapter<DocumentsRecyclerAdapter.DocumentsRecyclerHolder>() {
    private var documentsList = listOf<DocumentsResponse>()
    private lateinit var context: Context
    private lateinit var appInfo : AppInfo
    private lateinit var documentsClickListener: DocumentsClickListener


    fun updateViewList (documentsList: List<DocumentsResponse>,appInfo: AppInfo){
        val itemList = documentsList.distinctBy { it.catid }
        this.documentsList = documentsList
        this.appInfo = appInfo
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsRecyclerHolder {
        this.context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.documents_list_layout, parent, false)
        return DocumentsRecyclerHolder(view)
    }


    override fun onBindViewHolder(holder: DocumentsRecyclerHolder, position: Int) {
        holder.onBind(documentsList[position])


    }
    override fun getItemCount(): Int {
        return documentsList.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setDocumentsListener(documentsClickListener: DocumentsClickListener) {
        this.documentsClickListener = documentsClickListener
    }
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class DocumentsRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val category = itemView.findViewById<TextView>(R.id.category)
        private val path = itemView.findViewById<ImageView>(R.id.path)
//        private val description = itemView.findViewById<TextView>(R.id.description)

        init {

            itemView.setOnClickListener {
                documentsClickListener.OnDocumentsClick(documentsList[adapterPosition])
            }
        }

        fun onBind(documentsList: DocumentsResponse){

//            val sanitizer: UrlQuerySanitizer = UrlQuerySanitizer()
//            sanitizer.setAllowUnregisteredParamaters(true)
//            sanitizer.parseUrl(documentsList.path)
//            val urlpath  = sanitizer.getValue("v")

            val pathurl = documentsList.path
            val link = pathurl?.substringAfter(".")
            if (link.equals("ppt") || link.equals("pptx")){
                path.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.default_ppt, null))
            }else if (link.equals("pdf")){
                path.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.default_pdf, null))
            }else{
                path.load(appInfo.getFullAttachmentUrl(documentsList.path!!))
            }
            val image = appInfo.getFullAttachmentUrl(documentsList.path!!)
            Log.d("jhug",image)
            category.text = "${documentsList.category}"
//            description.text = documentsList.description




        }
    }



}