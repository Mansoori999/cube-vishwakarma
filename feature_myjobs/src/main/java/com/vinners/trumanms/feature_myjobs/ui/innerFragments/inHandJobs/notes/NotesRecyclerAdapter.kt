package com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.data.models.notes.Notes
import com.vinners.trumanms.feature_myjobs.R

class NotesRecyclerAdapter : RecyclerView.Adapter<NotesRecyclerAdapter.NotesViewHolder>() {
    private var notesList = listOf<Notes>()

    fun updateList(notesList: List<Notes>){
        this.notesList = notesList.sortedBy {
            it.date
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_recycler_items_layout, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bindTo(notesList[position])
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val notesTitle = itemView.findViewById<TextView>(R.id.notesTitle)
        private val desc = itemView.findViewById<TextView>(R.id.notesDesc)
        private val date = itemView.findViewById<TextView>(R.id.notesDate)

        fun bindTo(notes: Notes) {
            desc.text = notes.notes
            notesTitle.text = notes.userDetail
            if (notes.date.isNullOrEmpty().not())
                date.text = DateTimeHelper.getFancyDateWithTimeFromString(notes.date)
        }
    }
}