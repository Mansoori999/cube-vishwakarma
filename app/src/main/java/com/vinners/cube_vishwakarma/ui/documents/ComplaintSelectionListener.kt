package mobile.fitbitMerch.ui.masterData

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList


interface ComplaintSelectionListener {

        fun onComplaintSelected(myComplaintList: MyComplaintList)
    }