package com.hostel.management.dto.request;
/*
 * This class is used to represent the room details sent
 * by the client to create the room in database.
 * 
 */
public class RoomCreateRequest {
    
    private int roomNumber;
    private int capacity;
    private int bedsAvailable;
    private int fee;

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
