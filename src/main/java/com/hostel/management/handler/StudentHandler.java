package com.hostel.management.handler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import com.hostel.management.Exception.DuplicatestudentException;
import com.hostel.management.deserialization.StudentDeserializer;
import com.hostel.management.dto.request.StudentCreateRequest;
import com.hostel.management.dto.request.StudentUpdateRequest;
import com.hostel.management.dto.response.StudentCreateResponse;
import com.hostel.management.dto.response.StudentGetResponse;
import com.hostel.management.dto.response.StudentUpdateResponse;

import com.hostel.management.sevice.StudentService;
import com.hostel.management.utility.Helper;
import com.hostel.management.utility.JsonParseUtil;
import com.hostel.management.utility.StudentFieldParser;
import com.hostel.management.validation.StudentValidator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.HibernateException;
/*
 * This class is used to handle the requets(POST,PUT, GET, DELETE)
 * of student resource.
 * 
 * This receieves the exchange object from the server and finds
 * out the request method then handles that method with its respective methods
 * 
 */
public class StudentHandler implements HttpHandler{
    private StudentService studentService = new StudentService();
    private StudentValidator studentValidator = new StudentValidator();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "POST":
                    handlePostRequest(exchange);
                    break;
                case "GET":
                    handleGetRequest(exchange);
                    break;
                case "PUT":
                    handlePutRequest(exchange);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange);
                    break;
                default:
                    Helper.sendFailResponse("method not allowed", 405,  exchange);
                    break;

            }

        } catch (IOException e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            e.printStackTrace();
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            e.printStackTrace();
            return;
            
        }


        return;

    }

    /*
     * this method is used to handles the get request of student resource
     * this extraxts uri from the exchnage object , then extracts path from
     * uri.Then it checks whether the request has come to the correct
     * resource or not , if not it sends the fail response.If the request
     * has come to the right resource then it calls the studentGetRequest handler
     * method to handle it
     */
    private void handleGetRequest(HttpExchange exchange) throws IOException {

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] resources = path.split("/");
        String resource = resources[1];

        switch (resource) {
            case "student":
                handleStudentGetRequest(path, uri,exchange);
                break;
            default:
                Helper.sendFailResponse("Invalid resource or path", 400,  exchange);
                break;
        }        

        return;

    }

    /*
     * this is a submethod of handlePutRequest, this method recieves the uri,path, exchange from its caller
     * method.This first finds out whether the request has path or query parameter or has only path
     * or combination of path and query parameters.It sends fails response if its a combination of
     * path and query parameter as its not supported.After finding out the parameter it calls its 
     * respective handler method to handle those 
     */
    private void handleStudentGetRequest(String path, URI uri, HttpExchange exchange) throws IOException {

        boolean hasPathParameter = false;
        boolean hasQueryParamater = false;
        boolean hasOnlyPath = false;

        String query = uri.getQuery();

        String[] pathArray = path.split("/");

        if (pathArray.length ==  3) hasPathParameter = true;
        if (query != null && ! query.isBlank()) hasQueryParamater = true;
        if (pathArray.length == 2) hasOnlyPath = true;
    
        /*
         * this checks if the get request includes both query and path parameters then it sends 
         * the fail resoponse, as its not supported.
         */
        if(hasPathParameter && hasQueryParamater){

            Helper.sendFailResponse( "mix of path and query parameters are not allowed", 400 , exchange);
            return;

        }else if(hasPathParameter){
            /*
             * calling respective handler method for path parameter
             */
            handlePathParameter(pathArray, exchange);
            return;

      
        }else if(hasQueryParamater) {
            /*
             * calling respective handler method for query parameter, if the request has it
             */
            handleQueryParam(pathArray, query, exchange);
            return;

        }else if(hasOnlyPath){
            /*
             * if the request has only path
             * calling respective handler method
             */
            handleOnlyPathReq(exchange);
            return;
            
        }

        Helper.sendFailResponse("Invalid request, contains undefined parameters.", 400, exchange);
        return;

    }
    /*
     * This method is submethod of handleStudentGetRequest().
     * 
     * This handles the only path req of get req of Student resource.
     * 
     */
    private void handleOnlyPathReq(HttpExchange exchange) throws IOException {

        try {
            /*
             * calling service layer method to get all students and receiving the 
             * students list
             */
            List<StudentGetResponse> studentsList = studentService.getAllStudents();

            if (studentsList == null || studentsList.isEmpty()) {

                Helper.sendFailResponse("There are no students in the database", 200,  exchange);
                return;

            }

            Helper.sendSuccessResponse(studentsList, 200, exchange);
            return;

        
        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }
        
    }

    /*
     * This method is a sub method of handleStudentGetRequest().
     * 
     * This handles the path parameter of get request of student resource.
     * First gets the student id from the pathArray then sends id to the service layer
     * getStudentById() to get the student 
     * 
     * Then receieves the StudentGetResponse object containing the details of
     * student id provided.Then sends this response object to sendSuccessResponse()
     * to send the student details to the client. 
     * 
     */
    private void handlePathParameter(String[] pathArray, HttpExchange exchange) throws IOException {
        
        try {
            
            double studentId = Double.parseDouble(pathArray[pathArray.length - 1]);
            // calling and getting StudentGetResponse object with details of
            // student id provided.
            StudentGetResponse studentResponse = studentService.getStudentById(studentId);

            Helper.sendSuccessResponse(200, exchange, studentResponse);
            return;


        } catch (NumberFormatException e) {
      
            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;
        }
    }

    /*
     * this is a submethod of handleStudentGetRequest.This receives the query from its caller,then
     * finds out whether the query has one query or multiple queries in it.If it has multiple
     * queries then it sends fail response as its not supported.If its single query then it calls its handler
     * method handle different single queries
     */
    private void handleQueryParam(String[] pathArray, String query, HttpExchange exchange) throws IOException {
        
        String[] queries = query.split("&");
        boolean hasOneQuery = false;
        boolean hasMultipleQueries = false;

        if (queries.length == 1) hasOneQuery = true;
        if (queries.length >= 2) hasMultipleQueries  = true;

        if (hasOneQuery) {

            handleOneParameterQ(queries,exchange);
            return;

        } else if (hasMultipleQueries) {

            Helper.sendFailResponse("currently multiple query parameters are not supported", 400, exchange);
            return;

        }
    }

    /*
     * This method is a submethod of handleQueryParameter.This method handles single queries like
     * feeStatus{paid,due}, collegeName, joiningdate with respective handlers.
     * This first extracts the key,value from the queries[] array containing key value.Then it checks 
     * if the query is valid or not, if its valid then it calls its respective handler method.If its 
     * invalid then it sends fail response
     */
    private void handleOneParameterQ(String[] queries, HttpExchange exchange) throws IOException {

        /*
         * extracting key, value 
         */
        String keyVal = queries[0];
        String[] oneQuery = keyVal.split("=");
        /*
         * this condition checks if the parameter has got
         * key and value or not
         */
        if (oneQuery.length == 1 || oneQuery.length == 0) {

            Helper.sendFailResponse("Invalid parmeters.", 400, exchange);
            return;

        }

        String key = oneQuery[0];
        String value = oneQuery[1];

  
        switch (key) {
            case "feeStatus":
                if (studentValidator.doesItMatch(value, "Paid")) {
                    /*
                     * calling feeHandler to handle feeParameter if its value is Paid
                     */
                    handleFeeParameter(key, value, exchange);
                    return;

                } else if (studentValidator.doesItMatch(value, "Due")) {
                    /*
                     * calling feeHandler to handle feeParameter if its value is due
                     */
                    handleFeeParameter(key, value, exchange);
                    return;

                } else {
                    /*
                     * sending fails response if the value of parameter is invalid, unexpected
                     */
                    Helper.sendFailResponse("Invalid value for key feeStatus parameter.", 400, exchange);


                }
                break;
                
            case "collegeName":
                if (studentValidator.doesItMatch(value, "KPRIT")) {
                    /*
                     * calling collegeHandler if the value of college name paremeter value
                     * is KPRIT
                     */
                    handleCollegeParameter(key,value, exchange);
                    return;

                } else {
                    /*
                     * sending fail response if the value of college parameter is invalid
                     * or unexpected 
                     */
                    Helper.sendFailResponse("Invalid value for key collegeName.Provide valid college name.", 400,  exchange);
            
                }
                break;

            case "roomNumber":
                    /*
                     * calling roomhandler method
                     */
                    handleRoomParameter(key, value, exchange);
                    break;

            case "joiningDate":
                    /*
                     * calling joiningdate handler method if the parameter
                     * is this
                     */
                    handleDateParameter(value, exchange);
                    break;

            default:
                /*
                 * In case if the query's key is invalid
                 */
                Helper.sendFailResponse("Invalid key parameters.", 400, exchange);
                break;
        }

        return;
    }

    /*
     * This method is a submethod of handleOneParameterQ.It handles the joining date query parameter.
     * It sends the joining date (provided in query) to the service layer to get a list of students 
     * who joined on that specific date,it sends the list if its not empty or sends the fail response 
     * if the list is empty
     */
    private void handleDateParameter(String value, HttpExchange exchange) throws IOException {

        if (! studentValidator.isDateValid(value)) {
            Helper.sendFailResponse("Inavlid date format", 400, exchange);
            return;
        }

        try {

            LocalDate joiningDate = LocalDate.parse(value);
            List<StudentGetResponse> students = studentService.getStudentsByJoiningDate(joiningDate);
            if (students.isEmpty()) {
                Helper.sendFailResponse("No student joined on date "+value, 200, exchange);
                return;
            }

            Helper.sendSuccessResponse(students, 200, exchange);
            return;

        } catch (Exception e) {
            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;
        }
    }

    /*
     * This method is a submethod of handleOneParameterQ.It handles the room query paremeter.
     * It sends the room number to the service layer method to get a list of students belonging
     * to that room.It sends the list if its not empty or sends fail response if the list
     * is empty
     */
    private void handleRoomParameter(String key, String value, HttpExchange exchange) throws IOException {

        try {

            double roomNumber = Double.parseDouble(value);
            List<StudentGetResponse> students = studentService.getStudentsByRoomNumber(roomNumber);
            if (students.isEmpty()) {
                Helper.sendFailResponse("There are no strudents in room "+(int)roomNumber, 200, exchange);
                return;

            }

            Helper.sendSuccessResponse(students, 200, exchange);
            return;

        } catch (NumberFormatException e){

            Helper.sendFailResponse("Room number must be valid number.", 400, exchange);

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (Exception e){

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }
        
    }

    /*
     * this method is a submethod of handleOneParameterQ.This method handles fee query parameter
     * with values Paid, Due.This calls the service layer method to get students list as
     * per the matching query.Then it checks if the list is empty or not, if not,sends the list to the client.
     * If empty means no student records exist matching the query so sends fail response
     */
    private void handleFeeParameter(String key, String value, HttpExchange exchange) throws IOException {
        
        try {
       
            List<StudentGetResponse> studentsList = studentService.getStudentsByFeeStatus(value);
       
            if (studentsList.isEmpty()) {

                Helper.sendFailResponse("There are no students whose feeStatus is   "+value, 200,  exchange);
                return;

            }

            Helper.sendSuccessResponse(studentsList, 200, exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;

        }
        
    }

    /*
     * This method is a submethod of handleOnePrameterQ.This handles the collegeName query parmeter.
     * It calls the service layer method to get a list of students belonging to the specific college
     * provided in the query parameter.It sends the list if its not empty or sends the fail response if
     * the list is empty.
     */
    private void handleCollegeParameter(String key, String value, HttpExchange exchange) throws IOException {

        try {

            List<StudentGetResponse> studentsList = studentService.getStudentsOfCollege(value);
            if (studentsList != null && ! studentsList.isEmpty()) {
                Helper.sendSuccessResponse(studentsList, 200, exchange);
                return;
            }

            Helper.sendFailResponse("There are no students from "+value, 200, exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;

        }

    }

    /*
     * This method handles the delete request of student resource.
     * It includes deleting student record.This method first checks if the
     * request is valid or not like the path it has come at and does it include
     * the id of student or not.If any validation fails then it sends fail response accordingly
     * 
     * After all validations it extracts the id from the request then calls the service layer method for
     * further process of delete operation.
     * 
     * If th operation was successful then it sends success response
     * or sends fail response if it was unsuccessful.
     * 
     */
    private void handleDeleteRequest(HttpExchange exchange) throws IOException {

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSegment = path.split("/");
        
        /*
         * This condition checks whether the request has any path or not,
         * if not it sends fail response.
         */
        if (path.matches("/")) {

            Helper.sendFailResponse("Invalid request, path is required", 400,  exchange);
            return;

        }

        /*
         * This condition checks if the request includes the required id or not, if
         * not it sends fail response
         */
        if (pathSegment.length < 3) {

            Helper.sendFailResponse("Invalid request, roll number is required", 400, exchange);
            return;   

        }

        /*
         * This condition checks whether the request has come at the right resource
         * or not, if not it sends fail response
         */
        if (pathSegment.length > 2 && ! pathSegment[1].matches("student")) {

            Helper.sendFailResponse("Invalid path", 400, exchange);
            return;   

        }

        try {

            double id = Double.parseDouble(pathSegment[2]);
            //calling service layer method for further process of delete 
            studentService.removeStudent(id);
            
            Helper.sendSuccessResponse("student with id "+(int)id+" deleted successfully.", 200, exchange);
            return;

        } catch (NumberFormatException e) {

            Helper.sendFailResponse("Student id must be number.", 400, exchange);
            return;

        } catch (IllegalArgumentException e ) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (Exception e){

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;

        }
    }

    /*
     * this method is used to handle the update request methods of Student resource,
     * includes contact number, room number updates.This method handles the single
     * and multiple updates efficiently.This method has seperate sub methods for each field
     * update such as for  contact, roomnumber.
     * This method extracts the student id from reuqest, validates all checks,then
     * continues for futher excutions
     */
    private void handlePutRequest(HttpExchange exchange) throws IOException {

        InputStream is = exchange.getRequestBody();
        byte[] bytes = is.readAllBytes();
        String json = new String(bytes);

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSegment = path.split("/");

        /*
         * this check validates whether the request is valid or not.
         * If this becomes true means the request includes no path and resource.
         * So it sends fail response
         */
        if (path.matches("/")) {

            Helper.sendFailResponse("Invalid request.Request has no path or resource.", 400, exchange);
            return;

        }

        /*
         * this condition checks whether the request includes the requires student id'
         * or not, if the pathsegment[] length is < 3 means ,the request does not
         * include student id , so sends fail response.
         */
        if (pathSegment.length < 3) {

            Helper.sendFailResponse("Invalid request, student id required.", 400, exchange);
            return;

        }

        /*
         * this conditions checks if the included path/resource in the request is
         * valid or not, if its not valid it sends fail response
         */
        if (pathSegment.length > 2 && ! pathSegment[1].matches("student")) {

            Helper.sendFailResponse("Invalid path, provide valid path.", 400, exchange);
            return;

        }

        JsonElement element = null;
        JsonObject jsonObj = null;

        try {

            element = JsonParseUtil.parseStringToJsonElement(json);
            jsonObj = element.getAsJsonObject();
            /*
             * thic checks if the request body has feeStatus field or not,
             * if present then it calls its respective handler method
             */
            if (jsonObj.has("feeStatus")) {

                handleFeeStatusUpdate(element, jsonObj, exchange, pathSegment);
                return;

            }
            /*
             * this checks if the request has contact field or not, if its then calls its
             * respective handler
             */
            if (jsonObj.has("contactNumber")) {

                handleContactUpdate(jsonObj, element, exchange, pathSegment);
                return;

            }
            /*
             * this checks if the request contains room number field or not, if it has the field
             *  , then it calls its respective handler metrhod 
             */
            if (jsonObj.has("roomNumber")) {

                handleRoomNumUpdate(jsonObj, element, exchange, pathSegment);
                return;

            }


        } catch (JsonSyntaxException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            e.printStackTrace();
            return;

        }

        /*
         * if the request body has no updating field 
         * then send fail response to the client
         */
        Helper.sendFailResponse("Updating field is missing", 400, exchange);
        return;

    }
    /*
     * this method is a submethod of handlePutRequest().
     * This individually handles the feeStatus update of a student,
     * it receives the JsonElement containing the entire request, JsonObject conatining the
     * request body, exchange object, pathSegmentp[] string containing the studentId.
     * 
     * This gets the studentId from pathSegemnt[] then it uses parseFeeStatus() to parse and
     * validate the field feeStatus.Then it receives a StudentUpdateRequest object from this
     * parsing.
     * 
     * Then it checks the JsonObject has paidForMonhs field or not,then it calls the service layer
     * method for further processing of update.
     */
    private void handleFeeStatusUpdate(JsonElement element, JsonObject jsonObj, HttpExchange exchange, String[] pathSegment) throws IOException {

        try {
            //geeting studentId from pathSegment[]
            double studentId = Double.parseDouble(pathSegment[2]);
            //calling parseFeeStatus() to parse and validate feeStatus field
            StudentUpdateRequest feeStatusRequest = StudentFieldParser.parseFeeStatus(element, jsonObj);

            /*
             * calling service layer updateFeeStatusOfStudent() for further processing
             */
            StudentUpdateResponse feeStatusResponse = studentService.updateFeeStatusOfStudent(feeStatusRequest, studentId);

            Helper.sendSuccessResponse(feeStatusResponse, 200, exchange);
            return;

        } catch (NumberFormatException e) {
            
            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (JsonParseException e) {
            
            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;

        }
    }

    /*
     * this method is submethod of handlePutRequest, this method individually handles the roomNum
     * updaterequest, this parses the received jsonElement into a java object for sending request to service layer method ,then sends the request object to service layer for the updation, 
     * It receives true from service layer method if the update was successful, false if not.
     * 
     * @return returns true if update was successful, false if not also sends the fail response.
     */
    private void handleRoomNumUpdate(JsonObject jsonObj, JsonElement element, HttpExchange exchange, String[] pathSegment) throws IOException {
   
        try {

            double studentId = Double.parseDouble(pathSegment[2]);
            //calling service layer for further process for update
            StudentUpdateRequest roomNumUpdate = StudentFieldParser.parseRoomField(element, jsonObj);

            StudentUpdateResponse updateResponse = studentService.updateStudentRoomNum(roomNumUpdate, studentId);
            if (updateResponse == null) {

                Helper.sendFailResponse("room number was not updated due to unexpected error.", 500,  exchange);
                return;

            }

            Helper.sendSuccessResponse(updateResponse, 200, exchange);
            return;

        } catch (NumberFormatException e) {
            
            Helper.sendFailResponse("Student Id must be an integer value.", 400, exchange);
            return;

        } catch (JsonParseException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (IllegalStateException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;

        }

    }

    /*
     * currently paused this feature,builds afterwards
     */
    // private boolean handleRoomNumUpdateForSpecificStudents(JsonElement element, JsonObject jsonObj, HttpExchange exchange) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'handleRoomNumUpdateForAllStudents'");
    // }

    /*
     * this handler method is a submethod of handlePutRequest , this method individually
     * handles the contact number update request.
     * This method uses a parse method to validate recieved jsonElement containing contact field, 
     * then parses into StudentUpdateRequest object.Then it sends the object to service
     * layer for further process.
     * 
     * This receives a boolean value from the service method indicating the success or failure of
     * the update.If the update was failed then it receieves false, if successful receives true.
     * Based on the return from service layer method, this sends the boolean value to its caller.
     * 
     * @returns true if the update was successful, false if update was unsuccessful also sends the 
     * fail response.
     */
    private void handleContactUpdate(JsonObject jsonObj, JsonElement element, HttpExchange exchange, String[] pathSegment) throws IOException{

        try {
            
            double studentId = Double.parseDouble(pathSegment[2]);
            StudentUpdateRequest contactRequest = StudentFieldParser.parseContact(element, jsonObj);

            StudentUpdateResponse updateResponse = studentService.updateStudentContact(contactRequest, studentId);

            if (updateResponse == null) {

                Helper.sendFailResponse("Update was unsuccessful due to unexpected error", 500, exchange);
                return;

            }

            Helper.sendSuccessResponse(updateResponse, 200, exchange);
            return;

        } catch (NumberFormatException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (JsonParseException e) {
            
            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            e.printStackTrace();
            return;

        }
       
    }

    /*
     * this method handles the post request of students resource.This uses a custom deserializer
     * class for deserializing the json containing the request body into a CreateStudentRequest
     * object.This method may send fail respoonses accordingly if any exception occurs during parsing.
     * After successful parsing, it sends the createStudentrequest object to the service layer for further 
     * process.
     * This sends the success response if the student object created successfully without any error, or
     * sends fail responses according to the exceptions if anything is invalid
     */
    private void handlePostRequest(HttpExchange exchange) throws IOException {
        
        InputStream is = exchange.getRequestBody();
        byte[] bytes = is.readAllBytes();
        String json = new String(bytes);

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSegment = path.split("/");

        /*
         * This condition checks whether the request has any path or not,
         * if not it sends fail response.
         */
        if (path.matches("/")) {

            Helper.sendFailResponse("Invalid request, path is required", 400,  exchange);
            return;

        }
        /*
         * This condition checks if the pathSegments length > 2
         * or not if it is means the request includes undefined
         * paths 
         */
        if (pathSegment.length > 2) {

            Helper.sendFailResponse("Invalid request, contains undefined paths", 400,  exchange);
            return;

        }
        /*
         * This condition checks if the pathSegments length is 2 or not
         * and if the included path/resource is valid or not.
         * 
         * If path does not macth means request contains invalid
         * path so sends fail response.
         */
        if (pathSegment.length == 2 && ! pathSegment[1].matches("student")) {
            
            Helper.sendFailResponse("Invalid request, contains invalid path", 400,  exchange);
            return;

        }

        Gson gson = new GsonBuilder().registerTypeAdapter(StudentCreateRequest.class,  new StudentDeserializer()).create();

        try {

            /*
             * StudentDeserializer class method is called to validate and deserialize the json
             * into StudentCreateRequest which contains details of student 
             */
            StudentCreateRequest studentRequest = gson.fromJson(json, StudentCreateRequest.class);

            StudentCreateResponse studentResponse = studentService.addStudent(studentRequest);
            /*
            * sending the success response after student object created successfully
            */
            Helper.sendSuccessResponse(studentResponse, 201, gson, exchange);
            return;

        } catch (JsonParseException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400, exchange);
            return;

        } catch (IllegalStateException e){
            
            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;

        } catch (DuplicatestudentException e){

            Helper.sendFailResponse(e.getMessage(), 409, exchange);
            return;

        } catch (HibernateException e){
                
            Helper.sendFailResponse("From world", 500, exchange);
            return;

        } catch (Exception e){
           
            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;

        }

    }
    
}
