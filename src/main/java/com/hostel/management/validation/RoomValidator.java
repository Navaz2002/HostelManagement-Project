package com.hostel.management.validation;
/*
 * This class provides required validation methods to the RoomHandler
 * and RoomService in order to validate the details or fields of
 * Room sent by the client.
 * 
 * This provides following methods
 * 
 * public boolean isInteger(double num)
 * public boolean doesItMatch(String key, String value)
 * public boolean isPositive(int num)
 * 
 */
public class RoomValidator {
    /*
     * This checks provided double value is valid integer or not.
     * It returns true if its integer, false if not
     */
    public boolean isInteger(double number){
        return number % 1 == 0;
    }
    /*
     * This checks provided key string matches with provided value
     * or not
     * It returns true if matches, false if not.
     */
    public boolean doesItMatch(String key, String value) {
        return key.matches(value.trim());
    }
    /*
     * This checks provided int value is positive or not.
     * It returns true if its positive, false if not
     */
    public boolean isPositive(int num){
        return num > 0;
    }
}
