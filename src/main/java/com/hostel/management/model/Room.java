package com.hostel.management.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Room {
    @Id
    private int roomNumber;
    private int capacity;
    private int bedsAvailable;
    private int fee;
    
    @OneToMany(mappedBy = "room")
    List<Student> studentList = new ArrayList<>();
    
    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBedsAvailable() {
        return bedsAvailable;
    }

    public void setBedsAvailable(int bedsAvailable) {
        this.bedsAvailable = bedsAvailable;
    }

    public int getFee() {
        return fee;           
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public List<Student> getStudentList() {
        return studentList;
    }


    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void addStudent(Student student){
        
        if (this.studentList == null) {
            this.studentList = new ArrayList<>();
        }
        this.studentList.add(student);
    }

    @Override
    public String toString() {
        return "Room number :"+this.roomNumber+", capacity :"+this.capacity+", bedsavailable :"+this.bedsAvailable+", fee :"+this.fee;
    }

    public void removeStudent(SessionFactory factory, int studentId) {
        Session session  = factory.openSession();
        if(this.studentList != null){
            List<Student> studentList = this.getStudentList();
            for (Student student : studentList) {
                if(student.getStudentId() == studentId){
                    this.studentList.remove(student);
                    break;
                }
            }
        }
    }
    

}
