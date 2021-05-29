package com.vinners.cube_vishwakarma.ui.documents

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo

import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse



interface  DocumentsDetailsClickListener {

    fun OnDocumentsClick(documentsResponse: DocumentsResponse)
}

class DocumentDetailsRecyclerAdapter() : RecyclerView.Adapter<DocumentDetailsRecyclerAdapter.DocumentDetailsRecyclerHolder>() {
    private var documentsList = listOf<DocumentsResponse>()
    private lateinit var context: Context
    private lateinit var appInfo : AppInfo
    private lateinit var documentsDetailsClickListener: DocumentsDetailsClickListener



    fun updateViewList (documentsList: List<DocumentsResponse>,appInfo: AppInfo){
        this.documentsList = documentsList
        this.appInfo = appInfo
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentDetailsRecyclerHolder {
        this.context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tutorials_list_layout, parent, false)
        return DocumentDetailsRecyclerHolder(view)
    }


    override fun onBindViewHolder(holder: DocumentDetailsRecyclerHolder, position: Int) {
        holder.onBind(documentsList[position])


    }
    override fun getItemCount(): Int {
        return documentsList.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setDocumentsListener(documentsDetailsClickListener: DocumentsDetailsClickListener) {
        this.documentsDetailsClickListener = documentsDetailsClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class DocumentDetailsRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.name)
        private val path = itemView.findViewById<ImageView>(R.id.path)
        private val description = itemView.findViewById<TextView>(R.id.description)

        init {

            itemView.setOnClickListener {
                documentsDetailsClickListener.OnDocumentsClick(documentsList[adapterPosition])
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
            name.text = "${documentsList.name}"
            description.text = documentsList.description




        }
    }



}