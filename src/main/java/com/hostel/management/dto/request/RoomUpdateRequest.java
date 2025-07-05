package com.hostel.management.dto.request;
/*
 * This class is used represent the fields of Room sent by
 * the client to update
 */
public class RoomUpdateRequest {
    
    private int fee;
    private int capacity;
    private int bedsAvailable;

    public int getFee() {
        return fee;
    }
    public void setFee(int fee) {
        this.fee = fee;
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

    
}
