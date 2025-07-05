package com.hostel.management.Exception;

/*
 * This exception raises when you try to create a new student which
 * already exists in the database
 */
public class DuplicatestudentException extends RuntimeException{
    public DuplicatestudentException(String message){
        super(message);
    }
}
