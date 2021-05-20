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
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityProfileBinding
import com.vinners.cube_vishwakarma.databinding.ActivityProfileDetailsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity
import javax.inject.Inject

class ProfileDetailsActivity : BaseActivity<ActivityProfileDetailsBinding, ProfileActivityViewModel>(R.layout.activity_profile_details) {

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
        viewBinding.profileToolbar.setNavigationOnClickListener {
            onBackPressed()
        }



    }

    override fun onInitViewModel() {

            viewModel.profile.observe(this, Observer {

                if (it.aadhaarno == null || it.pan == null || it.voterid == null || it.dlnumber == null){
                    viewBinding.adharno.setText("Aadhar no : NA")
                    viewBinding.panNo.setText("Pan card no : NA")
                    viewBinding.voterid.setText("Voter Id : NA")
                    viewBinding.drivinglicence.setText("Driving Licence no : NA")
                }else{
                    viewBinding.adharno.setText("Aadhar no : ${it.aadhaarno}")
                    viewBinding.panNo.setText("Pan card no : ${it.pan}")
                    viewBinding.voterid.setText("Voter Id : ${it.voterid}")
                    viewBinding.drivinglicence.setText("Driving Licence no : ${it.dlnumber}")
                }

                    if (it.panpic.isNullOrEmpty().not())
                        viewBinding.pancardpic.imageView.load(appInfo.getFullAttachmentUrl(it.panpic!!))
                    else
                        viewBinding.pancardpic.imageView.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.ic_nodocuploaded,
                                null
                            )
                        )

                    if (it.dlpic.isNullOrEmpty().not())
                        viewBinding.drivingImage.imageView.load(appInfo.getFullAttachmentUrl(it.dlpic!!))
                    else
                        viewBinding.drivingImage.imageView.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.ic_nodocuploaded,
                                null
                            )
                        )

                    adharPicAndVoiterIdPic(it)

            })
            viewModel.initViewModel()

    }

    private fun adharPicAndVoiterIdPic(it: Profile) {
        val picArray = it.aadhaarpic?.trim()?.split(",")

        if (picArray != null && picArray.size != 0 ) {

            for (i in 0 until picArray.size) {
                if (i == 0) {
                    viewBinding.adharpicfront.imageView.load(appInfo.getFullAttachmentUrl(picArray[i]))
                } else if (i == 1) {
                    viewBinding.adharpicback.imageView.load(appInfo.getFullAttachmentUrl(picArray[i]))
                }
            }
        }

        val voterIdPicArray = it.voteridpic?.trim()?.split(",")
        if (voterIdPicArray != null && voterIdPicArray.size != 0 ) {

            for (i in 0 until voterIdPicArray.size) {
                if (i == 0) {
                    viewBinding.voterpicfront.imageView.load(appInfo.getFullAttachmentUrl(voterIdPicArray[i]))
                } else if (i == 1) {
                    viewBinding.voterpicback.imageView.load(appInfo.getFullAttachmentUrl(voterIdPicArray[i]))
                }
            }
        }
    }
}