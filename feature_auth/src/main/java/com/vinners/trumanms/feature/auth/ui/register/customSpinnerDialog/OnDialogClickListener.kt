package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import com.vinners.trumanms.data.models.stateAndCity.City
import com.vinners.trumanms.data.models.stateAndCity.Pincode
import com.vinners.trumanms.data.models.stateAndCity.State
import java.lang.StringBuilder

interface OnDialogClickListener {
    fun onCityClick(city: City)
    fun onPincodeClick(pincode: Pincode)
    fun onStateClick(state: State)
    fun onLanguageClick(language: StringBuilder)
    fun onWorkCatClick(workCat: StringBuilder)
}