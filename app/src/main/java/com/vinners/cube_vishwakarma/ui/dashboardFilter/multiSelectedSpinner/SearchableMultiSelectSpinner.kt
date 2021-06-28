package com.devstune.searchablemultiselectspinner

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData
import java.util.stream.Collectors

class SearchableMultiSelectSpinner {

    companion object {
        fun show(
            context: Context,
            title: String,
            doneButtonText:String,
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
                        }

                    },false)

            adapter.notifyDataSetChanged()


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

            alertDialog.setPositiveButton(doneButtonText) { dialogInterface, i ->
                dialogInterface.dismiss()
                val resultList =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until searchableItems.size) {
                    if (searchableItems[i].isSelected) {
                        resultList.add(searchableItems[i])
                    }
                }
                selectionCompleteListener.onCompleteSelection(resultList)
            }
            alertDialog.setNegativeButton("clear"){ DialogInterface, i: Int ->
                val resultList =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until searchableItems.size) {
                    if (searchableItems[i].isSelected) {
                        searchableItems[i].isSelected = false
                        resultList.remove(searchableItems[i])
                    }
                }
                selectionCompleteListener.onCompleteSelection(resultList)
            }

            alertDialog.show()
        }



        fun reCreateAfterReset(
                context: Context,
                title: String,
                doneButtonText:String,
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

            val isChecked :Boolean = false

            val adapter =
                    SearchableAdapter(
                            context,
                            searchableItems,
                            searchableItems,
                            object : SearchableAdapter.ItemClickListener {
                                override fun onItemClicked(
                                        item: RegionalOfficeFilterData,
                                        position: Int,
                                        b:Boolean
                                ) {
//                                    for(i in 0 until searchableItems.size) {
//                                        if (searchableItems[i].id == item.id) {
//                                            searchableItems[i].isSelected = false
//
//                                            break
//                                        }
//                                    }
                                    val resultList =ArrayList<RegionalOfficeFilterData>()
                                    for (i in 0 until searchableItems.size) {
                                        if (searchableItems[i].isSelected) {
                                            searchableItems[i].isSelected = false
                                            resultList.remove(searchableItems[i])
                                        }
                                    }
                                    selectionCompleteListener.onCompleteSelection(resultList)

                                }

                            },false)

            adapter.notifyDataSetChanged()
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

            fun resetAll(){
                val resultList =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until searchableItems.size) {
                    if (searchableItems[i].isSelected) {
                        searchableItems[i].isSelected = false
                        resultList.remove(searchableItems[i])
                    }
                }
                selectionCompleteListener.onCompleteSelection(resultList)
            }

            alertDialog.setPositiveButton(doneButtonText) { dialogInterface, i ->
                dialogInterface.dismiss()
                val resultList =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until searchableItems.size) {
                    if (searchableItems[i].isSelected) {
                        resultList.add(searchableItems[i])
                    }
                }
                selectionCompleteListener.onCompleteSelection(resultList)
            }

            alertDialog.setNegativeButton("clear"){ DialogInterface, i: Int ->
                val resultList =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until searchableItems.size) {
                    if (searchableItems[i].isSelected) {
                        searchableItems[i].isSelected = false
                        resultList.remove(searchableItems[i])
                    }
                }
                selectionCompleteListener.onCompleteSelection(resultList)
            }

        }
    }



}