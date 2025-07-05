package com.hostel.management.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.hostel.management.model.Room;
/*
 * This class is used to send the details of
 *  a Room to the client as a response for
 * the GET request for Room/Rooms
 */
public class RoomGetResponse {
    private int roomNumber;
    private int capacity;
    private int bedsAvailable;
    private int fee;

    public RoomGetResponse(){

    }

    public RoomGetResponse(Room room) {
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

    public List<RoomGetResponse> createList(List<Room> roomsList){
        List<RoomGetResponse> list = new ArrayList<>();

        if (! roomsList.isEmpty()) {
            for (Room room : roomsList) {
                RoomGetResponse responseRoom = new RoomGetResponse(room);
                list.add(responseRoom);
            }
            return list;
        }

        return list;
    }

    
}
