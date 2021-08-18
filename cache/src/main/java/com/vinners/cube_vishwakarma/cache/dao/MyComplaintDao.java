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


    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME +
            " WHERE " + MyComplaintList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " AND "
            + MyComplaintList.COLUMN_SALES_AREA_ID + " IN (:said)" + "")
    List<MyComplaintList>getComplaintByIDAND(List<Integer> roid, List<Integer> said);

    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME +
            " WHERE " + MyComplaintList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " OR "
            + MyComplaintList.COLUMN_SALES_AREA_ID + " IN (:said)" + "")
    List<MyComplaintList>getComplaintByIDWithOR(List<Integer> roid, List<Integer> said);

    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME +
            " WHERE " + MyComplaintList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " OR "
            + MyComplaintList.COLUMN_SALES_AREA_ID + " IN (:said)" + " OR "
            + MyComplaintList.COLUMN_SUBAMIN_ID + " IN (:subadmin)" + "")
    List<MyComplaintList>getComplaintByIDWithSubAmin(List<Integer> roid, List<Integer> said,List<Integer> subadmin);

    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME +
            " WHERE " + MyComplaintList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " AND "
            + MyComplaintList.COLUMN_SALES_AREA_ID + " IN (:said)" + " AND "
            + MyComplaintList.COLUMN_SUBAMIN_ID + " IN (:subadmin)" + "")
    List<MyComplaintList>getComplaintByAllID(List<Integer> roid, List<Integer> said,List<Integer> subadmin);

    @Query("SELECT * FROM " + MyComplaintList.TABLE_NAME +
            " WHERE " + MyComplaintList.COLUMN_REGIONAL_OFFICE_ID + " IN (:roid)" + " AND "
            + MyComplaintList.COLUMN_SUBAMIN_ID + " IN (:subadmin)" + "")
    List<MyComplaintList>getComplaintByIDWithSubadminAndRO(List<Integer> roid, List<Integer> subadmin);




    @Query("SELECT DISTINCT " + MyComplaintList.COLUMN_REGIONAL_OFFICE + " FROM " + MyComplaintList.TABLE_NAME)
    List<String> getRegionalOffice();

    @Query("SELECT DISTINCT " + MyComplaintList.COLUMN_SALES_AREA + " FROM " + MyComplaintList.TABLE_NAME +
            " WHERE :parentRegionalOffice IS NULL OR " + OutletsList.COLUMN_REGIONAL_OFFICE + "=:parentRegionalOffice")
    List<String> getSalesArea(String parentRegionalOffice);


    @Query("DELETE FROM " + MyComplaintList.TABLE_NAME)
    void deleteAllMyComplaints();



}
