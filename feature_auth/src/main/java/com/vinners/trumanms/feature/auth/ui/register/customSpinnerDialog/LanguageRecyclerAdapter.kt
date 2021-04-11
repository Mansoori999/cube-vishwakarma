package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.vinners.trumanms.data.models.stateAndCity.Languages
import com.vinners.trumanms.feature.auth.R

interface LanguageOnClickListener {
    fun onlanguageClick(languages: Languages)
}

class LanguageRecyclerAdapter : RecyclerView.Adapter<LanguageRecyclerAdapter.LanguageViewHolder>() {

    private var languageList = listOf<Languages>()
    private var languageOnClickListener: LanguageOnClickListener? = null

    fun updateList(languageList: List<Languages>) {
        this.languageList = languageList.sortedBy {
            it.name
        }
        notifyDataSetChanged()
    }

    fun setClickListener(languageOnClickListener: LanguageOnClickListener){
        this.languageOnClickListener = languageOnClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multi_select_recycler_items, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.onBind(languageList[position])
    }

    override fun getItemCount(): Int {
        return languageList.size
    }

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val language = itemView.findViewById<TextView>(R.id.valueEt)
        private val checkbox = itemView.findViewById<CheckBox>(R.id.selectedCheckbox)

        init {
            checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if (isChecked){
                        languageList[adapterPosition].isBoxChecked = true
                    }else{
                        languageList[adapterPosition].isBoxChecked = false
                    }
                    languageOnClickListener?.onlanguageClick(languageList[adapterPosition])
                }
            }
            )
        }
        fun onBind(languages: Languages){
            language.text = languages.name
            if (languages.isBoxChecked)
                checkbox.isChecked = true
            else
                checkbox.isChecked = false
        }
    }
}