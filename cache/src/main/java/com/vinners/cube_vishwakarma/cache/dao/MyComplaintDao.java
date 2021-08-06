package com.vinners.cube_vishwakarma.cache.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList;
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList;

import java.util.List;

@Dao
public interface MyComplaintDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMyComplaints(List<MyComplaintList> complaints);

    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME + " WHERE " + MyComplaintList.COLUMN_OUTLET_ID + " = :outletid")
    MyComplaintList getMyComplaints(String outletid);

    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME)
    List<MyComplaintList> getAllComplaints();


//@Query("SELECT * FROM " + OutletsList.TABLE_NAME +
//        " WHERE " + OutletsList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " OR " +
//        OutletsList.COLUMN_SALES_AREA_ID + " IN (:said)" +"")
//
//    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME +
//            " WHERE " + MyComplaintList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " AND "
//            + MyComplaintList.COLUMN_SALES_AREA_ID + " IN (:said)" + "")
////List<OutletsList>getOutletsByID(@Nullable String roid, @Nullable String said);
//    List<MyComplaintList>getOutletsByID(List<Integer> roid, List<Integer> said);
//
//    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME +
//            " WHERE " + MyComplaintList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " OR "
//            + MyComplaintList.COLUMN_SALES_AREA_ID + " IN (:said)" + "")
//    List<MyComplaintList>getOutletsByIDWithOR(List<Integer> roid, List<Integer> said);


    @Query("SELECT DISTINCT " + MyComplaintList.COLUMN_REGIONAL_OFFICE + " FROM " + MyComplaintList.TABLE_NAME)
    List<String> getRegionalOffice();

    @Query("SELECT DISTINCT " + MyComplaintList.COLUMN_SALES_AREA + " FROM " + MyComplaintList.TABLE_NAME +
            " WHERE :parentRegionalOffice IS NULL OR " + OutletsList.COLUMN_REGIONAL_OFFICE + "=:parentRegionalOffice")
    List<String> getSalesArea(String parentRegionalOffice);


    @Query("DELETE FROM " + MyComplaintList.TABLE_NAME)
    void deleteAllMyComplaints();



}
