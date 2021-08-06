package com.vinners.cube_vishwakarma.cache.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList;
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponseDataItem;
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList;

import java.util.List;

@Dao
public interface DashboardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDashboardData(DashBoardResponseDataItem dashboardList);


    @Query("SELECT * FROM " + DashBoardResponseDataItem.TABLE_NAME)
    DashBoardResponseDataItem getDashboardAllData();


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




    @Query("DELETE FROM " + DashBoardResponseDataItem.TABLE_NAME)
    void deleteDashboardData();



}
