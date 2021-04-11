package com.vinners.trumanms.core.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.vinners.trumanms.core.QuickAlertDialog
import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.locale.LocaleManager

abstract class BaseActivity<B : ViewDataBinding,M : ViewModel>(
    @LayoutRes
    private val layoutId: Int
) : AppCompatActivity() {

    protected abstract val viewModel: M

    private var _viewBinding: B? = null
    val viewBinding :B get() {
        return _viewBinding ?: throw IllegalStateException("viewBinding Requested But _viewBinding Found Null")
    }

    /**
     * Called to initialize dagger injection dependency graph when fragment is attached.
     */
    abstract fun onInitDependencyInjection()

    /**
     * Called to Initialize view data binding variables when fragment view is created.
     */
    abstract fun onInitDataBinding()


    abstract fun onInitViewModel()


    fun getCoreComponent() : CoreComponent = (this.application as CoreApplication).initOrGetCoreDependency()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be
     * attached to. The fragment should not add the view itself, but this can be used to generate
     * the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     *
     * @see Fragment.onCreateView
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = DataBindingUtil.setContentView(this, layoutId)
        viewBinding.lifecycleOwner = this
        onInitDependencyInjection()
        onInitDataBinding()
        onInitViewModel()
    }

    /**
     * Called when a fragment is first attached to its context.
     *
     * @param context The application context.
     *
     * @see Fragment.onAttach
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
       // onInitDependencyInjection()
    }
    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManager.setLocale(base))
    }
    fun showErrorDialog(errorMessage: String) {
        QuickAlertDialog.showErrorDialog(this, errorMessage)
    }

    fun showNonCancellableErrorDialog(errorMessage: String) {
        QuickAlertDialog.showNonCancellableErrorDialog(this, errorMessage)
    }

    fun showSuccessDialog(message: String) {
        QuickAlertDialog.showSuccessDialog(this, message)
    }

    fun showNonCancellableSuccessDialog(message: String) {
        QuickAlertDialog.showNonCancellableSuccessDialog(this, message)
    }

    fun showInformationDialog(message: String) {
        QuickAlertDialog.showInformationDialog(this, message)
    }
}