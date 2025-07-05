package com.hostel.management.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/*
 * This class is used to send the details
 * of Student/Students requested by the client.
 * This is used to send the 
 */
import com.hostel.management.model.Payment;

public class PaymentGetResponse {

    private int studentId;
    private LocalDateTime payment_Date;
    private LocalDate paid_till;

    public PaymentGetResponse(Payment studentPayment) {
        
        this.studentId = studentPayment.getStudent().getStudentId();
        this.payment_Date = studentPayment.getPayment_Date();
        this.paid_till = studentPayment.getPaid_till();

    }

    public PaymentGetResponse(){
        
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public List<PaymentGetResponse> getStudentPaymentList(List<Payment> list){

        List<PaymentGetResponse> responseList = new ArrayList<>();

        if (list != null && ! list.isEmpty()) {
            
            for (Payment record :  list) {
                PaymentGetResponse obj = new PaymentGetResponse(record);
                responseList.add(obj);
            }

        }

        return responseList;
    }
    
}
