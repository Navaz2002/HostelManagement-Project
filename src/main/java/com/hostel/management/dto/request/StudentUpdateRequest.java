package com.hostel.management.dto.request;
/*
 * This class is used to represent the fields of Student
 * sent by the client to update 
 */
public class StudentUpdateRequest {
    
   
    private String contactNumber;
    private int roomNumber;
    private String feeStatus;
    private int paidforMonths ;

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(String feeStatus) {
        this.feeStatus = feeStatus;
    }

    public int getPaidforMonths() {
        return paidforMonths;
    }

    public void setPaidforMonths(int paidforMonths) {
        this.paidforMonths = paidforMonths;
    }




    
}
