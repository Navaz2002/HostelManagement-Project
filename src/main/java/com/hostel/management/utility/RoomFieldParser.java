package com.hostel.management.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hostel.management.dto.request.RoomUpdateRequest;


/*
 * this class is used to parse the individual fields like
 * capacity, fee, bedsAvailable, into java objects for 
 * methods like update, delete where we get specific fields 
 * for update or delete
 */
public class RoomFieldParser {


    /*
     * this method is used to parse the fee field of type JsonElement into
     * a java object, first it performs all validations to make sure
     * that the received fee field is of type integer, not null, not double, etc.
     * After successful validation,it creates an object of RoomUpdateRequest and assigns
     * the fee field to it and returns the object.
     * @parameters: JsonElement containing fee, JsonObject containing the update request
     * body ,the field fee.
     * @return: returns RoomUpdateRequest object if the field passes all validations,
     * or throws JsonParseException if any validation fails.
     */
    public static RoomUpdateRequest parseFee(JsonElement json, JsonObject jsonObj) throws JsonParseException{

        JsonElement fee = jsonObj.get("fee");
        if (jsonObj.has("fee")) {

            if (! fee.isJsonNull()) {

                if (! fee.getAsString().isBlank()) {

                    if (fee.isJsonPrimitive()) {

                        if (fee.getAsJsonPrimitive().isNumber()) {

                            if (fee.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("fee must be an integer value.");

                            }
                        } else {

                            throw new JsonParseException("fee field must be a number.");

                        }
                    } else {

                        throw new JsonParseException("fee field must be a primitive value.");

                    }
                } else {

                    throw new JsonParseException("fee field cannot be empty.");

                }
            } else {

                throw new JsonParseException("fee field cannot be null.");

            }
        } else {

            throw new JsonParseException("fee field is missing.");
        }

        RoomUpdateRequest feeUpdate = new RoomUpdateRequest();
        feeUpdate.setFee(fee.getAsInt());

        return feeUpdate;
    }
    /*
     * This method is used to parse the capacity as well as bedsAvailable field into
     * RoomUpdateRequest object necessary for updation.
     * 
     * Even though the method name contains only capacity but bedsAvailable is also
     * parsed along with it inside this method becauze when client needs to update
     * capacity reepective bedsAvailable also changes so client provided that as well
     * so we need to parse that as well.So instead of getting RoomUpdateRequest
     * object multiple time we can do only once.
     */
    public static RoomUpdateRequest parseCapacity(JsonElement jsonElement, JsonObject jsonObj){

        JsonElement capacity = jsonObj.get("capacity");
        if (jsonObj.has("capacity")) {

            if (! capacity.isJsonNull()) {

                if (capacity.isJsonPrimitive()) {

                    if (! capacity.getAsString().isBlank()) {

                        if (capacity.getAsJsonPrimitive().isNumber()) {

                            if (capacity.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("capacity field must be an integer value.");

                            }
                        } else {

                            throw new JsonParseException("capacity field must be a number.");

                        }
                    } else {

                        throw new JsonParseException("capacity field cannot be empty string.It must be a number.");

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

        JsonElement bedsAvailable = jsonObj.get("bedsAvailable");
        if (jsonObj.has("bedsAvailable")) {

            if (! bedsAvailable.isJsonNull()) {
    
                if (bedsAvailable.isJsonPrimitive()) {
                   
                    if (! bedsAvailable.getAsString().isBlank()) {
               
                        if (bedsAvailable.getAsJsonPrimitive().isNumber()) {

                            if (bedsAvailable.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("bedsAvailable field must be an integer value..");

                            }
                        } else {
                     
                            throw new JsonParseException("bedsAvailable field must be a number.");

                        }
                    } else {

                        throw new JsonParseException("bedsAvailable field cannot be a empty string.It must be a number.");

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

        RoomUpdateRequest capacityUpdate = new RoomUpdateRequest();
        capacityUpdate.setCapacity(capacity.getAsInt());
        capacityUpdate.setBedsAvailable(bedsAvailable.getAsInt());

        return capacityUpdate;
    }
}
