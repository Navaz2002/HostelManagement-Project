package com.hostel.management.model;

import java.lang.ProcessBuilder.Redirect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {
    
    @Id
    private int studentId;
    private String name;
    private String contactNumber;
    private String collegeName;
    private LocalDate joiningDate;
    private String feeStatus;

    @OneToOne
    @JoinColumn(name = "roomNumber")
    private Room room;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Payment> paymentList;

    public Room getRoom() {
        return room;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public void setRoom(Room room) {
        this.room = room;
        room.addStudent(this);
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

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Override
    public String toString() {
        return "Student Id : "+this.studentId+ ", name : "+this.name+", contactNumber : "+this.contactNumber+", collegeName : " +this.collegeName+ ", joingDate : "+this.joiningDate+", roomNumber : "+this.getRoomNumber();
    }

    public int getRoomNumber(){
        Room room = this.getRoom();
        int roomNumber = room.getRoomNumber();
        return roomNumber;
    }

    public String getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(String feeStatus) {
        this.feeStatus = feeStatus;
    }

    /*
     * this setter method add the provided payment object 
     * to this student paymentList
     */
    public void addPayment(Payment payment) {

        if (this.paymentList == null) {

            this.paymentList = new ArrayList<>();

        }

        this.paymentList.add(payment);
        
    }


    





}
