package com.hostel.management.sevice;

import java.util.List;

import com.hostel.management.Exception.DuplicateRoomException;
import com.hostel.management.dao.RoomDao;
import com.hostel.management.dto.request.RoomCreateRequest;
import com.hostel.management.dto.request.RoomUpdateRequest;
import com.hostel.management.dto.response.RoomCreateResponse;
import com.hostel.management.dto.response.RoomGetResponse;
import com.hostel.management.dto.response.RoomUpdateResponse;
import com.hostel.management.model.Room;
import com.hostel.management.validation.RoomValidator;
/*
 * This class is used to validate the details/fields it gets from the
 * RoomHandlerclass by any (POST, PUT, GET,DELETE) request and interacts
 * with RoomDao class methods to actully finish the task assigned
 * to it by the caller.
 * 
 * 
 */
public class RoomService {
    private RoomDao roomDao = new RoomDao();
    private RoomValidator roomvalidator = new RoomValidator();
    private RoomGetResponse roomGetResponse = new RoomGetResponse();

    /*
     * this method receives the room number from room handler class to get the
     * room(details) associated with this number.
     * 
     * First it validates whether provided room number is valid or not,
     * it performs all required validations before actually calling the dao's
     * method to get the room.
     * 
     * After all validation it finally calls the fetchRoomByNumber() method of Dao
     * to actually get the room.
     * 
     * @returns RoomGetResponse object containing the excat details of room associated
     * with this number.
     */
    public RoomGetResponse getRoomByNumber(double roomNumber) {
        /*
         * this condition validates whether provided room number is
         * valid integer value or not.
         * It uses isInteger() of RoomValidator class.
         */
        if (! roomvalidator.isInteger(roomNumber)) {
            throw new IllegalArgumentException("Room number must be an integer value.Floating values are not allowed.");
        }
        /*
         * this condition checks whether provided room nuber with room
         * exists in the database or not.
         * It uses doesRoomExist() for validation
         */
        if (! roomDao.doesRoomExist((int) roomNumber)) {
            throw new IllegalArgumentException("Room number "+(int)roomNumber+" does not exist in the database.Provide valid room number.");
        }

        try {

            Room room = roomDao.fetchRoomByNumber((int) roomNumber);

            RoomGetResponse responseRoom = new RoomGetResponse(room);

            return responseRoom;

        } catch (Exception e) {
            throw e;
        }
    }
    /*
     * this method is used to get all the rooms from database.
     * 
     * @returns the list of RoomGetResponse containing all rooms
     * with all details of them
     */
    public List<RoomGetResponse> getAllRooms() {

        try {

            List<Room> roomsList = roomDao.fetchAllRooms();
            List<RoomGetResponse> roomsResponselist = roomGetResponse.createList(roomsList);
            return roomsResponselist;

        } catch (Exception e) {

            throw e;

        }
    }

