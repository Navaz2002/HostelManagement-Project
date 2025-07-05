package com.hostel.management.dto.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hostel.management.model.Student;
/*
 * This class is used to send the details
 * of Student/Students to the client as a 
 * response for the GET req for Student resource.
 */
public class StudentGetResponse {
    private int studentId;
    private String name;
    private String contactNumber;
    private String collegeName;
    private int roomNumber;
    private LocalDate joiningDate;
    private String feeStatus;

    public StudentGetResponse(Student obj, int roomNumber){
        
        this.studentId = obj.getStudentId();
        this.name = obj.getName();
        this.collegeName = obj.getCollegeName();
        this.contactNumber = obj.getContactNumber();
        this.feeStatus = obj.getFeeStatus();
        this.joiningDate = obj.getJoiningDate();
        this.roomNumber = roomNumber;

    }

    @Override
    public String toString() {
        return "Student Id "+this.studentId+", name "+this.name+", collegeName "+this.collegeName+" contactNumber"+this.contactNumber+", feeStatus "+this.feeStatus+", roomNumber "+this.roomNumber;
    }

    public StudentGetResponse(){
        
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

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(String feeStatus) {
        this.feeStatus = feeStatus;
    }

    public List<StudentGetResponse> getStudentGetResponseList(List<Student> studentsList){

        List<StudentGetResponse> list = new ArrayList<>();

        if (! studentsList.isEmpty()) {
            
            for (Student stu : studentsList) {
                StudentGetResponse st = new StudentGetResponse(stu, stu.getRoom().getRoomNumber());
                list.add(st);
            }
            return list;
        }

        return list;
    }
    
}
