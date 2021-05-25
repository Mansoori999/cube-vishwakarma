package com.vinners.cube_vishwakarma.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOutletList
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.repository.ComplaintRequestRepository
import com.vinners.cube_vishwakarma.data.repository.DashBoardRepository
import com.vinners.cube_vishwakarma.data.repository.ProfileRepository
import com.vinners.cube_vishwakarma.feature_auth.ui.ObserveProfileInteractor
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.LoadMetaForAddComplaintState
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileEvents {

    val logoutState: LiveData<Lse>

    val profile: LiveData<Profile>
    fun initViewModel()

    val financialFilterState: LiveData<Lce<List<DashboardFilterList>>>
    val regionalOfficeFilterState: LiveData<Lce<List<ComplaintOutletList>>>
    val dashboardState : LiveData<Lce<DashBoardResponse>>


}
class MainActivityViewModel@Inject constructor(
    private val userProfileRepository: ProfileRepository,
    private val dashBoardRepository: DashBoardRepository,
    private val complaintRequestRepository: ComplaintRequestRepository,
    private val observeProfileInteractor: ObserveProfileInteractor
) : ViewModel(), ProfileEvents  {

    private val _logoutState = MutableLiveData<Lse>()
    override val logoutState: LiveData<Lse> = _logoutState


    fun logout() {
        _logoutState.value = Lse.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userProfileRepository.logout()
                _logoutState.postValue(Lse.Success)
            } catch (e: Exception) {
                _logoutState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    override fun initViewModel() {
        startObservingProfile()
    }
    private val _profile = MutableLiveData<Profile>()
    override val profile: LiveData<Profile> = _profile

    private fun startObservingProfile() {
        observeProfileInteractor.execute(GetProfileObserver(), null)

    }
    private inner class GetProfileObserver : DisposableObserver<Profile>() {

        override fun onComplete() {}

        override fun onError(e: Throwable) {}

        override fun onNext(t: Profile) {
            _profile.postValue(t)
        }
    }

    /* DashBoard Financial year filter*/

    private val _financialFilterState =  MutableLiveData<Lce<List<DashboardFilterList>>>()
    override val financialFilterState: LiveData<Lce<List<DashboardFilterList>>> = _financialFilterState

    fun getRinancialYearFilterData() {
        _financialFilterState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = dashBoardRepository.getFinancialData()
                _financialFilterState.postValue(Lce.content(response))
            }catch (e : Exception){
                _financialFilterState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    /* DashBoard */

    private val _dashboardState =  MutableLiveData<Lce<DashBoardResponse>>()
    override val dashboardState: LiveData<Lce<DashBoardResponse>> = _dashboardState

    fun dashBoardData(startDate:String,endDate:String,regionalOfficeids:String?) {
        _dashboardState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = dashBoardRepository.getDashBoard(startDate,endDate,regionalOfficeids)
                _dashboardState.postValue(Lce.content(response))
            }catch (e : Exception){
                _dashboardState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }



    //Regional office
    private val _regionalOfficeFilterState = MutableLiveData<Lce<List<ComplaintOutletList>>>()
    override val regionalOfficeFilterState: LiveData<Lce<List<ComplaintOutletList>>>  = _regionalOfficeFilterState


    fun getRegionalOfficeFilterData() = viewModelScope.launch {

        _regionalOfficeFilterState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = complaintRequestRepository.getComplaintList()
                _regionalOfficeFilterState.postValue(Lce.content(response))
            }catch (e : Exception){
                _regionalOfficeFilterState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

}