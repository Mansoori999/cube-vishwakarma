package com.vinners.trumanms.feature.wallet.ui.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.help.QueryRequest
import com.vinners.trumanms.data.models.money.Money
import com.vinners.trumanms.feature.wallet.R
import com.vinners.trumanms.feature.wallet.databinding.FragmentWalletBinding
import com.vinners.trumanms.feature.wallet.ui.di.DaggerWalletComponent
import com.vinners.trumanms.feature.wallet.ui.di.WalletViewModelFactory
import com.vinners.trumanms.feature.wallet.ui.ui.transactionHistory.TranctionHistoryActivity
import javax.inject.Inject

class WalletFragment :
    BaseFragment<FragmentWalletBinding, WalletInfoViewModel>(R.layout.fragment_wallet) {
    private var alertDialog: AlertDialog? = null
    private var redeemValue: Float = 0.0f
    lateinit var submitButton: Button

    @Inject
    lateinit var viewModelFactory: WalletViewModelFactory
    override val viewModel: WalletInfoViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {
        DaggerWalletComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.tranctionHistoryBtn.setOnClickListener {
            startActivity(Intent(requireContext(), TranctionHistoryActivity::class.java))
        }
        viewBinding.exclusiveOfferLayout.setOnClickListener {
            startActivity(Intent(requireContext(), ExclusiveOfferActivity::class.java))
        }
        viewBinding.referAndEarnLayout.setOnClickListener {
            startActivity(Intent(requireContext(), ReferActivity::class.java))
        }
        viewBinding.redeemableLayout.setOnClickListener {
            if (redeemValue > 0)
                openRedeemDiaolog()
            else
                showInformationDialog("Redeemable value can not be zero")
        }
    }

    override fun onInitViewModel() {
        viewModel.moneyState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    setDataOnView(it.content)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.redeemMonetState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    submitButton.showProgress {
                        "Loading"
                        progressColor = Color.WHITE
                    }
                }
                is Lse.Success -> {
                    submitButton.hideProgress(
                        "Submit"
                    )
                    alertDialog?.dismiss()
                    viewModel.getMoney()
                    Toast.makeText(
                        requireContext(),
                        "Money redeemed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Lse.Error -> {
                    submitButton.hideProgress(
                        "Submit"
                    )
                }
            }
        })
        viewModel.getMoney()
    }

    private fun setDataOnView(money: Money) {
        if (money.reedemable != null)
            redeemValue = money.reedemable!!
        viewBinding.totalEarning.text = money.totalEarned.toString()
        viewBinding.availbleMoney.text = money.available.toString()
        viewBinding.redeemableMoney.text = money.reedemable.toString()
    }

    fun openRedeemDiaolog() {
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.redeem_money_dialog_layout, null)
        submitButton = view.findViewById(R.id.querySubmitBtn)
        val query = view.findViewById<TextInputEditText>(R.id.queryEt)
        val queryLayout = view.findViewById<TextInputLayout>(R.id.queryLayout)
        query.doOnTextChanged { text, start, count, after ->
            if (queryLayout.error != null)
                queryLayout.error = null
        }
        submitButton.setOnClickListener {
            val redeemAmount =
                if (query.text.isNullOrEmpty().not())
                    query.text.toString().toFloat()
                else
                    0.0f
            if (redeemAmount > 0 && redeemAmount <= redeemValue)
                viewModel.submitRedeem(redeemAmount.toString())
            else
                queryLayout.error =
                    "Entered value must be greater than 0 & less than redeemable value"
        }
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(view)
        alertDialog = alertDialogBuilder.create()
        alertDialog?.show()
    }
}