package com.hostel.management.dto.response;

import com.hostel.management.model.Student;
/*
 * This class is used to send the details of
 * newly created Student to the client as a response
 * for the POST req for Student resource
 */
public class StudentCreateResponse {
    
    private int studentId;
    private String name;
    private String contactNumber;
    private String collegeName;
    private int roomNumber;
    private String feeStatus;
    
    public StudentCreateResponse(Student student) {

        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.collegeName = student.getCollegeName();
        this.feeStatus = student.getFeeStatus();
        this.contactNumber = student.getContactNumber();
        this.roomNumber = student.getRoom().getRoomNumber();
        
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
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

    

}
