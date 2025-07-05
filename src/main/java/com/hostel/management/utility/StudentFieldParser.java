package com.hostel.management.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hostel.management.dto.request.StudentUpdateRequest;
/*
 * this class is used to parse the individual fields like
 * contact, fee, paidForMonths(not a property of Student), room number, into java objects for 
 * methods like update, delete of Student resource where we get specific fields 
 * for update or delete
 */
public class StudentFieldParser {
    /*
     * This parse the contact field and returns the StudentUpdateRequest containing the
     * contact provided to it.
     */
    public static StudentUpdateRequest parseContact(JsonElement element, JsonObject jsonObj) {

        JsonElement contactNum = jsonObj.get("contactNumber");

        if (jsonObj.has("contactNumber")) {

            if (! contactNum.isJsonNull()) {

                if (contactNum.isJsonPrimitive()) {

                    if (! contactNum.getAsString().isBlank()) {
                        
                    } else {

                        throw new JsonParseException("contact number field cannot be an empty.");

                    }
                } else {

                    throw new JsonParseException("contact number field must be a primitive value.");

                }
            } else {

                throw new JsonParseException("contact number field cannot be null.");

            }
        } else {

            throw new JsonParseException("contact number field is missing.");
        }

        StudentUpdateRequest contactUpdate = new StudentUpdateRequest();
        contactUpdate.setContactNumber(contactNum.getAsString());

        return contactUpdate;

    }
    /*
     * This method parses and returns the StudentUpdateRequest
     * containing the contact provided to it.
     */
    public static StudentUpdateRequest parseRoomField(JsonElement element, JsonObject jsonObj) {

        JsonElement roomNumber = jsonObj.get("roomNumber");

        if (jsonObj.has("roomNumber")) {

            if (! roomNumber.isJsonNull()) {

                if (roomNumber.isJsonPrimitive()) {

                    if (! roomNumber.getAsString().isBlank()) {

                        if (roomNumber.getAsJsonPrimitive().isNumber()) {

                            if (roomNumber.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("room number field must be an integer value.");

                            }
                        } else {

                            throw new JsonParseException("room number field must be a number.");
                            
                        }
                    } else {

                        throw new JsonParseException("room number field cannot be an empty string.It must be a number");

                    }
                } else {

                    throw new JsonParseException("room number field must be a primitive value.");

                }
            } else {

                throw new JsonParseException("room number field cannot be null.");

            }
        } else {

            throw new JsonParseException("room number field is missing.");

        }

        StudentUpdateRequest roomNumUpdate = new StudentUpdateRequest();
        roomNumUpdate.setRoomNumber(roomNumber.getAsInt());

        return roomNumUpdate;
    }
    /*
     * This method parses feeStatus, paidForMonths(if provided by client)
     * and returns the StudentUpdateRequest containing the fields provided to
     * it.
     */
    public static StudentUpdateRequest parseFeeStatus(JsonElement element, JsonObject jsonObj) {
        

        JsonElement feeStatus = jsonObj.get("feeStatus");
        /*
         * validating feeStatus field before parsing
         */
        if (jsonObj.has("feeStatus")) {

            if (! feeStatus.isJsonNull()) {

                if (feeStatus.isJsonPrimitive()) {

                    if (! feeStatus.getAsString().isBlank()) {

                    } else {

                        throw new JsonParseException("feeStatus field cannot be an empty string.");
                    }

                } else {

                    throw new JsonParseException("feeStatus field cannot be null.");
                }

            } else {

                throw new JsonParseException("feeStatus field cannot be null.");
            }

        } else {

            throw new JsonParseException("feeStatus field is missing.");
        }

        JsonElement paidForMonths = jsonObj.get("paidForMonths");
        /*
         * validating paidForMonths field if provided
         * before parsing.
         */
        if (jsonObj.has("paidForMonths")) {

            if (! paidForMonths.isJsonNull()) {

                if (paidForMonths.isJsonPrimitive()) {

                    if (! paidForMonths.getAsString().isBlank()) {

                        if (paidForMonths.getAsJsonPrimitive().isNumber()) {

                            if (paidForMonths.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("paidformonths field must be an intger value.");
                            }

                        } else {

                            throw new JsonParseException("paidformonths must be a number.");
                        }

                    } else {

                        throw new JsonParseException("paidformonths cannot be an empty string.It must be a number");
                    }

                } else {

                    throw new JsonParseException("paidformonths field must be a primitive value.");
                }

            } else {

                throw new JsonParseException("paidformonths field cannot be null.");
            }

        } else {
                
            throw new JsonParseException("paidformonths field is missing.");

        }

        StudentUpdateRequest feeStatusRequest = new StudentUpdateRequest();
        feeStatusRequest.setFeeStatus(feeStatus.getAsString());
        feeStatusRequest.setPaidforMonths(paidForMonths.getAsInt());

        return feeStatusRequest;

    }
    
}
