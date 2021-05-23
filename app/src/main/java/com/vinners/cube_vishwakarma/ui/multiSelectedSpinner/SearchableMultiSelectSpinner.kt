package com.devstune.searchablemultiselectspinner

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData

class SearchableMultiSelectSpinner {
    companion object {
        fun show(
            context: Context,
            title: String,
            doneButtonText:String,
            cancelButtonText:String,
            searchableItems: MutableList<RegionalOfficeFilterData>,
            selectionCompleteListener: SelectionCompleteListener
        ) {
            val alertDialog = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val convertView = inflater.inflate(R.layout.searchable_list_layout, null)

            alertDialog.setView(convertView)
            alertDialog.setTitle(title)

            val searchView = convertView.findViewById<SearchView>(R.id.searchView)
            val recyclerView =
                convertView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
            val mLayoutManager = LinearLayoutManager(context)
            val adapter =
                SearchableAdapter(
                    context,
                    searchableItems,
                    searchableItems,
                    object : SearchableAdapter.ItemClickListener {
                        override fun onItemClicked(
                            item: RegionalOfficeFilterData,
                            position: Int,
                            b: Boolean
                        ) {
                            for(i in 0 until searchableItems.size) {
                                if (searchableItems[i].id == item.id) {
                                    searchableItems[i].isSelected = b
                                    break
                                }
                            }
//                            for (i in searchableItems.indices) {
//                                if (searchableItems[i].id == item.id) {
//                                    searchableItems[i].isSelected = b
//                                    break
//                                }
//                            }
                        }

                    },false)
            recyclerView.itemAnimator = null
            recyclerView.layoutManager = mLayoutManager
            recyclerView.adapter = adapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // do something on text submit
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // do something when text changes
                    adapter.filter.filter(newText)
                    return false
                }
            })

            alertDialog.setNegativeButton(cancelButtonText){ dialogInterface, i ->
//
//                val resultList=ArrayList<RegionalOfficeFilterData>()
//                for (i in 0 until searchableItems.size) {
//                    if (searchableItems[i].isSelected) {
//                        resultList.clear()
//                    }
//                }
//                selectionCompleteListener.onCompleteSelection(resultList)
//
//                adapter.notifyDataSetChanged()
                dialogInterface.cancel()

            }
            alertDialog.setPositiveButton(doneButtonText) { dialogInterface, i ->
                dialogInterface.dismiss()
                val resultList=ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until searchableItems.size) {
                    if (searchableItems[i].isSelected) {
                        resultList.add(searchableItems[i])
                    }
                }

                selectionCompleteListener.onCompleteSelection(resultList)
            }

            alertDialog.show()
        }
    }


}