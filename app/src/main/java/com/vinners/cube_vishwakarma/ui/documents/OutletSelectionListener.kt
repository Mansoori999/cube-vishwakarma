package mobile.fitbitMerch.ui.masterData

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList


interface OutletSelectionListener {

        fun onOutletSelected(outletsList: OutletsList)

}
