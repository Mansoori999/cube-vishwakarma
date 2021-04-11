package com.vinners.cube_vishwakarma.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.vinners.cube_vishwakarma.core.QuickAlertDialog
import com.vinners.cube_vishwakarma.core.di.CoreComponent

abstract class BaseFragment<B : ViewDataBinding, M : ViewModel>(
    @LayoutRes
    private val layoutId: Int
) : Fragment() {

    protected abstract val viewModel: M

    private var _viewBinding: B? = null
    val viewBinding: B
        get() {
            return _viewBinding
                ?: throw IllegalStateException("viewBinding Requested But _viewBinding Found Null")
        }

    /**
     * Called to initialize dagger injection dependency graph when fragment is attached.
     */
    abstract fun onInitDependencyInjection()

    /**
     * Called to Initialize view data binding variables when fragment view is created.
     */
    abstract fun onInitDataBinding()

    /**
     * called To Initialize ViewModel
     */
    abstract fun onInitViewModel()


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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       // if (_viewBinding == null)
            _viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    /**
     * Called when a fragment is first attached to its context.
     *
     * @param context The application context.
     *
     * @see Fragment.onAttach
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onInitDependencyInjection()
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param view The view returned by onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @see Fragment.onViewCreated
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitDataBinding()
        onInitViewModel()
    }

    /**
     * Return the [AppCompatActivity] this fragment is currently associated with.
     *
     * @throws IllegalStateException if not currently associated with an activity or if associated
     * only with a context.
     * @throws TypeCastException if the currently associated activity don't extend [AppCompatActivity].
     *
     * @see requireActivity
     */
    fun requireCompatActivity(): AppCompatActivity {
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            return activity
        } else {
            throw TypeCastException("Main activity should extend from 'AppCompatActivity'")
        }
    }

    fun getCoreComponent(): CoreComponent =
        (requireActivity().application as CoreApplication).initOrGetCoreDependency()

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    fun showErrorDialog(errorMessage: String) {
        QuickAlertDialog.showErrorDialog(requireContext(), errorMessage)
    }

    fun showNonCancellableErrorDialog(errorMessage: String) {
        QuickAlertDialog.showNonCancellableErrorDialog(requireContext(), errorMessage)
    }

    fun showSuccessDialog(message: String) {
        QuickAlertDialog.showSuccessDialog(requireContext(), message)
    }

    fun showNonCancellableSuccessDialog(message: String) {
        QuickAlertDialog.showNonCancellableSuccessDialog(requireContext(), message)
    }

    fun showInformationDialog(message: String) {
        QuickAlertDialog.showInformationDialog(requireContext(), message)
    }
}