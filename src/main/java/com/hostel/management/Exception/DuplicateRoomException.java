package com.hostel.management.Exception;

/*
 * This exception raises when you try to create a new room object 
 *  (for(POST method)) that already exists in the database
 */
public class DuplicateRoomException extends RuntimeException{
    public DuplicateRoomException(String message){
        super(message);
    }
}
