package com.hostel.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Payment {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int paymentId;
    private LocalDateTime payment_Date;
    private LocalDate paid_till;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    /*
     * This constructor recieves the student object , months.
     * Student object is saved into this payment object
     * months defines how many months fee student paid.
     */
    public Payment(Student student, int months) {

        this.paid_till = student.getJoiningDate().plusMonths(months);
        this.payment_Date = LocalDateTime.now();
        this.student = student; 

    }

    public Payment(){
        
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getPayment_Date() {
        return payment_Date;
    }

    public void setPayment_Date(LocalDateTime payment_Date) {
        this.payment_Date = payment_Date;
    }

    public LocalDate getPaid_till() {
        return paid_till;
    }

    public void setPaid_till(LocalDate paid_till) {
        this.paid_till = paid_till;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

   

   

    
    
}
