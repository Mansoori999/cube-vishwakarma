package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.vinners.trumanms.core.base.BaseDialogFragment
import com.vinners.trumanms.data.models.stateAndCity.Languages
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.FragmentLanguageDialogBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import javax.inject.Inject
import kotlin.text.StringBuilder

class LanguageDialogFragment :
    BaseDialogFragment<FragmentLanguageDialogBinding, StateCityViewModel>(R.layout.fragment_language_dialog),
    LanguageOnClickListener {

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    lateinit var onDialogClickListener: OnDialogClickListener
    private var checkedItemList = mutableListOf<String>()
    val languageItemsWrapper = StringBuilder()
    private var selectedLanguageItems: String? = null

    val languageList = listOf<Languages>(
        Languages(
            "Hindi",
            false
        ), Languages(
            "English",
            false
        ),
        Languages(
            "Punjabi",
            false
        ),
        Languages(
            "Tamil",
            false
        ),
        Languages(
            "Bengali",
            false
        ),
        Languages(
            "Marathi",
            false
        ),
        Languages(
            "Telugu",
            false
        ),
        Languages(
            "Gujarati",
            false
        ),
        Languages(
            "Urdu",
            false
        ),
        Languages(
            "Kannada",
            false
        ),
        Languages(
            "Odia",
            false
        ),
        Languages(
            "Malayalam",
            false
        )
    )

    override val viewModel: StateCityViewModel by viewModels {
        viewModelFactory
    }

    private val languageRecyclerAdapter: LanguageRecyclerAdapter by lazy {
        LanguageRecyclerAdapter().apply {
            updateList(setLanguageList())
            setClickListener(this@LanguageDialogFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            selectedLanguageItems = it.getString(INTENT_EXTRA_LANGUAGE_ITEMS) ?: return@let
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onInitDependencyInjection() {
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {

            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onInitDataBinding() {
        viewBinding.languageRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.languageRecyclerView.adapter = languageRecyclerAdapter
        viewBinding.okBtn.setOnClickListener {
            languageItemsWrapper.setLength(0)
            for (i in checkedItemList.indices) {
                languageItemsWrapper.append(checkedItemList.get(i))
                if (i < checkedItemList.size.minus(1))
                    languageItemsWrapper.append(",")
            }
            onDialogClickListener.onLanguageClick(languageItemsWrapper)
            dismiss()
        }
    }

    override fun onInitViewModel() {

    }

    companion object {
        const val TAG = "cityDialogFragment"
        const val INTENT_EXTRA_LANGUAGE_ITEMS = "language_items"
        const val LANGUAGE = "Languages"
        const val NOT_IN_LIST = "Not In List"

        fun launchDialogFragment(
            fragmentManager: FragmentManager,
            selectedLanguageItems: String?,
            onDialogClickListener: OnDialogClickListener
        ) {
            val fragment = LanguageDialogFragment()
            fragment.arguments = bundleOf(INTENT_EXTRA_LANGUAGE_ITEMS to selectedLanguageItems)
            fragment.onDialogClickListener = onDialogClickListener
            fragment.show(fragmentManager, TAG)
        }
    }

    private fun setLanguageList(): List<Languages> {
        if (selectedLanguageItems.isNullOrEmpty().not()) {
            val selectedWorkItemList = selectedLanguageItems?.split(",")
            selectedWorkItemList?.forEach {
                val workItem = it
                checkedItemList.add(workItem)
                languageList.forEach {
                    if (it.name.equals(workItem, true))
                        it.isBoxChecked = true
                }
            }
        }
        return languageList
    }

    override fun onlanguageClick(languages: Languages) {
        if (languages.isBoxChecked && checkedItemList.contains(languages.name).not())
            checkedItemList.add(languages.name)
        else if (languages.isBoxChecked.not() && checkedItemList.contains(languages.name))
            checkedItemList.remove(languages.name)
    }
}