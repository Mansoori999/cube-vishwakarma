package com.vinners.cube_vishwakarma.ui.documents

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.devstune.searchablemultiselectspinner.SearchableAdapter
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.QuickAlertDialog.showInformationDialog
import com.vinners.cube_vishwakarma.core.base.BaseDialogFragment
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.outlets.Outlet
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.databinding.FragmentSelectOutletsDetailsDialogBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData
import mobile.fitbitMerch.ui.masterData.ComplaintSelectionListener
import mobile.fitbitMerch.ui.masterData.OutletSelectionListener
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class SelectOutletsDetailsDialogFragment : BaseDialogFragment<FragmentSelectOutletsDetailsDialogBinding,DocumentsViewModel>(R.layout.fragment_select_outlets_details_dialog),
    OutletSelectionListener {
    companion object{
         var TAG: String = "SelectOutletsDetailsDialogFragment"
        fun newInstance(callBackToCheckActivity: OutletSelectionListener): SelectOutletsDetailsDialogFragment {
            val outletSelectorDialogFragment = SelectOutletsDetailsDialogFragment()
            outletSelectorDialogFragment.setStyle(
                DialogFragment.STYLE_NORMAL,
                R.style.DialogFragmentTheme
            )
            outletSelectorDialogFragment.setOutletSelectedCallback(callBackToCheckActivity)
            return outletSelectorDialogFragment
        }
    }

    var roselectedId = Arrays.asList<Int>()
    var saleselectedId = Arrays.asList<Int>()
    var selectedItemList:String? = ""
    var saleselectedItemList:String? = ""
    var roid:Int = 0
    private lateinit var resestTV: TextView
    var salesArea =  mutableListOf<OutletsList>()
    val regionalOfficeResetData =  mutableListOf<RegionalOfficeFilterData>()

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    private lateinit var outletListener: OutletListener

    private var callbackToComplaintSelectorFragment: OutletSelectionListener? = null

    fun setOutletSelectedCallback(callbackToComplaintSelectorFragment: OutletSelectionListener) {
        this.callbackToComplaintSelectorFragment = callbackToComplaintSelectorFragment
    }



    fun setOutletListener(outletListener: OutletListener) {
        this.outletListener = outletListener
    }


    interface OutletListener {
        fun onOutletClick(outlet: Outlet)
    }

    override val viewModel: DocumentsViewModel by viewModels { viewModelFactory }

    private val selectOutletDetailsRecyclerAdapter: SelectOutletDetailsRecyclerAdapter by lazy {
        SelectOutletDetailsRecyclerAdapter()
            .apply {
                updateViewList(emptyList())
                setOutletListener(this@SelectOutletsDetailsDialogFragment)
            }
    }
    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                selectOutletDetailsRecyclerAdapter.filter.filter(p0)
                return true
            }

        })
        viewBinding.outletRecycler.layoutManager = LinearLayoutManager(context)
        selectOutletDetailsRecyclerAdapter.updateViewList(emptyList())
        viewBinding.outletRecycler.adapter = selectOutletDetailsRecyclerAdapter
    }

    override fun onInitViewModel() {
        viewModel.outletListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()
                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = "Not Outlets Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        selectOutletDetailsRecyclerAdapter.updateViewList(it.content)


                        val regionalOffice = it.content.map {
                            RegionalOfficeFilterData(
                                id = it.roid!!,
                                name = it.regionaloffice!!,
                                isSelected = false
                            ) }.toMutableList()
                        setRegionalOfficeTypeSpinner(regionalOffice)
                        salesArea.addAll(it.content)
                    }

                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityGone()


                }


            }
        })

        viewModel.getOutletData()

        viewModel.outletState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()

                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.progressBar.setVisibilityGone()
                        selectOutletDetailsRecyclerAdapter.updateViewList(emptyList())
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = "Not Outlets Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        selectOutletDetailsRecyclerAdapter.updateViewList(it.content)
                    }
                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityGone()


                }


            }
        })
        viewModel.outletStateWithOR.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()

                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.progressBar.setVisibilityGone()
                        selectOutletDetailsRecyclerAdapter.updateViewList(emptyList())
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = "Not Outlets Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        selectOutletDetailsRecyclerAdapter.updateViewList(it.content)

                    }
                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityGone()
                }


            }
        })



    }
    private fun setRegionalOfficeTypeSpinner(regionalOffice: MutableList<RegionalOfficeFilterData>) {
        regionalOffice.sortBy { it.name }
        val setItems = regionalOffice.distinctBy { it.name }
        regionalOffice.clear()
        regionalOffice.addAll(setItems)

        viewBinding.roSpinner.setOnClickListener {
            val alertDialog = context?.let { it1 -> AlertDialog.Builder(it1) }
            val inflater = LayoutInflater.from(context)
            val convertView = inflater.inflate(R.layout.searchable_list_layout, null)

            alertDialog!!.setView(convertView)
            alertDialog!!.setTitle("Select Regional Office")

            val searchView = convertView.findViewById<SearchView>(R.id.searchView)
            val recyclerView =
                convertView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
            val mLayoutManager = LinearLayoutManager(context)

            val adapter =
                context?.let { it1 ->
                    SearchableAdapter(
                        it1,
                        regionalOffice,
                        regionalOffice,
                        object : SearchableAdapter.ItemClickListener {
                            override fun onItemClicked(
                                item: RegionalOfficeFilterData,
                                position: Int,
                                b: Boolean
                            ) {
                                for (i in 0 until regionalOffice.size) {
                                    if (regionalOffice[i].id == item.id) {
                                        regionalOffice[i].isSelected = b
                                        break
                                    }
                                }
                            }

                        }, false
                    )
                }

            adapter!!.notifyDataSetChanged()


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

            if (alertDialog != null) {
                alertDialog.setPositiveButton("Done") { dialogInterface, i ->
                    dialogInterface.dismiss()
                    val resultList = ArrayList<RegionalOfficeFilterData>()
                    for (i in 0 until regionalOffice.size) {
                        if (regionalOffice[i].isSelected) {
                            resultList.add(regionalOffice[i])
                        }
                    }
                    viewBinding.roSpinner.text = resultList.toString().substring(1, resultList.toString().length - 1)
                    roselectedId  = resultList.map { it.id }
                    regionalOfficeResetData.addAll(resultList)
                    setSalesOnSpinner(roselectedId)
                    if (roselectedId.isEmpty().not() && saleselectedId.isEmpty()){
                        viewModel.getOutletsByIdWithOR(roselectedId, saleselectedId)
                    }else if (roselectedId.isEmpty().not() && saleselectedId.isEmpty().not()) {
                        viewModel.getOutletsById(roselectedId, saleselectedId)
                    }else if (roselectedId.isEmpty() && saleselectedId.isEmpty()){
                        viewModel.getOutletData()
                    }
        //                selectionCompleteListener?.onCompleteSelection(resultList)
                }
            }
            if (alertDialog != null) {
                alertDialog.setNegativeButton("clear"){ DialogInterface, i: Int ->
                    val resultList =ArrayList<RegionalOfficeFilterData>()
                    for (i in 0 until regionalOffice.size) {
                        if (regionalOffice[i].isSelected) {
                            regionalOffice[i].isSelected = false
                            resultList.remove(regionalOffice[i])
                        }
                    }
                    viewBinding.roSpinner.text = ""
        //                selectionCompleteListener?.onCompleteSelection(resultList)
                }
            }

            alertDialog?.show()
        }

    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setSalesOnSpinner(id: List<Int>) {

        for(i in 0 until id.size){
            roid = id[i]
        }
        val sales = salesArea.filter {
            it.roid == roid
        }.map {
            RegionalOfficeFilterData(
                it.said!!,
                it.salesarea!!,
                isSelected = false
            )
        }.distinctBy {
            it.id
        }.toMutableList()
        sales.sortBy { it.name }
        val setItems = sales.distinctBy { it.name }
        sales.clear()
        sales.addAll(setItems)



        viewBinding.salesSpinner.setOnClickListener {
            val alertDialog = context?.let { it1 -> AlertDialog.Builder(it1) }
            val inflater = LayoutInflater.from(context)
            val convertView = inflater.inflate(R.layout.searchable_list_layout, null)

            alertDialog!!.setView(convertView)
            alertDialog!!.setTitle("Select Sales Area")

            val searchView = convertView.findViewById<SearchView>(R.id.searchView)
            val recyclerView =
                convertView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
            val mLayoutManager = LinearLayoutManager(context)

            val adapter =
                context?.let { it1 ->
                    SearchableAdapter(
                        it1,
                        sales,
                        sales,
                        object : SearchableAdapter.ItemClickListener {
                            override fun onItemClicked(
                                item: RegionalOfficeFilterData,
                                position: Int,
                                b: Boolean
                            ) {
                                for (i in 0 until sales.size) {
                                    if (sales[i].id == item.id) {
                                        sales[i].isSelected = b
                                        break
                                    }
                                }
                            }

                        }, false)
                }

            adapter!!.notifyDataSetChanged()


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
                    adapter!!.filter.filter(newText)
                    return false
                }
            })

            alertDialog.setPositiveButton("Done") { dialogInterface, i ->
                dialogInterface.dismiss()
                val resultList = ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until sales.size) {
                    if (sales[i].isSelected) {
                        resultList.add(sales[i])
                    }
                }
                viewBinding.salesSpinner.text = resultList.toString().substring(1, resultList.toString().length - 1)
                saleselectedId  = resultList.map { it.id }
                if (roselectedId.isEmpty().not() && saleselectedId.isEmpty()){
                    viewModel.getOutletsByIdWithOR(roselectedId, saleselectedId)
                }else if (roselectedId.isEmpty().not() && saleselectedId.isEmpty().not()) {
                    viewModel.getOutletsById(roselectedId, saleselectedId)
                }else if (roselectedId.isEmpty() && saleselectedId.isEmpty()){
                    viewModel.getOutletData()
                }
