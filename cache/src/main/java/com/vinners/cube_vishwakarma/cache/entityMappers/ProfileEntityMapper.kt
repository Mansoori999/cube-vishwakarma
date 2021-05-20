package com.vinners.cube_vishwakarma.cache.entityMappers

import com.vinners.cube_vishwakarma.cache.entities.CachedProfile
import com.vinners.cube_vishwakarma.cache.entityMappers.EntityMapper
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import javax.inject.Inject

class ProfileEntityMapper @Inject constructor() : EntityMapper<CachedProfile, Profile> {

    override fun mapFromCached(type: CachedProfile): Profile {
        return Profile(
                name = type.name,
                nickname = type.nickname,
                mobile = type.mobile,
                email = type.email,
                alternatemobile = type.alternatemobile,
                logintype = type.logintype,
                designation=type.designation,
                userType = type.userType,
                dob = type.dob,
                gender = type.gender,
                loginid = type.loginid,
                pic = type.pic,
                city = type.city,
                pincode = type.pincode,
                employment = type.employment,
                education = type.education,
                managerid = type.managerid,
                deviceid = type.deviceid,
                doj = type.doj,
                dol = type.dol,
                aadhaarno = type.aadhaarno,
                aadhaarpic = type.aadhaarpic,
                pan = type.pan,
                panpic = type.panpic,
                dlnumber = type.dlnumber,
                dlpic = type.dlpic,
                bankname = type.bankname,
                nameonbank = type.nameonbank,
                pfnumber = type.pfnumber,
                ifsc = type.ifsc,
                accountno = type.accountno,
                esicnumber = type.esicnumber,
                state = type.state,
                voterid = type.voterid,
                voteridpic = type.voteridpic,
                bankbranch = type.bankbranch,
                emergencymobile = type.emergencymobile,
                emergencyname = type.emergencyname,
                emergencyrelation = type.emergencyrelation,
                referencename = type.referencename,
                referencemobile = type.referencemobile,
                referencerelation = type.referencerelation,
                address = type.address,
                createdon = type.createdon,
                createdby = type.createdby

        )
    }

    override fun mapToCached(type: Profile): CachedProfile {
        return CachedProfile(
                name = type.name,
                nickname = type.nickname,
                mobile = type.mobile,
                email = type.email,
                alternatemobile = type.alternatemobile,
                logintype = type.logintype,
                designation=type.designation,
                userType = type.userType,
                dob = type.dob,
                gender = type.gender,
                loginid = type.loginid,
                pic = type.pic,
                city = type.city,
                pincode = type.pincode,
                employment = type.employment,
                education = type.education,
                managerid = type.managerid,
                deviceid = type.deviceid,
                doj = type.doj,
                dol = type.dol,
                aadhaarno = type.aadhaarno,
                aadhaarpic = type.aadhaarpic,
                pan = type.pan,
                panpic = type.panpic,
                dlnumber = type.dlnumber,
                dlpic = type.dlpic,
                bankname = type.bankname,
                nameonbank = type.nameonbank,
                pfnumber = type.pfnumber,
                ifsc = type.ifsc,
                accountno = type.accountno,
                esicnumber = type.esicnumber,
                state = type.state,
                voterid = type.voterid,
                voteridpic = type.voteridpic,
                bankbranch = type.bankbranch,
                emergencymobile = type.emergencymobile,
                emergencyname = type.emergencyname,
                emergencyrelation = type.emergencyrelation,
                referencename = type.referencename,
                referencemobile = type.referencemobile,
                referencerelation = type.referencerelation,
                address = type.address,
                createdon = type.createdon,
                createdby = type.createdby

        )
    }


}