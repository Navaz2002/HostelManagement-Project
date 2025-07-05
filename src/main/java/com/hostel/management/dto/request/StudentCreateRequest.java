package com.hostel.management.dto.request;
/*
 * This class is used to represent the details of student
 * sent by the client to create the student record(object) in the
 * database
 */
public class StudentCreateRequest {
    
    private int StudentId;
    private String name;
    private String contactNumber;
    private int roomNumber;
    private String collegeName;
    private int paidForMonths;

    public int getStudentId() {
        return StudentId;
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public int getPaidForMonths() {
        return paidForMonths;
    }

    public void setPaidForMonths(int paidForMonths) {
        this.paidForMonths = paidForMonths;
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

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int rooNumber) {
        this.roomNumber = rooNumber;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    
}