//                selectionCompleteListener?.onCompleteSelection(resultList)
            }
            alertDialog.setNegativeButton("clear"){ DialogInterface, i: Int ->
                val resultList =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until sales.size) {
                    if (sales[i].isSelected) {
                        sales[i].isSelected = false
                        resultList.remove(sales[i])
                    }
                }
                viewBinding.salesSpinner.text = ""

//                selectionCompleteListener?.onCompleteSelection(resultList)
            }

            alertDialog.show()

            viewBinding.resestTV.setOnClickListener {
                val resultList = ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until regionalOfficeResetData.size) {
                    if (regionalOfficeResetData[i].isSelected) {
                        regionalOfficeResetData[i].isSelected = false
                        resultList.remove(regionalOfficeResetData[i])
                        viewBinding.roSpinner.setText("")
                    }
                }
                roselectedId.clear()
                saleselectedId.clear()
                val result =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until sales.size) {
                    if (sales[i].isSelected) {
                        sales[i].isSelected = false
                        result.remove(sales[i])
                        viewBinding.salesSpinner.text = ""
                    }
                }
                if (roselectedId.isEmpty().not() && saleselectedId.isEmpty()){
                    viewModel.getOutletsByIdWithOR(roselectedId, saleselectedId)
                }else if (roselectedId.isEmpty().not() && saleselectedId.isEmpty().not()) {
                    viewModel.getOutletsById(roselectedId, saleselectedId)
                }else if (roselectedId.isEmpty() && saleselectedId.isEmpty()){
                    viewModel.getOutletData()
                }

            }
        }
    }


    override fun onOutletSelected(outletsList: OutletsList) {
        callbackToComplaintSelectorFragment?.onOutletSelected(outletsList)
        dismiss()
    }

}