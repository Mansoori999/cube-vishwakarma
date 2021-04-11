package com.vinners.trumanms.cache.dao



import androidx.room.*
import com.vinners.trumanms.cache.entities.CachedProfile
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import java.util.*


/**
 * Created by Himanshu on 6/22/2018.
 */
@Dao
interface ProfileDao {

    @get:Query("SELECT * FROM ${CachedProfile.TABLE_NAME} LIMIT 1")
    val profile: Observable<List<CachedProfile>>

    @Query("SELECT * FROM ${CachedProfile.TABLE_NAME} LIMIT 1")
    suspend fun getCachedProfile(): CachedProfile?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cachedProfile: CachedProfile)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(cachedProfile: CachedProfile)

    @Query("UPDATE ${CachedProfile.TABLE_NAME} SET " + "${CachedProfile.COLUMN_PROFILE_PICTURE_UPDATED}=1")
    suspend fun markProfilePictureAsUpdated()

    @Query("UPDATE ${CachedProfile.TABLE_NAME} SET " + "${CachedProfile.COLUMN_PROFILE_PIC}=:profilePic")
    suspend fun updateProfilePic(profilePic: String?)

    @Query("UPDATE ${CachedProfile.TABLE_NAME} SET " + "${CachedProfile.COLUMN_AADHAR_NO}=:aadharNo")
    suspend fun updateAdhar(aadharNo: String?)

   // @Query("UPDATE ${CachedProfile.TABLE_NAME} SET " + "${CachedProfile.COLUMN_RESUME_UPDATED}=1")
    //suspend fun markResumeAsUpdated()

   /* @Query(
        "UPDATE ${CachedProfile.TABLE_NAME} SET " +
                "${CachedProfile.COLUMN_AADHAR_STATUS}=:aadharStatus," +
                "${CachedProfile.COLUMN_PAN_STATUS}=:panStatus"
    )
    suspend fun updateAadharPanResumeStatus(aadharStatus: String?, panStatus: String?)
*/
    @Query(
        "UPDATE ${CachedProfile.TABLE_NAME} SET " +
                "${CachedProfile.COLUMN_ACCOUNT_NO}=:accountNo," +
                "${CachedProfile.COLUMN_BANK_NAME}=:bankName," +
                "${CachedProfile.COLUMN_NAME_ON_BANK}=:nameOnBank," +
                "${CachedProfile.COLUMN_MOBILE_ON_BANK}=:mobileOnBank," +
                "${CachedProfile.COLUMN_IFSC}=:ifsc"
    )
    suspend fun updateBankData(
       accountNo: String?,
       bankName: String?,
       nameOnBank: String?,
       mobileOnBank: String?,
       ifsc: String?
    )

}
