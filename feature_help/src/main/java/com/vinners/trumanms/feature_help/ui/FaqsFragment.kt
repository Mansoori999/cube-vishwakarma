package com.vinners.trumanms.feature_help.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
import com.vinners.trumanms.feature_help.databinding.FragmentFaqLayoutBinding
import com.vinners.trumanms.feature_help.di.DaggerHelpComponent
import com.vinners.trumanms.feature_help.di.HelpModuleFactory
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class FaqsFragment :
    BaseFragment<FragmentFaqLayoutBinding, HelpViewModel>(R.layout.fragment_faq_layout) {

    @Inject
    lateinit var viewModelFactory: HelpModuleFactory

    override val viewModel: HelpViewModel by viewModels {
        viewModelFactory
    }
    private var helpList = listOf<Help>()
    private var categoryList = mutableListOf<String>()
    private var alertDialog: AlertDialog? = null
    lateinit var imageContainer: ImageView
    private var imageFilePath: String? = null

    private var querySubmitListener: QuerySubmitListener? = null

    fun setQuerySubmitListener(querySubmitListener: QuerySubmitListener) {
        this.querySubmitListener = querySubmitListener
    }

    private val cameraIntegrator: CameraIntegrator by lazy {
        CameraIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }

    private val galleryIntegrator: GalleryIntegrator by lazy {
        GalleryIntegrator(this)
            .apply {
                setStorageMode(StorageMode.EXTERNAL_PUBLIC_STORAGE)
                setPublicDirectoryName(AppConstants.PUBLIC_FILE_FOLDER)
                setRequiredImageSize(ImagesSizes.OPTIMUM_MEDIUM)
            }
    }

    override fun onInitDependencyInjection() {
        DaggerHelpComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    inFlateQueAnsLayout()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }


            }
        viewBinding.needHelp.setOnClickListener {
            imageFilePath = null
            val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.query_data_layout, null)
            val categorySpinner = view.findViewById<Spinner>(R.id.categorySpinner)
            val submitBtn = view.findViewById<Button>(R.id.querySubmitBtn)
            val query = view.findViewById<TextInputEditText>(R.id.queryEt)
            val queryLayout = view.findViewById<TextInputLayout>(R.id.queryLayout)
            imageContainer = view.findViewById(R.id.queryContainer)
            val clickPhoto = view.findViewById<ImageView>(R.id.clickPhoto)
            query.doOnTextChanged { text, start, count, after ->
                if (queryLayout.error != null)
                    queryLayout.error = null
            }


            clickPhoto.setOnClickListener {
                showCameraOptionDialog()
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryList
            )
            categorySpinner.adapter = adapter
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(view)
            submitBtn.setOnClickListener {
                if (query.text.isNullOrEmpty())
                    queryLayout.error = "Enter Valid Question"
                else {
                    val category =
                        if (categorySpinner.count != 0)
                            categorySpinner.selectedItem.toString()
                        else
                            null
                    val question = query.text.toString()
                    val queryRequest = QueryRequest(
                        category = category,
                        question = question
                    )
                    viewModel.submitQuery(queryRequest,imageFilePath)
                }
            }
            alertDialog = alertDialogBuilder.create()
            alertDialog?.show()
        }
    }

    override fun onInitViewModel() {
        viewModel.helpState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    helpList = it.content
                    populateCategory(it.content)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.submitQuery.observe(this, Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    alertDialog?.dismiss()
                    querySubmitListener?.onQuerySubmit()
                }
                is Lse.Error -> {
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.getHelpList()
    }


    fun populateCategory(helpList: List<Help>) {
        // categoryList = mutableListOf<String>()
        helpList.filter {
            categoryList.add(it.category)
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryList
        )
        viewBinding.categorySpinner.adapter = adapter
    }

    fun inFlateQueAnsLayout() {
        viewBinding.queAnsContainer.removeAllViews()
        val queAnsList = mutableListOf<QueAns>()
        helpList.forEach {
            if (viewBinding.categorySpinner.selectedItem.equals(it.category)) {
                queAnsList.addAll(it.questionList)
            }

        }
        queAnsList.forEach {
            val queAnsContainer = LayoutInflater.from(requireContext())
                .inflate(R.layout.que_ans_layout, viewBinding.queAnsContainer, false)
            queAnsContainer.findViewById<TextView>(R.id.queEt).text = it.question
            queAnsContainer.findViewById<TextView>(R.id.ansEt).text = it.answer
            queAnsContainer.findViewById<ImageView>(R.id.showAnsIcon).setOnClickListener {
                queAnsContainer.findViewById<ImageView>(R.id.showAnsIcon).setVisibilityGone()
                queAnsContainer.findViewById<ImageView>(R.id.hideAnsIcon).setVisibilityVisible()
                queAnsContainer.findViewById<TextView>(R.id.ansEt).setVisibilityVisible()
            }
            queAnsContainer.findViewById<ImageView>(R.id.hideAnsIcon).setOnClickListener {
                queAnsContainer.findViewById<TextView>(R.id.ansEt).setVisibilityGone()
                queAnsContainer.findViewById<ImageView>(R.id.hideAnsIcon).setVisibilityGone()
                queAnsContainer.findViewById<ImageView>(R.id.showAnsIcon).setVisibilityVisible()
            }
            viewBinding.queAnsContainer.addView(queAnsContainer)
        }

    }

    fun openCamera() {
        try {
            cameraIntegrator.initiateCapture()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openGallery() {
        try {
            galleryIntegrator.initiateImagePick()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showCameraOptionDialog() {
        val optionsForDialog = arrayOf<CharSequence>("Open Camera", "Select from Gallery")
        val alertBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        alertBuilder.setTitle("Select An Option")
        alertBuilder.setIcon(R.drawable.ic_camera)
        alertBuilder.setItems(optionsForDialog, DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        })
        alertBuilder.setNegativeButton("Cancel") { dialog12, _ -> dialog12.dismiss() }
        alertBuilder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraIntegrator.REQUEST_IMAGE_CAPTURE)
            cameraIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
        else if (requestCode == GalleryIntegrator.REQUEST_IMAGE_PICK)
            galleryIntegrator.parseResults(requestCode, resultCode, data, imageCallback)
    }

    private val imageCallback =
        ImageCallback { requestedBy, result, error ->

            if (result != null) {
                val imagePath = result.imagePath
                imageContainer.load(File(imagePath))
                imageFilePath = result.imagePath
            }
        }

    interface QuerySubmitListener {
        fun onQuerySubmit()
    }
}