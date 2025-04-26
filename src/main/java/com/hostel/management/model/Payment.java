package com.hostel.management.model;

import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Payment {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;
    private LocalDateTime paymentDateTime;
    
    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    @Override
    public String toString() {
        if (this != null) {
            int studentId = this.getStudent().getStudentId();
            return "payment id : "+this.paymentId+ ", payment date :"+this.paymentDateTime+", student id : "+studentId;
        }

        return "Payment is null.";
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    
}
