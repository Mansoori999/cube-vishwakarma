package com.vinners.cube_vishwakarma.data.models.outlets;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by himanshu on 5/9/17.
 */

@Entity(tableName = Outlet.TABLE_NAME)
public class Outlet implements Parcelable {

    @Ignore
    public static final String TABLE_NAME = "outlets";
    @Ignore
    public static final String COLUMN_OUTLET_ID = "outletID";
    @Ignore
    public static final String COLUMN_SALES_AREA = "salesArea";
    @Ignore
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    @Ignore
    public static final String COLUMN_REGIONAL_OFFICE = "regionalOffice";
    @Ignore
    public static final String COLUMN_OUTLET = "outletName";
    @Ignore
    public static final String COLUMN_CUSTOMER_CODE = "customerCode";
    @Ignore
    public static final String COLUMN_LOCATION = "location";


    public static final Creator<Outlet> CREATOR = new Creator<Outlet>() {
        @Override
        public Outlet createFromParcel(Parcel source) {
            return new Outlet(source);
        }

        @Override
        public Outlet[] newArray(int size) {
            return new Outlet[size];
        }
    };
    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private String outletID;
    @SerializedName("salesarea")
    private String salesArea;
    @SerializedName("district")
    private String district;
    @SerializedName("location")
    private String location;
    private String dealerName;
    @SerializedName("name")
    private String outletName;
    @SerializedName("regionaloffice")
    private String regionalOffice;
    @SerializedName("contactno")

    private String phoneNumber;
    @SerializedName("category")
    private String category;


    @SerializedName("customercode")
    private String customerCode;
    @SerializedName("resv")
    private String resv;
    @SerializedName("contactname")
    private String contactName;
    @SerializedName("ccoms")
    private String ccoms;
    @SerializedName("ccnohsd")

    private String ccnohsd;
    @SerializedName("gps")
    private String locationCordinates;
    @SerializedName("gpsaddress")
    private String streetName;
    @SerializedName("pic")
    private String imageUrl;
    @SerializedName("mobile1")
    private String mobileFirst;
    @SerializedName("mobile2")
    private String mobilesecond;
    @SerializedName("landline")
    private String landLineNumber;

    @SerializedName("primaryemail")
    private String primaryEmailId;

    @SerializedName("email")
    private String secondaryEmailId;

    public Outlet() {
    }

    protected Outlet(Parcel in) {
        this.outletID = in.readString();
        this.salesArea = in.readString();
        this.district = in.readString();
        this.location = in.readString();
        this.outletName = in.readString();
        this.dealerName = in.readString();
        this.regionalOffice = in.readString();
        this.phoneNumber = in.readString();
        this.category = in.readString();
        this.customerCode = in.readString();
        this.resv = in.readString();
        this.ccoms = in.readString();
        this.ccnohsd = in.readString();
        this.locationCordinates = in.readString();
        this.streetName = in.readString();
        this.imageUrl = in.readString();
        this.contactName = in.readString();
        this.mobileFirst = in.readString();
        this.mobilesecond = in.readString();
        this.landLineNumber = in.readString();
        this.secondaryEmailId = in.readString();
        this.primaryEmailId = in.readString();
    }

    public String getMobileFirst() {
        return mobileFirst;
    }

    public void setMobileFirst(String mobileFirst) {
        this.mobileFirst = mobileFirst;
    }

    public String getMobilesecond() {
        return mobilesecond;
    }

    public void setMobilesecond(String mobilesecond) {
        this.mobilesecond = mobilesecond;
    }

    public String getLandLineNumber() {
        return landLineNumber;
    }

    public void setLandLineNumber(String landLineNumber) {
        this.landLineNumber = landLineNumber;
    }

    public String getSecondaryEmailId() {
        return secondaryEmailId;
    }

    public void setSecondaryEmailId(String secondaryEmailId) {
        this.secondaryEmailId = secondaryEmailId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocationCordinates() {
        return locationCordinates;
    }

    public void setLocationCordinates(String locationCordinates) {
        this.locationCordinates = locationCordinates;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getOutletID() {
        return outletID;
    }

    public void setOutletID(String outletID) {
        this.outletID = outletID;
    }

    public String getSalesArea() {
        return salesArea;
    }

    public void setSalesArea(String salesArea) {
        this.salesArea = salesArea;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return getOutletName();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getRegionalOffice() {
        return regionalOffice;
    }

    public void setRegionalOffice(String regionalOffice) {
        this.regionalOffice = regionalOffice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getResv() {
        return resv;
    }

    public void setResv(String resv) {
        this.resv = resv;
    }

    public String getCcoms() {
        return ccoms;
    }

    public void setCcoms(String ccoms) {
        this.ccoms = ccoms;
    }

    public String getCcnohsd() {
        return ccnohsd;
    }

    public void setCcnohsd(String ccnohsd) {
        this.ccnohsd = ccnohsd;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.outletID);
        dest.writeString(this.salesArea);
        dest.writeString(this.district);
        dest.writeString(this.location);
        dest.writeString(this.outletName);
        dest.writeString(this.dealerName);
        dest.writeString(this.regionalOffice);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.category);
        dest.writeString(this.customerCode);
        dest.writeString(this.resv);
        dest.writeString(this.ccoms);
        dest.writeString(this.ccnohsd);
        dest.writeString(this.streetName);
        dest.writeString(this.locationCordinates);
        dest.writeString(this.imageUrl);
        dest.writeString(this.contactName);
        dest.writeString(this.mobileFirst);
        dest.writeString(this.mobilesecond);
        dest.writeString(this.landLineNumber);
        dest.writeString(this.secondaryEmailId);
        dest.writeString(this.primaryEmailId);
    }


    public String getPrimaryEmailId() {
        return primaryEmailId;
    }

    public void setPrimaryEmailId(String primaryEmailId) {
        this.primaryEmailId = primaryEmailId;
    }
}
