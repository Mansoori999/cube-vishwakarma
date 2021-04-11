package com.vinners.trumanms.cache.entityMappers

import com.vinners.trumanms.cache.entities.CachedProfile
import com.vinners.trumanms.cache.entityMappers.EntityMapper
import com.vinners.trumanms.data.models.profile.Profile
import javax.inject.Inject

class ProfileEntityMapper @Inject constructor() : EntityMapper<CachedProfile, Profile> {

    override fun mapFromCached(type: CachedProfile): Profile {
        return Profile(
         firstName = type.firstName,
            lastName = type.lastName,
            mobile = type.mobile,
            email = type.email,
            agencyType = type.agencyType,
            userType = type.userType,
            whatsAppMobileNumber = type.whatsAppMobileNumber,
            dob = type.dob,
            gender = type.gender,
            district = type.district,
            cityId = type.cityId,
            pinCode = type.pinCode,
            pinCodeId = type.pinCodeId,
            education = type.education,
            experience = type.experience,
            languages = type.languages,
            twoWheeler = type.twoWheeler,
            workCategory = type.workCategory,
            teamSize = type.teamSize,
            websitePage = type.websitePage,
            facebookPage = type.facebookPage,
            designation = type.designationName,
            gpsAddress = null,
            gps = null,
            password = "pass",
            agencyName = type.agencyName,
            yearsInBusiness = type.yearInBusiness,
            bankName = type.bankName,
            accountNo = type.accountNo,
            nameOnBank = type.nameOnBank,
            mobileOnBank = type.mobileOnBank,
            ifsc = type.ifsc,
            state = type.state,
            stateId = type.stateId,
            districtId = type.districtId,
            profilePic = type.profilePic,
            aadharNo = type.aadharNo,
            profilePicUpdated = type.profilePicUpdated
        )
    }

    override fun mapToCached(type: Profile): CachedProfile {
        return CachedProfile(
            firstName = type.firstName,
            lastName = type.lastName,
            mobile = type.mobile,
            email = type.email,
            agencyType = type.agencyType,
            userType = type.userType,
            whatsAppMobileNumber = type.whatsAppMobileNumber,
            dob = type.dob,
            gender = type.gender,
            district = type.district,
            cityId = type.cityId,
            pinCode = type.pinCode,
            pinCodeId = type.pinCodeId,
            education = type.education,
            experience = type.experience,
            languages = type.languages,
            twoWheeler = type.twoWheeler,
            workCategory = type.workCategory,
            teamSize = type.teamSize,
            websitePage = type.websitePage,
            facebookPage = type.facebookPage,
            designationName = type.designation,
            agencyName = type.agencyName,
            yearInBusiness = type.yearsInBusiness,
            bankName = type.bankName,
            accountNo = type.accountNo,
            nameOnBank = type.nameOnBank,
            mobileOnBank = type.mobileOnBank,
            ifsc = type.ifsc,
            state = type.state,
            stateId = type.stateId,
            districtId = type.districtId,
            profilePic = type.profilePic,
            aadharNo = type.aadharNo,
            profilePicUpdated = type.profilePicUpdated
        )
    }


}