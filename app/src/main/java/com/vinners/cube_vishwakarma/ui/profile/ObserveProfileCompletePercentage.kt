package com.vinners.cube_vishwakarma.ui.profile

import com.vinners.cube_vishwakarma.core.executors.PostExecutionThread
import com.vinners.cube_vishwakarma.core.executors.ThreadExecutor
import com.vinners.cube_vishwakarma.core.observerUseCase.ObservableUseCase
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class ObserveProfileCompletePercentage @Inject constructor(private val authRepository: AuthRepository
                                                           , private val threadExecutor: ThreadExecutor
                                                           , private val postExecutionThread: PostExecutionThread
) : ObservableUseCase<Any?, Int>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Any?): Observable<Int> {
       return authRepository.observeProfile().map {
                getProfilePercentageComplete(it)
       }
    }

    fun getProfilePercentageComplete(profile: Profile): Int{
        var profileCompletePercentage = 0

        if (profile.pic.isNullOrEmpty().not())
            profileCompletePercentage += 25
//        if (profile.userType.equals(RegisterActivity.INDIVIDUAL_USER_TYPE,true)){
//            if (profile.firstName.isNullOrEmpty().not()
//                && profile.mobile.isNullOrEmpty().not()
//                && profile.email.isNullOrEmpty().not()
//                && profile.lastName.isNullOrEmpty().not()
//                && profile.dob.isNullOrEmpty().not())
//                profileCompletePercentage += 25
//        }
//
//        if (profile.userType.equals(RegisterActivity.AGENCY_USER_TYPE,true)){
//            if (profile.firstName.isNullOrEmpty().not()
//                && profile.mobile.isNullOrEmpty().not()
//                && profile.email.isNullOrEmpty().not()
//                && profile.lastName.isNullOrEmpty().not()
//                && profile.agencyName.isNullOrEmpty().not()
//                && profile.agencyType.isNullOrEmpty().not()
//                && profile.designation.isNullOrEmpty().not()
//                && profile.whatsAppMobileNumber.isNullOrEmpty().not())
//                profileCompletePercentage += 25
//        }
//        if (profile.userType.equals(RegisterActivity.AGENCY_USER_TYPE,true)){
//            if (profile.teamSize.isNullOrEmpty().not()
//                && profile.languages.isNullOrEmpty().not()
//                && profile.workCategory.isNullOrEmpty().not()
//                && profile.facebookPage.isNullOrEmpty().not()
//                && profile.websitePage.isNullOrEmpty().not()
//                && profile.yearsInBusiness.isNullOrEmpty().not())
//                profileCompletePercentage += 25
//        }
//        if (profile.userType.equals(RegisterActivity.INDIVIDUAL_USER_TYPE,true)){
//            if (profile.education.isNullOrEmpty().not()
//                && profile.experience.isNullOrEmpty().not()
//                && profile.workCategory.isNullOrEmpty().not()
//                && profile.languages.isNullOrEmpty().not())
//                profileCompletePercentage += 25
//        }
        if (profile.aadhaarno.isNullOrEmpty().not()){
            profileCompletePercentage += 25
        }
        return profileCompletePercentage
    }
}