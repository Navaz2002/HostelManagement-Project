package com.hostel.management.validation;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
/*
 * This class provides required validation methods to the StudentHandler
 * and StudentService in order to validate the details or fields of
 * Student sent by the client.
 * 
 * This provides following methods
 * 
 *  public boolean isNameValid(String name)
 *  public boolean isContactValid(String contactNumber)
 *  public boolean isInteger(double num)
 *  public boolean isDateValid(String date)
 *  public boolean doesItMatch(String key, String value)
 *  public boolean isPositive(int num)
 * 
 * 
 */
public class StudentValidator {
    /*
     * This checks provided name string is valid or not.
     * Its valid if it has only alphabets(upper or lower or mix),
     * invalid if it has any other characters other than alphabets.
     * So it returns true if name string is valid, false if not.
     */
    public boolean isNameValid(String name) {
        return name != null && name.matches("[a-zA-Z ]+");
    }
    /*
     * This checks provided contact is valid or not,
     * contact is valid if it has exact 10 digits in it,invalid
     * if not.
     * It returns true if contact is valid, false if not
     */
    public boolean isContactValid(String contactNumber) {
        return contactNumber != null && contactNumber.matches("\\d{10}");
    }
    /*
     * This checks provided double type value is inetegr or not.
     * It returns true if its integer, false if not.
     */
    public boolean isInteger(double number){
        return number % 1 == 0;
    }
    /*
     * This checks provided date is valid or not.
     * It returns true if its valid, false if not
     */
    public boolean isDateValid(String date) {

        DateTimeFormatter format =  DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate.parse(date.trim(), format);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }

    }
    /*
     * This checks provided key string matches with provided value
     * or not
     * It returns true if matches, false if not.
     */
    public boolean doesItMatch(String key, String value) {

        return key.matches(value);

    }
    /*
     * This checks provided int value is positive or not.
     * It returns true if its positive, false if not
     */
    public boolean isPositive(int num){
        return num > 0;
    }
    
}
