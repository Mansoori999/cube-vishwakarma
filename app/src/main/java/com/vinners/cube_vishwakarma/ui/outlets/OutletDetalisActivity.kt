package com.vinners.cube_vishwakarma.ui.outlets

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityOutletDetalisBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import javax.inject.Inject

class OutletDetalisActivity : BaseActivity<ActivityOutletDetalisBinding, OutletsViewModel>(R.layout.activity_outlet_detalis) {

    companion object {
        const val OUTLET_ID = "outeltid"
    }

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    @Inject
    lateinit var appInfo : AppInfo

    private var outletid: String? = null

    private var gps : String ? = null

    override val viewModel: OutletsViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        outletid = intent.getStringExtra(OUTLET_ID)
        viewBinding.outletDetailsToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val mapUrl = "http://maps.google.co.in/maps?q="+ gps
        viewBinding.locationpin.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q="+ gps))
            startActivity(intent)
        }
    }

    override fun onInitViewModel() {
        outletid.let { viewModel.getOutletDetailsData(it!!) }
        viewModel.outletDetailsListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.loadingData.setVisibilityVisible()
                    viewBinding.outletDetailScreen.setVisibilityGone()

                }
                is Lce.Content->{

                    if (it.content !=null) {
                        if (it.content.pic != null){
                            viewBinding.imageView.load(appInfo.getFullAttachmentUrl(it.content.pic!!))
                        }else{
                            viewBinding.imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_image,null))
                        }
                        viewBinding.outletDetailScreen.setVisibilityVisible()
                        viewBinding.loadingData.setVisibilityGone()
                        viewBinding.outletid.text = it.content.outletid
                        viewBinding.outletName.text = it.content.outlet
                        viewBinding.customercode.text = it.content.customercode
                        viewBinding.roid.text = it.content.roid
                        viewBinding.regionaloffice.text = it.content.regionaloffice
                        viewBinding.said.text = it.content.said
                        viewBinding.sales.text = it.content.salesarea
                        viewBinding.district.text = it.content.district
                        viewBinding.location.text = it.content.location
                        viewBinding.address.text = it.content.address
                        viewBinding.category.text = it.content.category
                        viewBinding.contactperson.text = it.content.contactperson
                        viewBinding.primarymobile.text = it.content.primarymobile
                        viewBinding.secondarymobile.text = it.content.secondarymobile
                        viewBinding.primarymail.text = it.content.primarymail
                        viewBinding.secondarymail.text = it.content.secondarymail
                        if (it.content.gps!!.isEmpty().not() && it.content.gps != "," ) {
                            viewBinding.gps.text = it.content.gps
                            viewBinding.locationpin.setVisibilityVisible()
                        }else{
                            viewBinding.gps.text = it.content.gps
                            viewBinding.locationpin.setVisibilityGone()
                        }
                        viewBinding.gpsaddress.text = it.content.gpsaddress
                        viewBinding.zone.text = it.content.zone
                        viewBinding.zoneid.text = it.content.zoneid
                        gps = it.content.gps!!
                    }else{
                        viewBinding.loadingData.setVisibilityVisible()
                    }

                }
                is Lce.Error ->{
                    viewBinding.outletDetailScreen.setVisibilityVisible()
                    viewBinding.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)
                }
            }
        })

    }
}