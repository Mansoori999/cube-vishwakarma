package com.vinners.cube_vishwakarma.ui.profile

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import coil.api.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
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

    private lateinit var oldPassword:EditText
    private lateinit var newPassword:EditText

    private lateinit var mAlertDialog:AlertDialog

    override val viewModel: ProfileActivityViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.backbtn.setOnClickListener {
            onBackPressed()
        }
        val  myAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.card_anim)
        viewBinding.personalDetail.setOnClickListener {
            viewBinding.personalDetail.startAnimation(myAnim)
            val intent = Intent(this,ProfileDetailsActivity::class.java)
            startActivity(intent)
        }
        viewBinding.bankDetail.setOnClickListener {
            viewBinding.bankDetail.startAnimation(myAnim)
            val intent = Intent(this,BankDetailsAndOtherDetailsActivity::class.java)
            intent.putExtra(BankDetailsAndOtherDetailsActivity.ENABLE_BANK_DETAILS, true)
            startActivity(intent)
        }
        viewBinding.otherDetail.setOnClickListener {
            viewBinding.otherDetail.startAnimation(myAnim)
            val intent = Intent(this,BankDetailsAndOtherDetailsActivity::class.java)
            intent.putExtra(BankDetailsAndOtherDetailsActivity.ENABLE_OTHER_DETAILS, true)
            startActivity(intent)
        }
        viewBinding.passwordChanged.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.forgot_password_dialog_layout,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Change Password")

            mAlertDialog = mBuilder.show()
            oldPassword = dialogView.findViewById<EditText>(R.id.text_password)
            newPassword = dialogView.findViewById<EditText>(R.id.re_password)
            val passwordToggle = dialogView.findViewById<ImageView>(R.id.password_toggle)
            val rePasswordToggle = dialogView.findViewById<ImageView>(R.id.re_password_toggle)
            passwordToggle.setOnClickListener {
                if(oldPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                    passwordToggle.setImageResource(com.vinners.cube_vishwakarma.feature_auth.R.drawable.ic_invisible)
                    //Show Password
                    oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                }
                else{

                    passwordToggle.setImageResource(com.vinners.cube_vishwakarma.feature_auth.R.drawable.ic_visibile)
                    //Hide Password
                    oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())

                }
            }
            rePasswordToggle.setOnClickListener {
                if(newPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){


                    rePasswordToggle.setImageResource(com.vinners.cube_vishwakarma.feature_auth.R.drawable.ic_invisible)
                    //Show Password
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                }
                else{

                    rePasswordToggle.setImageResource(com.vinners.cube_vishwakarma.feature_auth.R.drawable.ic_visibile)
                    //Hide Password
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())

                }

            }


            val cancle = dialogView.findViewById<Button>(R.id.cancelbtn)
            val update = dialogView.findViewById<Button>(R.id.updatebtn)
            cancle.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()
            }
            update.setOnClickListener{
               setValidationChangePassword()

            }

        }
        viewBinding.refresh.setOnClickListener {
            viewModel.refreshProfileData()
        }
    }

    private fun setValidationChangePassword() {
        if  (oldPassword.isVisible && oldPassword.text.isNullOrBlank()){
            showInformationDialog("Please enter password")
            return
        }
        if  (newPassword.isVisible && newPassword.text.isNullOrBlank()){
            showInformationDialog("Please re-enter password")
            return
        }

        if (oldPassword.text.toString() != newPassword.text.toString()){
            showInformationDialog("Re-Password is not matching.please try again")
            return
        }
        viewModel.changedUserPassword(
                newpassword = newPassword.text.toString()
        )
    }

    override fun onInitViewModel() {
        viewModel.profile.observe(this, Observer {

            if (it.pic.isNullOrEmpty().not()) {
                viewBinding.profilePic.load(appInfo.getFullAttachmentUrl(it.pic!!))

            } else {
                viewBinding.profilePic.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.user,
                        null
                    )
                )
            }
            viewBinding.hiuserTV.setText(it.name)
            viewBinding.hiuserMobileTV.setText(it.mobile)
            viewBinding.designationTV.setText(it.designation)
            viewBinding.mailTV.setText(it.email)
            viewBinding.phoneTV.text = "+91${it.mobile}"


        })
        viewModel.initViewModel()

        viewModel.refreshProfileState.observe(this, Observer {
            when(it){
               Lce.Loading->{
                   viewBinding.loadingData.setVisibilityVisible()
                   viewBinding.refresh.setVisibilityGone()
               }
                is Lce.Content->{
                    viewBinding.loadingData.setVisibilityGone()
                    viewBinding.refresh.setVisibilityVisible()
                    Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
                }
                is Lce.Error->{
                    viewBinding.loadingData.setVisibilityGone()
                    viewBinding.refresh.setVisibilityVisible()
                    showInformationDialog(it.error)

                }

            }
        })

        viewModel.changeduserpasswordState.observe(this, Observer {
            when(it){
                Lce.Loading->{

                }
                is Lce.Content->{
                    Toast.makeText(this, "Successfully changed password", Toast.LENGTH_SHORT).show()
                    mAlertDialog.dismiss()

                }
                is Lce.Error->{
                    showInformationDialog(it.error)

                }
            }
        })
    }
}