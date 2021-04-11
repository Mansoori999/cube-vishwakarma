package com.vinners.trumanms.feature_help.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.himanshu.cameraintegrator.ImageCallback
import com.himanshu.cameraintegrator.ImagesSizes
import com.himanshu.cameraintegrator.integrator.CameraIntegrator
import com.himanshu.cameraintegrator.integrator.GalleryIntegrator
import com.himanshu.cameraintegrator.storage.StorageMode
import com.vinners.trumanms.core.AppConstants
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.help.Help
import com.vinners.trumanms.data.models.help.QueAns
import com.vinners.trumanms.data.models.help.QueryRequest
import com.vinners.trumanms.feature_help.R
import com.vinners.trumanms.feature_help.databinding.FragmentHelpBinding
import com.vinners.trumanms.feature_help.di.DaggerHelpComponent
import com.vinners.trumanms.feature_help.di.HelpModuleFactory
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class HelpFragment : BaseFragment<FragmentHelpBinding, HelpViewModel>(R.layout.fragment_help),
    FaqsFragment.QuerySubmitListener {
    companion object {
        private const val PERMISSION_REQUEST_STORAGE = 233
        const val REQUEST_CODE = 101
    }

    @Inject
    lateinit var viewModelFactory: HelpModuleFactory

    override val viewModel: HelpViewModel by viewModels {
        viewModelFactory
    }
    private val helpViewPagerAdapter: HelpViewPagerAdapter by lazy {
        HelpViewPagerAdapter(childFragmentManager)
    }

    private val faqsFragment: FaqsFragment by lazy {
        FaqsFragment().apply {
            setQuerySubmitListener(this@HelpFragment)
        }
    }
    private val queryFragment: QueryFragment by lazy {
        QueryFragment()
    }

    private var categoryList = mutableListOf<String>()
    private var alertDialog: AlertDialog? = null


    override fun onInitDependencyInjection() {
        DaggerHelpComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        initViewPager()
        if (storagePermissions().not())
            requestStoragePermissions()
    }

    override fun onInitViewModel() {
        viewModel.helpState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    populateCategory(it.content)
                }
                is Lce.Error -> {

                }
            }
        })

        viewModel.getHelpList()
    }

    fun populateCategory(helpList: List<Help>) {
        helpList.filter {
            categoryList.add(it.category)
        }
    }

    private fun initViewPager() {

       // val faqsFragment = FaqsFragment()
       // faqsFragment.setQuerySubmitListener(this)
        helpViewPagerAdapter.addFragment(faqsFragment, "FAQs")

       // val queryFragment = QueryFragment()
        helpViewPagerAdapter.addFragment(queryFragment, "My Queries")

        viewBinding.helpViewPager.adapter = helpViewPagerAdapter
        viewBinding.helpTabLayout.setupWithViewPager(viewBinding.helpViewPager)
    }

    override fun onQuerySubmit() {
        viewBinding.helpViewPager.setCurrentItem(1, true)
        queryFragment.refreshList()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //TODO handle manual permission here
                }
                return
            }
        }
    }

    private fun storagePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            PERMISSION_REQUEST_STORAGE
        )
    }
}