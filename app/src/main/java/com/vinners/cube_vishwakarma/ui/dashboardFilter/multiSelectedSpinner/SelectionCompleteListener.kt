package com.devstune.searchablemultiselectspinner

import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData

interface SelectionCompleteListener {
    fun onCompleteSelection(selectedItems: ArrayList<RegionalOfficeFilterData>)
}