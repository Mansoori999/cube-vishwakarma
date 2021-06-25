package com.vinners.cube_vishwakarma.cache.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vinners.cube_vishwakarma.cache.OutletsType;
import com.vinners.cube_vishwakarma.cache.entities.CachedOutlets;
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList;

import java.util.List;

import static android.transition.Fade.IN;


/**
 * Created by Himanshu on 6/22/2018.
 */
@Dao
public interface OutletsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOutlet(List<OutletsList> complaints);

    @Query("SELECT * FROM " + OutletsList.TABLE_NAME + " WHERE " + OutletsList.COLUMN_OUTLET_ID + " = :outletid")
    OutletsList getOutlets(String outletid);

    @Query("SELECT * FROM " + OutletsList.TABLE_NAME)
    List<OutletsList> getAllOutlets();


//@Query("SELECT * FROM " + OutletsList.TABLE_NAME +
//        " WHERE " + OutletsList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " OR " +
//        OutletsList.COLUMN_SALES_AREA_ID + " IN (:said)" +"")

@Query("SELECT * FROM " + OutletsList.TABLE_NAME +
            " WHERE " + OutletsList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " AND "
        + OutletsList.COLUMN_SALES_AREA_ID + " IN (:said)" + "")
    List<OutletsList>getOutletsByID(@Nullable String roid, @Nullable String said);


//    @Query("SELECT * FROM " + OutletsList.TABLE_NAME +
//            " WHERE ( :regionalOffice IS NULL OR " + OutletsList.COLUMN_REGIONAL_OFFICE + " IN (':regionalOffice')" + ") AND " +
//            "( :salesArea IS NULL OR " + OutletsList.COLUMN_SALES_AREA + " IN (':salesArea')" + ") ")
//    @Nullable String searchText
//    AND " +
//
//            "( :searchText IS NULL OR " + OutletsList.COLUMN_OUTLET_NAME + " like '%' || :searchText || '%' OR "
//            + OutletsList.COLUMN_CUSTOMER_CODE + " like '%' || :searchText || '%' OR "
//            + OutletsList.COLUMN_REGIONAL_OFFICE + " like '%' || :searchText || '%' OR "
//            + OutletsList.COLUMN_SALES_AREA + " like '%' || :searchText || '%' " + " )
    /**
     * Returns All Regional Office List for Complaint Filters
     */

    @Query("SELECT DISTINCT " + OutletsList.COLUMN_REGIONAL_OFFICE + " FROM " + OutletsList.TABLE_NAME)
    List<String> getRegionalOffice();

    @Query("SELECT DISTINCT " + OutletsList.COLUMN_SALES_AREA + " FROM " + OutletsList.TABLE_NAME +
            " WHERE :parentRegionalOffice IS NULL OR " + OutletsList.COLUMN_REGIONAL_OFFICE + "=:parentRegionalOffice")
    List<String> getSalesArea(String parentRegionalOffice);


    @Query("DELETE FROM " + OutletsList.TABLE_NAME)
    void deleteAllOutlets();

//    @Query("SELECT * FROM " + OutletsType.TABLE_NAME + " ORDER BY " + OutletsType.COLUMN_TYPE)
//    List<OutletsType> getOutletsTypes();
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    void insertOutletsTypes(List<OutletsType> outletsTypeList);


}
