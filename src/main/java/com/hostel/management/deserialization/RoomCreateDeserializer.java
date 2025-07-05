package com.hostel.management.deserialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hostel.management.dto.request.RoomCreateRequest;
/*
 * This class conatins the method which is used to validate
 * and parse the Room fields sent by the client to create
 * the room record in the database.
 * 
 */
public class RoomCreateDeserializer implements JsonDeserializer<RoomCreateRequest>{
    @Override
    /*
     * this method validates all the fields present in the JsonElement
     * then parses into RommCreateRequest object
     */
    public RoomCreateRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();

        /*
         * type-cheking roomNumber field
         */
        JsonElement roomNumber = jsonObj.get("roomNumber");
        if (jsonObj.has("roomNumber")) {

            if (! roomNumber.isJsonNull()) {

                if (roomNumber.isJsonPrimitive()) {

                    if (! roomNumber.getAsString().isBlank()) {

                        if (roomNumber.getAsJsonPrimitive().isNumber()) {

                            if (roomNumber.getAsDouble() % 1 != 0){

                                throw new JsonParseException("roomNumber must be an integer.Floating values are not allowed.");

                            }
                        } else {

                            throw new JsonParseException("roomNumber must be a number.");

                        }
                    } else {

                        throw new JsonParseException("roomNumber field cannot be an empty string.It must be a number.");

                    }
                } else {

                    throw new JsonParseException("roomNumber field must be a primitive.");

                }
            } else {

                throw new JsonParseException("roomNumber field cannot be null.");

            }
        } else {

            throw new JsonParseException("roomNumber field is missing.");
        }

        /*
         * type checking capacity field
         */
        JsonElement capacity = jsonObj.get("capacity");
        if (jsonObj.has("capacity")) {

            if (! capacity.isJsonNull()) {

                if (capacity.isJsonPrimitive()) {

                    if (! capacity.getAsString().isBlank()) {

                        if (capacity.getAsJsonPrimitive().isNumber()) {

                            if (capacity.getAsDouble() % 1 != 0) {
                                
                                throw new JsonParseException("capacity must be an integer value.");
                            }
                        } else {

                            throw new JsonParseException("capacity must be a number.");

                        }
                    } else {

                        throw new JsonParseException("capacity cannot be an empty string.It must be a number.");

                    }
                } else {

                    throw new JsonParseException("capacity field must be a primitive.");

                }
            } else {

                throw new JsonParseException("capacity field cannot be null.");

            }
        } else {

            throw new JsonParseException("capacity field is missing.");
        }

        /*
         * type-checking bedsAvailable field
         */
        JsonElement bedsAvailable = jsonObj.get("bedsAvailable");
        if (jsonObj.has("bedsAvailable")) {

            if (! bedsAvailable.isJsonNull()) {

                if (bedsAvailable.isJsonPrimitive()) {

                    if (! bedsAvailable.getAsString().isBlank()) {

                        if (bedsAvailable.getAsJsonPrimitive().isNumber()) {

                            if (bedsAvailable.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("bedsAvailable field must be an integer value.");

                            }
                        } else {

                            throw new JsonParseException("bedsAvailable field must be number.");

                        }

                    } else {

                        throw new JsonParseException("bedsAvailable field cannot be an empty string.It must be a number.");

                    }
                } else {

                    throw new JsonParseException("bedsAvailable field must be a primitive.");

                }
            } else {

                throw new JsonParseException("bedsAvailable field cannot be null.");

            }
        } else {

            throw new JsonParseException("bedsAvailable field is missing.");
        }

        /*
         * type-checking fee field
         */
        JsonElement fee = jsonObj.get("fee");
        if (jsonObj.has("fee")) {

            if (! fee.isJsonNull()) {
               
                if (fee.isJsonPrimitive()) {

                    if ( ! fee.getAsString().isBlank()) {

                        if (fee.getAsJsonPrimitive().isNumber()) {

                            if (fee.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("fee must be an integer value.");

                            }
                        } else {

                            throw new JsonParseException("fee field must be a number.");

                        }
                    } else {

                        throw new JsonParseException("fee field cannot be an empty string.It must be a number.");

                    }
                } else {

                    throw new JsonParseException("fee field must be a primitive.");

                }
            } else {

                throw new JsonParseException("fee field cannot be null.");

            }
        } else {

            throw new JsonParseException("fee field is missing.");
        }

        /*
         * creating an object of CreateRoomRequest,this object is further sent to service layer for 
         * further validations,after correct validation an object of room will be created in the 
         * RoomDao class with exact details of this request object ,then room object will be 
         * saved to the database
         */
        RoomCreateRequest roomRequest = new RoomCreateRequest();
        roomRequest.setRoomNumber(roomNumber.getAsInt());
        roomRequest.setCapacity(capacity.getAsInt());
        roomRequest.setBedsAvailable(bedsAvailable.getAsInt());
        roomRequest.setFee(fee.getAsInt());

        return roomRequest;
    }
    
}
