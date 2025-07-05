package com.hostel.management.deserialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hostel.management.dto.request.StudentCreateRequest;
/*
 * This class conatins the method which is used to validate
 * and parse the student fields sent by the client to create
 * the student record in the database.
 * 
 */
public class StudentDeserializer implements JsonDeserializer<StudentCreateRequest>{

    /*
     * This method parses the received student details existing in json
     * into java object StudentCreateRequest for further process of creating the student
     * record in db.
     * 
     * This receives the JsonElement containing the entire json, JsonObject containing the request body.
     * This validates all the fields present in the request , throws JsonParseException if any
     * validation fails.
     * 
     * After all validations, it creates an object of StudentCreateRequest and sets its properties
     * using this fields(student details).
     * 
     * @returns the  StudentCreateRequest object with properties of student 
     */
    @Override
    public StudentCreateRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObj = json.getAsJsonObject();
        JsonElement studentId = jsonObj.get("studentId");

        /*
         * validating studentId field before parsing
         */
        if (jsonObj.has("studentId")) {

            if (! studentId.isJsonNull()) {

                if (studentId.isJsonPrimitive()) {

                    if (! studentId.getAsString().isBlank()) {

                        if (studentId.getAsJsonPrimitive().isNumber()) {

                            if (studentId.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("Student id must be an integer value.");
                            }

                        } else {

                            throw new JsonParseException("Student id must a number.");
                        }

                    } else {

                        throw new JsonParseException("Student id cannot be an empty string.It must be a number.");
                    }

                } else {

                    throw new JsonParseException("Student id must be a primitive value..");
                }

            } else {

                throw new JsonParseException("student id cannot be null.");
            }

        } else {

            throw new JsonParseException("Student id is missing..");

        }
        /*
         * validating name field before parsing
         */
        JsonElement name = jsonObj.get("name");
        if (jsonObj.has("name")) {

            if (! name.isJsonNull()) {

                if (name.isJsonPrimitive()) {

                    if (! name.getAsString().isBlank()) {

                        if (name.getAsJsonPrimitive().isNumber()) {

                            throw new JsonParseException("name cannot be a number.It must be a string.");

                        }

                    } else {

                        throw new JsonParseException("name cannot be an empty string.");

                    }

                } else {
                    
                    throw new JsonParseException("name must be a primitive value.");

                }

            } else {

                throw new JsonParseException("name cannot be null.");

            }

        } else {

            throw new JsonParseException("name field is missing.");

        }
        /*
         * validating contactNumber field before parsing
         */
        JsonElement contactNumber = jsonObj.get("contactNumber");
        if (jsonObj.has("contactNumber")) {

            if (! contactNumber.isJsonNull()) {

                if (contactNumber.isJsonPrimitive()) {

                    if (! contactNumber.getAsString().isBlank()) {

                        if (contactNumber.getAsJsonPrimitive().isNumber()) {

                            throw new JsonParseException("contact number must be a string value.");

                        }

                    } else {

                        throw new JsonParseException("contact number cannot be empty.");

                    }

                } else {

                    throw new JsonParseException("contact number must be a primitive value.");

                }

            } else {

                throw new JsonParseException("contact number cannot be null.");

            }

        } else {

            throw new JsonParseException("contact number is missing.");

        }

        /*
         * validating collegeName field before parsing
         */
        JsonElement collegeName = jsonObj.get("collegeName");
        if (jsonObj.has("collegeName")) {

            if (! contactNumber.isJsonNull()) {

                if (collegeName.isJsonPrimitive()) {

                    if (! collegeName.getAsString().isBlank()) {

                        if (collegeName.getAsJsonPrimitive().isNumber()) {

                            throw new JsonParseException("contact number must be a string value.");

                        }

                    } else {

                        throw new JsonParseException("contact number cannot be empty");

                    }

                } else {

                    throw new JsonParseException("contact number must be a primitive value.");

                }

            } else {

                throw new JsonParseException("contact number cannot be null.");

            }

        } else {

            throw new JsonParseException("college name is missing.");

        }

        /*
         * validating roomnumber field before parsing
         */
        JsonElement roomNumber = jsonObj.get("roomNumber");
        if (jsonObj.has("roomNumber")) {

            if (! roomNumber.isJsonNull()) {
                
                if (roomNumber.isJsonPrimitive()) {

                    if (! roomNumber.getAsString().isBlank()) {

                        if (roomNumber.getAsJsonPrimitive().isNumber()) {

                            if (roomNumber.getAsDouble() % 1 != 0) {

                                throw new JsonParseException("floating values are not supported.Room number must be an integer value.");

                            }

                        } else {

                            throw new JsonParseException("room number must be a number.");

                        }

                    } else {

                        throw new JsonParseException("room number cannot be an empty string.It must be a number.");
                        
                    }

                } else {

                    throw new JsonParseException("room number must be a primitive value.");

                }
                
            } else {

                throw new JsonParseException("room number cannot be null.");

            }

        } else {

            throw new JsonParseException("room number is missing.");
            
        }
        /*
         * validating paidformonths field if provided with student
         * details
         */
        JsonElement paidForMonths = jsonObj.get("paidForMonths");
        if (jsonObj.has("paidForMonths")) {

            if (! paidForMonths.isJsonNull()) {

                if (paidForMonths.isJsonPrimitive()) {

                    if (! paidForMonths.getAsString().isBlank()) {

                        if (paidForMonths.getAsJsonPrimitive().isNumber()) {

                            if (paidForMonths.getAsDouble() % 1 == 0) {

                                if (paidForMonths.getAsDouble() != 0) {
                                    
                                } else {

                                    throw new JsonParseException("paidformonths field value cannot be 0 .");
                                }

                            } else {

                                throw new JsonParseException("paidformonths field cannot be a floating value, it must be an integer value.");
                            }

                        } else {

                            throw new JsonParseException("paidformonths field must be a number.");
                        }

                    } else {

                        throw new JsonParseException("paidformonths field cannot be an empty string.It must be a number.");
                    }

                } else {

                    throw new JsonParseException("paidformonths field must be a primitive value.");
                }

            } else {

                throw new JsonParseException("paidformonths field value cannot be null.");
            }

        } else {
                
            throw new JsonParseException("paidformonths field is missing.");

        }

        /*
         * creating CreateStudentRequest object and setting its properties 
         * with provided student details
         */
        StudentCreateRequest student = new StudentCreateRequest();
        student.setStudentId(studentId.getAsInt());
        student.setName(name.getAsString());
        student.setContactNumber(contactNumber.getAsString());
        student.setCollegeName(collegeName.getAsString());
        student.setRoomNumber(roomNumber.getAsInt());
        student.setPaidForMonths(paidForMonths.getAsInt());
        
        return student;

    }
    
}
