package com.hostel.management.validation;
/*
 * This class provides required validation methods to the PaymentHandler
 * and PaymentService in order to validate the details or fields of
 * Payment sent by the client.
 * 
 * This provides following methods
 * 
 * public boolean isInteger(double num)
 * public boolean isPositive(int num)
 * 
 */
public class PaymentValidator {
    

    /*
     * This checks provided double type value is inetegr or not.
     * It returns true if its integer, false if not.
     */
    public boolean isInteger(double number){
        return number % 1 == 0;
    }

    /*
     * This checks provided int value is positive or not.
     * It returns true if its positive, false if not
     */
    public boolean isPositive(int num){
        return num > 0;
    }

    
}
