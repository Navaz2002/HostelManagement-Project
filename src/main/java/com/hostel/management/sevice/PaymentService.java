package com.hostel.management.sevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hostel.management.dao.PaymentDao;
import com.hostel.management.dao.StudentDao;
import com.hostel.management.dto.response.PaymentGetResponse;
import com.hostel.management.model.Payment;
import com.hostel.management.validation.PaymentValidator;
/*
 * This class is used to validate the details/fields it gets from the
 * PaymentHandlerclass by any (POST, PUT, GET,DELETE) request and interacts
 * with PaymentDao class methods to actully finish the task assigned
 * to it by the caller.
 * 
 */
public class PaymentService {

    private PaymentDao paymentDao = new PaymentDao();
    private PaymentValidator paymentValidator = new PaymentValidator();
    private PaymentGetResponse paymentGetResponse = new PaymentGetResponse();
    private StudentDao studentDao = new StudentDao();
    /*
     * 
     * This first checks all validations, if any validatiuon fails it sends fail
     * responses respectively.
     * 
     * If all validations complete then calls the PaymentDao method
     * fetchStudentPaymentRecordsById()  to actually get the list of Payment 
     * os student id.
     * 
     * Then it receives  the list of Payment records belonging to a provided
     * student id from PaymentDao and assigns that list to the PaymentGetResponse list
     * and sends that newly created list back to its caller.
     * 
     */
    public List<PaymentGetResponse> getPaymentById(double studentId) throws Exception {
        /*
        * this condition validates whether provided student id is
        * valid integer value or not.
        * It uses isInteger() of RoomValidator class.
        */
        if (! paymentValidator.isInteger(studentId)) {

            throw new IllegalArgumentException("student id must be an integer value.");

        }
        /*
         * this condition validates provided student id is a positive value 
         * or not.It uses isPositive() for validation
         * 
         */
        if (! paymentValidator.isPositive((int) studentId)) {

            throw new IllegalArgumentException("student id must be a positive value.");

        }
        /*
         * this checks if provided student id exists in the database or not.It uses
         * doesIdExist() for this validation.If does not exist  means its a invalid
         * student id so throws respective exception
         */
        // if (! studentDao.doesIdExist((int) studentId)) {
        //     System.out.println("callingf student exist");
        //     throw new IllegalArgumentException("student id does not exist.Provide valid id.");

        // }

        try {
            
            List<Payment> studentPaymentRecords = paymentDao.fetchStudentPaymentRecordsById((int) studentId);

            List<PaymentGetResponse> paymentRecords = paymentGetResponse.getStudentPaymentList(studentPaymentRecords);

            return paymentRecords;

            
        } catch (Exception e) {

            throw e;

        }
        
    }
    /*
     * This method receives the list of payment(all) from the 
     * paymentDao's method fetchAllStudentPaymentRecords(), then
     * sends that list back to its caller.
     */
    public List<PaymentGetResponse> getAllStudentPayments() throws Exception {

        try {
            
            List<Payment> paymentOfStudents = paymentDao.fetchAllStudentPaymentRecords();

            List<PaymentGetResponse> studentsPaymentResponse = paymentGetResponse.getStudentPaymentList(paymentOfStudents);

            return studentsPaymentResponse;

        } catch (Exception e) {
            
            throw e;
            
        }
    }


    
}
