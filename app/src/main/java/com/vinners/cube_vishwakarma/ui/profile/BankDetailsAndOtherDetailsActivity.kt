package com.vinners.cube_vishwakarma.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityBankDetailsAndOtheretailsBinding
import com.vinners.cube_vishwakarma.databinding.ActivityProfileDetailsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import javax.inject.Inject

class BankDetailsAndOtherDetailsActivity : BaseActivity<ActivityBankDetailsAndOtheretailsBinding, ProfileActivityViewModel>(R.layout.activity_bank_details_and_otheretails) {

    companion object {
        const val ENABLE_BANK_DETAILS = "bank_details"
        const val ENABLE_OTHER_DETAILS = "other_details"


    }
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    var personalDoc:Boolean = false
    var bankDetail:Boolean= false
    var otherDetail : Boolean= false

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: ProfileActivityViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }


    override fun onInitDataBinding() {
        viewBinding.bankBtn.setOnClickListener {
            onBackPressed()
        }
        bankDetail = intent.getBooleanExtra(ENABLE_BANK_DETAILS,false)
        otherDetail = intent.getBooleanExtra(ENABLE_OTHER_DETAILS,false)
         if (bankDetail == true){
            viewBinding.bankDetailsAndOtherTextview.setText("Bank Details")
            viewBinding.bankDetailsScreen.setVisibilityVisible()
            viewBinding.otherDetailsScreen.setVisibilityGone()
        }else if (otherDetail == true){
             viewBinding.bankDetailsAndOtherTextview.setText("Other Details")
            viewBinding.bankDetailsScreen.setVisibilityGone()
            viewBinding.otherDetailsScreen.setVisibilityVisible()
        }

    }

    override fun onInitViewModel() {
        if (bankDetail == true){
            viewModel.profile.observe(this, Observer {
                if (it.pic.isNullOrEmpty().not())
                    viewBinding.profilePic.load(appInfo.getFullAttachmentUrl(it.pic!!))
                else
                    viewBinding.profilePic.setImageDrawable( ResourcesCompat.getDrawable(getResources(),R.drawable.user,null))
                viewBinding.userName.setText(it.name)
                if (it.nameonbank == null ){
                    viewBinding.accountholderName.setText(": NA")

                }else {
                    viewBinding.accountholderName.setText(": ${it.nameonbank}")
                }
                if(it.accountno == null){
                    viewBinding.accountno.setText(": NA")

                }else{
                    viewBinding.accountno.setText(": ${it.accountno}")

                }
                if (it.bankname == null){
                    viewBinding.bankname.setText(": NA")

                }else{
                    viewBinding.bankname.setText(": ${it.bankname}")

                }
                if (it.ifsc== null){
                    viewBinding.ifsccode.setText(": NA")

                }else{
                    viewBinding.ifsccode.setText(": ${it.ifsc}")

                }
                if (it.bankbranch == null){
                    viewBinding.branch.setText(": NA")

                }else{
                    viewBinding.branch.setText(": ${it.bankbranch}")
                }


            })
            viewModel.initViewModel()


        }else if (otherDetail == true){
            viewModel.profile.observe(this, Observer {
                if (it.pic.isNullOrEmpty().not())
                    viewBinding.profilePic.load(appInfo.getFullAttachmentUrl(it.pic!!))
                else
                    viewBinding.profilePic.setImageDrawable( ResourcesCompat.getDrawable(getResources(),R.drawable.user,null))
                viewBinding.userName.setText(it.name)
                if (it.emergencyname == null || it.emergencymobile == null || it.emergencyrelation == null){
                    viewBinding.contactName.setText(": NA")
                    viewBinding.mobile.setText(": NA")
                    viewBinding.relation.setText(": NA")

                }else{
                    viewBinding.contactName.setText(": ${it.emergencyname}")
                    viewBinding.mobile.setText(": ${it.emergencymobile}")
                    viewBinding.relation.setText(": ${it.emergencyrelation}")
                }

                if (it.referencename == null || it.referencemobile == null || it.referencerelation == null){
                    viewBinding.contactNameReference.setText(": NA")
                    viewBinding.mobileReference.setText(": NA")
                    viewBinding.relationReference.setText(": NA")

                }else{
                    viewBinding.contactNameReference.setText(": ${it.referencename}")
                    viewBinding.mobileReference.setText(": ${it.referencemobile}")
                    viewBinding.relationReference.setText(": ${it.referencerelation}")

                }

            })
            viewModel.initViewModel()


        }

    }
}