    /*
     * this method is used to get all available rooms from the database.
     * 
     * @returns the list of RoomGetResponse containing all available
     * rooms.
     */
    public List<RoomGetResponse> getAvailableRooms() {

        try {

            List<Room> roomsAvailableList = roomDao.fetchRoomsByAvailability();
            List<RoomGetResponse> avilableRoomsResponse = roomGetResponse.createList(roomsAvailableList);
            return avilableRoomsResponse;

        } catch (Exception e) {
            throw e;
        }
    }
    /*
     * this method is used to get all unavailable rooms
     * from tre database.
     * 
     * @returns the list of RoomGetResoponse conataining all
     * unavailable rooms
     */
    public List<RoomGetResponse> getUnavailableRooms() {

        try {

            List<Room> unAvailableRooms = roomDao.fetchUnAvailableRooms();
            List<RoomGetResponse> unAvailableRoomsResponse = roomGetResponse.createList(unAvailableRooms);
            return unAvailableRoomsResponse;

        } catch (Exception e) {

            throw e;

        }
    }
    /*
     * this method is used to get all available rooms with provided capacity from
     * the database.
     * 
     * @returns the list of RoomGetResponse containing all available rooms with provided
     * capacity.
     */
    public List<RoomGetResponse> getAvailableRoomsOfCapacity(int capacity) {

        try {

            List<Room> availableCapacityRooms = roomDao.fetchAvailableRoomsOfCapacity(capacity);
            List<RoomGetResponse> avilableroomsOfCapacity = roomGetResponse.createList(availableCapacityRooms);
            return avilableroomsOfCapacity;

        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * this method is used to add the room to the databse.It does not actually add
     * the room but performs all validations before calling dao method which actually 
     * adds the room to the database.
     * 
     * It receives the CreateRoomRequest object containing the required fields
     * required for adding to the databse.It validates roomnumber, capacity,
     * bedsavaialble, fee, 
     * 
     * after completing validations it calls addRoom() of RoomDAO for
     * actually adding this room to the database.
     * 
     * @returns the RoomCreateResponse object containing the added room details
     */
    public RoomCreateResponse addRoom(RoomCreateRequest roomRequest) {
        /*
         * this condition validates provided room number is a positive value 
         * or not.It uses isPositive() for validation
         */
        if (! roomvalidator.isPositive(roomRequest.getRoomNumber())) {
            throw new IllegalArgumentException("roomNumber must be a positive number.");
        }
          /*
         * this condition validates provided capacity of room is a positive value 
         * or not.It uses isPositive() for validation
         */
        if (! roomvalidator.isPositive(roomRequest.getCapacity())) {
            throw new IllegalArgumentException("capacity must be a positive number.");
        }
          /*
         * this condition validates provided bedsavialable field is a positive value 
         * or not.It uses isPositive() for validation
         */
        if (! roomvalidator.isPositive(roomRequest.getBedsAvailable())) {
            throw new IllegalArgumentException("bedsAvailable must be a positive number.");
        }
          /*
         * this condition validates provided room fee field  is a positive value 
         * or not.It uses isPositive() for validation
         */
        if (! roomvalidator.isPositive(roomRequest.getFee())) {
            throw new IllegalArgumentException("fee must be a positive number.");
        }
        /*
         * this condition validates provided room number already exists in the
         * database or not,it uses doesRoomExist() for validation.
         * If room already exists means provided room number is duplicate 
         */
        if (roomDao.doesRoomExist(roomRequest.getRoomNumber())) {
            throw new DuplicateRoomException("Room with roomNumber "+ roomRequest.getRoomNumber() + " already exists in the database.");
        }

        try {

            Room room = new Room();
            room.setRoomNumber(roomRequest.getRoomNumber());
            room.setCapacity(roomRequest.getCapacity());
            room.setBedsAvailable(roomRequest.getBedsAvailable());
            room.setFee(roomRequest.getFee());

            //calling addRoom() for adding this room to the database.
            room = roomDao.addRoom(room);
            RoomCreateResponse roomResponse = new RoomCreateResponse(room);
            return roomResponse;

        } catch (Exception e) {
            throw e;
        }
    }
    /*
     * this method receives updateRoomRequest object, room number
     * for updating the room's fee.
     * 
     * It validates everything required before calling dao's method
     * 
     * @returns the RoomUpdateResponse object containing the updated room
     * details
     */
    public RoomUpdateResponse updateFee(RoomUpdateRequest feeUpdate, double roomNumber) throws Exception {
        /*
         * this validates whether provided new fee is a positive
         * value or not.Uses isPositive() for validation
         */
        if (! roomvalidator.isPositive(feeUpdate.getFee())) {
            throw new IllegalArgumentException("fee field must be a positive number.");
        }
        /*
         * this validates provided room number is a valid integer 
         * or not.Uses isIntger() for validation.
         */
        if (! roomvalidator.isInteger(roomNumber)) {
            throw new IllegalArgumentException("roomNumber field must be an integer value.");
        }
          /*
         * this condition validates provided room number already exists in the
         * database or not,it uses doesRoomExist() for validation.
         * If room already exists means provided room number is duplicate 
         */
        if (! roomDao.doesRoomExist((int) roomNumber)) {
            throw new IllegalArgumentException("roomNumber  "+(int)roomNumber+ " does not exist in the database.");
        }

        try {
            //calling updateFeeOfRoom() to update room's fee.
            Room room = roomDao.updateFeeOfRoom(feeUpdate.getFee(), (int)roomNumber);
            if (room != null) {

                RoomUpdateResponse feeUpdateResponse = new RoomUpdateResponse();
                feeUpdateResponse.setFee(room.getFee());
                return feeUpdateResponse;
    
            } 

            throw new Exception("Unexpected error,update was not successful.");
            
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * this method receives updateRoomRequest object containing the fields capacity,
     * beds available for capacity update, also recieves the room number.
     * 
     * It performs all required validations then calls the updateCapacity() of dao
     * for updating the rooms capacity in the database.
     * 
     * @returns the RoomUpdateresponse object containing the updated room object
     * with necessary fields. 
     */
    public RoomUpdateResponse updateCapacity(RoomUpdateRequest capacityUpdate, double roomNumber) throws Exception {

        /*
         * this check ensures that we have got a positive capacity field, if not 
         * it throws illegalArguementException
         */
        if (! roomvalidator.isPositive(capacityUpdate.getCapacity())) {
            throw new IllegalArgumentException("capacity field must be a positive value");
        }

        /*
         * this check ensure that we have got a positive bedsAvailable field, if not it throws
         * throws illegalArguementException
         */
        if (! roomvalidator.isPositive(capacityUpdate.getBedsAvailable())) {
            throw new IllegalArgumentException("bedsAvailable  field must be a positive value");
        }

         /*
         * this check ensure that we have got a integer roomNumber field, if not it throws
         * throws illegalArguementException
         */
        if (! roomvalidator.isInteger((int)roomNumber)) {
            throw new IllegalArgumentException("roomNumber field must be a positive value");
        }

        /*
         * this check ensures that the roomNumber we got valid, if its not valid 
         * it throws exception
         */
        if (! roomDao.doesRoomExist((int) roomNumber)) {
            throw new IllegalArgumentException("roomNumber "+(int)roomNumber +" does not exist in the database.");
        }
        /*
         * this check makes sure that the new bedsAvailable value is in 
         *  the range 0 - capacity , or if the value exceeds the capacity value it throws
         * exception
         */
        if (  capacityUpdate.getBedsAvailable()  < 0  ||   capacityUpdate.getBedsAvailable() > capacityUpdate.getCapacity() ) {
            throw new IllegalArgumentException("bedsAvailabel field value must be less than or equal to the capacity value.It must be in the range (1-capacity).");
        }

        try {
            //calling the updateCapacity() of RoomDao to update capacity
            Room room = roomDao.updateCapacity(capacityUpdate.getCapacity(), capacityUpdate.getBedsAvailable(), (int) roomNumber);

            /*
             * if the ipdate was not successful due to any error 
             */
            if (room == null) {

                throw new Exception("Unexpected error,update was not successful.Try later");

            }

            RoomUpdateResponse capacityUpdateResponse = new RoomUpdateResponse();
            capacityUpdateResponse.setBedsAvailable(room.getBedsAvailable());
            capacityUpdateResponse.setCapacity(room.getCapacity());
            capacityUpdateResponse.setRoomNumber(room.getRoomNumber());
            return capacityUpdateResponse;

        } catch (Exception e) {
            throw e;
        }

    }

    /*
     * this method receives the room number for removing the room
     * from the database.
     * 
     * It performs all required validations, then calls deleteRoom()
     * of dao to delete the room associated with this number from database.
     * 
     */
    public void removeRoom(double roomNumber) throws Exception {

         /*
         * this check ensure that we have got integer roomNumber field, if not it throws
         * throws illegalArguementException
         */
        if (! roomvalidator.isInteger(roomNumber)) {
            throw new IllegalArgumentException("Room number field must be an integer value.floating values are not allowed.");
        }
        /*
         * this check validates provided room number is a positive
         * value or not.
         */
        if (! roomvalidator.isPositive((int) roomNumber)) {
            throw new IllegalArgumentException("Room number field must be a positive value.");
        }

       

        try {
            /*
             * this check validates provided room number with room exists
             * in the database or not.If not then its a invalid room number provided.
             */
            if (! roomDao.doesRoomExist((int) roomNumber)) {
                throw new IllegalArgumentException("Room number "+(int)roomNumber+" does not exist in the database.");
            }
            /*
             * this check validates provided room number with room has 
             * students in it or not.
             * 
             */
            if (roomDao.doesRoomHaveStudents((int) roomNumber)) {
                throw new IllegalStateException("Cannot delete a room that has students assigned");
            }
            /*
             * calling deleteRoom() to actully delete this room from database.
             * deletes room only if there are no students in this room.
             */
            if (! roomDao.deleteRoom((int) roomNumber)) {
                throw new Exception("Deletion failed due to unexpected error.");
            }


        } catch (Exception e) {
            throw e;
        }


    }
}
