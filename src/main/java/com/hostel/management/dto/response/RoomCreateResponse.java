package com.hostel.management.dto.response;

import com.hostel.management.model.Room;
/*
 * This class is used to send the details of 
 * a newly created Room to the client as a response
 * for the client's Post request for Room
 */
public class RoomCreateResponse {
    private int roomNumber;
    private int capacity;
    private int bedsAvailable;
    private int fee;

    public RoomCreateResponse(Room room) {

        this.roomNumber = room.getRoomNumber();
        this.capacity = room.getCapacity();
        this.bedsAvailable = room.getBedsAvailable();
        this.fee = room.getFee();
        
    }

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

}
