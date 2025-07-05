package com.hostel.management.dto.response;

import java.time.LocalDate;

import com.hostel.management.model.Student;
/*
 * This class is used to send the updated details
 * (along with other not updated) to the client as a
 * response for the PUT req for Student resource.
 */
public class StudentUpdateResponse {
    
    private Integer studentId;
    private String name;
    private String collegeName;
    private String contactNumber;
    private Integer roomNumber;
    private LocalDate joiningDate;
    private String feeStatus;

    public StudentUpdateResponse(Student student) {

        this.studentId = student.getStudentId();
        this.name = student.getName();
        this.collegeName = student.getCollegeName();
        this.contactNumber = student.getContactNumber();
        this.feeStatus = student.getFeeStatus();
        this.roomNumber = student.getRoomNumber();
        
    }

    public String getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(String feeStatus) {
        this.feeStatus = feeStatus;
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

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

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

    public LocalDate getJoiningDate() {
        return joiningDate;
    }
    
    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    

}
