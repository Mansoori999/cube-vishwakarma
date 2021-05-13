package com.vinners.cube_vishwakarma.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityProfileBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileActivityViewModel>(R.layout.activity_profile) {



    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    @Inject
    lateinit var appInfo: AppInfo


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
            val profilepic = findViewById<CircleImageView>(R.id.profile_pic2)
            if (it.pic.isNullOrEmpty().not())
                profilepic.load(appInfo.getFullAttachmentUrl(it.pic!!))
            else
                profilepic.setImageDrawable(getResources().getDrawable(R.drawable.user))
            viewBinding.text1.setText(it.name)
            viewBinding.text2.setText(it.mobile)
            viewBinding.text3.setText(it.designation)


        })
        viewModel.initViewModel()
    }
}