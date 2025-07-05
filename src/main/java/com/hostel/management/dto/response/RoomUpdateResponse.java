package com.hostel.management.dto.response;
/*
 * This class is used to send the updated 
 * details of a Room to the client as a response for the
 * PUT request for Room
 */
public class RoomUpdateResponse {
    
    private Integer roomNumber;
    private Integer fee;
    private Integer capacity;
    private Integer bedsAvailable;
    
    public Integer getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Integer getBedsAvailable() {
        return bedsAvailable;
    }

    public void setBedsAvailable(Integer bedsAvailable) {
        this.bedsAvailable = bedsAvailable;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    

}
