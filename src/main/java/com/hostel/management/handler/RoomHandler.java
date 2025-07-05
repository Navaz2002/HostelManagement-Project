package com.hostel.management.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
//import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.hostel.management.Exception.DuplicateRoomException;
import com.hostel.management.deserialization.RoomCreateDeserializer;
import com.hostel.management.dto.request.RoomCreateRequest;
import com.hostel.management.dto.request.RoomUpdateRequest;
import com.hostel.management.dto.response.RoomCreateResponse;
import com.hostel.management.dto.response.RoomGetResponse;
import com.hostel.management.dto.response.RoomUpdateResponse;
import com.hostel.management.sevice.RoomService;
//import com.hostel.management.sevice.StudentService;
import com.hostel.management.utility.ExtractParameter;
import com.hostel.management.utility.Helper;
import com.hostel.management.utility.JsonParseUtil;
import com.hostel.management.utility.RoomFieldParser;
import com.hostel.management.validation.RoomValidator;
//import com.hostel.management.validation.StudentValidator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/*
 * This class is used to handle the requets(POST,PUT, GET, DELETE)
 * of room resource.
 * 
 * This receieves the exchange object from the server and finds
 * out the request method then handles that method with its respective methods
 * 
 */
public class RoomHandler implements HttpHandler{
    private RoomService roomService = new RoomService();
    private RoomValidator roomValidator = new RoomValidator();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            String requestMethod = exchange.getRequestMethod();
            System.out.println(requestMethod);
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
                    Helper.sendFailResponse("method is not allowed", 405,  exchange);
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
     * this method handles the delete request of room resouce
     * it processes and validates the receved data.
     * 
     * After completeing all validations it calss the service layer removeRoom()
     * for further process.
     */
    private void handleDeleteRequest(HttpExchange exchange) throws IOException {

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSegment = path.split("/");

        /*
         * this condition checks whether path has been included
         * in the request uri or not,if there is no path it
         * sends the fail response indicating path required
         */

        if (path.matches("/")) {

            Helper.sendFailResponse("Invalid request, path required", 400,  exchange);
            return;

        }

        /*
         * this condition checks if the pathSegments arry length is < 3
         * or not, if its true means the request does not include the room
         * number which is required ,so it sends fail response to the client.
         */
        if (pathSegment.length < 3) {

            Helper.sendFailResponse("Invalid request, room number is required.", 400,  exchange);
            return;

        }

        /*
         * this condition checks if the pathSegment length is 3
         * and if the path does not match the required path means 
         * its a invalid path ,so sends failresponse.
         */
        if (pathSegment.length == 3 && ! pathSegment[1].matches("Rooms")) {

            Helper.sendFailResponse("Invalid path, provide valid path.", 400,  exchange);
            return;

        }

        try {

            double roomNumber = Double.parseDouble(pathSegment[2]);
            roomService.removeRoom(roomNumber);

            Helper.sendSuccessResponse("Roomnumber "+roomNumber+ " deleted successfully", 200, exchange);
            return;

        } catch (NumberFormatException e) {

            Helper.sendFailResponse("Room number field must be a number.", 400,  exchange);
            return;

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (IllegalStateException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            e.printStackTrace();
            return;

        }

    }
    /*
     * This method is used to handle the put request of room resource.
     * First, it extracts the path from the request to check whether the 
     * request includes valid resource, room number or not.If the request does not
     * include any one these then it sends fail response accordingly.
     * Even if the resource is invalid.
     * 
     * Then after validations, it parses the json to jsonelement using parseStringToJsonElement() in
     * JsonParseUtil class.Then it checks whether the update request includes fee or capacity update
     * and calls the respective indvidual process() for that update.
     * If the request includes invalid update fields other than valid or includes no field for update,fail
     * response is sent accordingly
     * 
     */
    private void handlePutRequest(HttpExchange exchange) throws IOException {

        InputStream is = exchange.getRequestBody();
        byte[] bytes = is.readAllBytes();
        String json = new String(bytes);
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        /*
         * this conition ensures if no path was peovided with api request
         * then its invalid 
         */
        if (path.matches("/")) {
            Helper.sendFailResponse("Invalid Request , path required", 400,  exchange);
            return;
        }

        String[] pathArray = path.split("/");

        /*
        * this condition ensures that if the pathArray length is < 3 means PUT request
        * is missing its roomNumber 
        */
        if (pathArray.length < 3) {
            Helper.sendFailResponse("roomNumber is required", 400,  exchange);
            return;
        }

        /*
         * this makes sure that the PUt request has come to the right
         * path
         */
        if (pathArray.length >= 2) {
            if (! pathArray[1].matches("room")) {
                Helper.sendFailResponse("Invalid path", 400,  exchange);
                return;
            }
        }

        JsonElement jsonElement = null;

        try {

            jsonElement = JsonParseUtil.parseStringToJsonElement(json);
            JsonObject jsonObj = jsonElement.getAsJsonObject();

            if (jsonObj.has("capacity")) {
                //calling process() for capacity update
                processCapacityUpdate(jsonElement, jsonObj, exchange, pathArray);
                return;

            } else if (jsonObj.has("fee")) {
                //calling process() for fee update
                processFeeUpdate(jsonElement, jsonObj, exchange, pathArray);
                return;

            } else {

                Helper.sendFailResponse("Updating fields required.", 400,  exchange);
                return;

            }


        } catch (JsonSyntaxException e) {

            System.out.println("Jsonsyntax exception");
            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            e.printStackTrace();
            return;
        }
        
        
    }

    /*
     * this method specifically processes feeUpdate request, it takes parameters from
     * handlePutRequest() which provides JsonElement containing the updateRequest json,
     * JsonObject containing the request body, exchange object, pathArray[] containing the path
     * and roomNumber
     * this first takes the roomNumber from pathArray[], then uses parseFee() to parse JsonElement
     * into java object of type RoomUpdateRequest for further porocessing.
     * After successful parsing it calls updateFee() of RoomService and passess 
     * RoomUpdateRequest obj, roomNumber. 
     * 
     * updateFee() in service does further validations
     * and assigns the task to Dao for updating the database, upon the success it returns 
     * the object of RoomUpdateResponse
     * This RoomUpdateResponse is used to send the successResponse to the client
     */
    private void processFeeUpdate(JsonElement jsonElement, JsonObject jsonObj, HttpExchange exchange,
            String[] pathArray) throws IOException {

        try {
            //taking room number from pathArray
            double roomNumber = Double.parseDouble(pathArray[2]);

            //calling parseFee() for parsing JsonElement into UpdateRoomRequest object
            RoomUpdateRequest feeUpdate = RoomFieldParser.parseFee(jsonElement, jsonObj);
            //calling service layer updateFee()
            RoomUpdateResponse feeUpdateResponse = roomService.updateFee(feeUpdate, roomNumber);

            Helper.sendSuccessResponse(feeUpdateResponse, 200, exchange);
            return;

        } catch (NumberFormatException e) {

            Helper.sendFailResponse("roomNumber field must be a number.", 400,  exchange);
            return;

        } catch (JsonSyntaxException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (JsonParseException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;
            
        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }
    }
    /*
     * This is a submethod of handlePutRequest.This receives JsonElement, Jsonobject objects containing the
     * request body updating fields, pathArray[] containing the path segments like resource, room number.
     * This uses a static method parseCapacity() to parse the jsonObject to updateRoomRequest object.
     * ParseCapacity() returns the UpdateRoomRequest object which is sent to the service layer updateCapacity()
     * for further update process.
     * 
     * Upon update success it receives the RoomUpdateResponse object used to send to the client as a response.
     * Service layer method does not return anything,if any error occurs it throws exceptions and fail responses
     * are sent accordingly here.
     * 
     */
    private void processCapacityUpdate(JsonElement jsonElement, JsonObject jsonObj, HttpExchange exchange, String[] pathArray) throws IOException {
        
        try {
            //taking room number from pathArray to send it to service layetr
            double roomNumber = Double.parseDouble(pathArray[2]);
    
            //calling parseCapacity() for parsing capacity,bedsAvailable field present in jsonElement
            RoomUpdateRequest capacityUpdate = RoomFieldParser.parseCapacity(jsonElement, jsonObj);

            //calling service layer updateCapacity() for further update process.Also
            //receiving the response object
            RoomUpdateResponse capacityUpdateResponse = roomService.updateCapacity(capacityUpdate, roomNumber);

            Helper.sendSuccessResponse(capacityUpdateResponse,200, exchange);
            return;

        } catch (NumberFormatException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (JsonParseException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (Exception e){

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            System.out.println(e.getStackTrace());
            e.printStackTrace();
            return;

        }
    }
    /*
     * This method handles the get request of room resource.
     * It extracts the uri, path then validates whether the request has
     * come at the right resource or not.If the resource is invalid then
     * it sends the fail response accordingly.If the resource is correct 
     * then it calls the handleRoomGetRequest() for handling
     */
    private void handleGetRequest(HttpExchange exchange) throws IOException {

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSegment = path.split("/");
        String resource = null;

        /*
         * This condition checks whether the request has any path or not,
         * if not it sends fail response.
         */
        if (path.matches("/")) {

            Helper.sendFailResponse("Invalid request, path is required", 400,  exchange);
            return;

        }
        /*
         * This condition checks if the pathSegments length > 3
         * or not if it is means the request includes undefined
         * paths 
         */
        if (pathSegment.length > 3) {

            Helper.sendFailResponse("Invalid request, contains undefined paths", 400,  exchange);
            return;

        }
        /*
         * extracting resource string from pathsegment[] for validation
         */
        if (pathSegment.length > 1) {
            resource = pathSegment[1];
        }

        switch (resource) {
            case "room":
                handleRoomGetRequest(pathSegment,exchange,uri);
                break;
            default:
                Helper.sendFailResponse("Invalid path, provide valid path in the request.", 400,  exchange);
                break;
        }

        return;
    }
    /*
     * This is a submethod of handleGetRequest.This handles the get requests
     * coming at the room resource.
     * First it finds out whether the get request has query or path parameters or only
     * path.If its a combination of query and path then it sends fail response as its
     * not supported.
     * 
     * After finding the parameters,it calls respective handler methods for handling those
     * parameter request.
     */
    private void handleRoomGetRequest(String[] pathSegment, HttpExchange exchange, URI uri) throws IOException {

        boolean hasPathParameter = false;
        boolean hasOnlyPath = false;
        boolean hasQueryParameter = false;

        String queryParam = uri.getQuery();

        if (pathSegment.length == 3) hasPathParameter = true;
        if (pathSegment.length == 2 && queryParam == null) hasOnlyPath = true;
        if (queryParam != null) hasQueryParameter = true;

        if (hasPathParameter && hasQueryParameter) {

            Helper.sendFailResponse("combination of query, path parameters are not allowed", 400,  exchange);
            return;

        } else if (hasPathParameter) {
            //calling pathParamter() to handle path parameter
            handlePathParameter(pathSegment,exchange);
            return;

        } else if (hasQueryParameter) {
            //calling queryparameter method to handle query param
            handleQueryParameters(queryParam, exchange);
            return;

        } else if (hasOnlyPath) {
            //calling handleOnlyPath() to handle only path 
            handleOnlyPath(pathSegment, exchange);
            return;

        }
    }
    /*
     * This is a submethod of handleRoomGeRequest.
     * This method individually handles the query parameters of get request of resource room with
     * respective methods.
     * This first checks whether the query has single parameter or multiple parameters.Then
     * accordingly calls respective handler methods to handle those paramaters.
     */
    private void handleQueryParameters(String queryParam, HttpExchange exchange) throws IOException {

        String[] queries = queryParam.split("&");
        boolean hasOneQuery = false;
        boolean hasMultipleQueries = false;

        if (queries.length == 1) hasOneQuery = true;
        if (queries.length >= 2) hasMultipleQueries = true;

        if (hasOneQuery) {
            //calling single query parameter handler
            handleSingleQuryParameter(queries, exchange);
            return;

        } else if (hasMultipleQueries) {
            //calling multiple query param handler method
            handleMultyQueryParam(queries, exchange);
            return;

        }
    }
    /*
     * This is a submethod of handleQueryParameters.This individually handles
     * the multiple query parameters get requests of room resource.
     * 
     * It currently supports queries like
     * 1.roomsAvailable=true&capacity={}
     * 
     * First it extracts the key value pairs from the queries[] using a method, this method returns 
     * a map of key values of query.Then it checks whether the key values are valid or not,if valid
     * it calls its respective process() for further processing, if invalid sends respective fail
     * responses.
     * 
     */
    private void handleMultyQueryParam(String[] queries, HttpExchange exchange) throws IOException {

        if (queries.length > 2) {
            Helper.sendFailResponse("Invalid query parameters", 400,  exchange);
        }

        Map<String, String> queryParams = ExtractParameter.extractQueryParam(queries);
        Set<String> keys = queryParams.keySet();

        if (queryParams.containsKey("roomsAvailable")) {

            if (queryParams.get("roomsAvailable").equals("true")) {

                if (queryParams.containsKey("capacity")) {

                    if (queryParams.get("capacity").equals("2") || queryParams.get("capacity").equals("4")) {
                        //calling process() after successful validation of query
                        processRoomsAvailableParamWithCapacity(queryParams, exchange);
                        return;

                    } else {

                        Helper.sendFailResponse("capacity must be a number(either 2 or 4).", 400,  exchange);
                        return;

                    }

                } else {

                    Helper.sendFailResponse("capacity parameter missing with roomsAvailable parameter", 400, exchange);
                    return;

                }
            } else {

                Helper.sendFailResponse("Invalid value for parameter roomsAvailable, roomsAvailable must be true if provided.", 400,  exchange);
                return;

            }
        }


    }
    /*
     * This is a sub method of handleMultipleQueryParam.
     * This is used to get the avialable rooms with sepcific capacity.
     * 
     * This calls the service layer getAvailableRoomsOfCapacity() to get the rooms available
     * with provided capacity.
     * 
     * Upon success it recieves the list of RoomGetResponse list object to send the avilable rooms
     * with capacity to the client.
     */
    private void processRoomsAvailableParamWithCapacity(Map<String,String> queryParams, HttpExchange exchange) throws IOException {

        try {

            int capacity = Integer.parseInt(queryParams.get("capacity"));
            List<RoomGetResponse> availableRoomsOfCapacity = roomService.getAvailableRoomsOfCapacity(capacity);

            //if there are no rooms avilable with provided capacity.Send fail response
            if (availableRoomsOfCapacity.isEmpty()) {
                Helper.sendFailResponse("There are no rooms available with capacity "+capacity, 200,  exchange);
                return;
            }

            //Upon success,success response.
            Helper.sendSuccess(availableRoomsOfCapacity, 200, exchange);
            return;

        } catch (NumberFormatException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (Exception e){

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }
    }
    /*
     * This is a submethod of handleQueryParameter.
     * This extracts the key value from the query, checks whether the 
     * key is valid or not, if its invalid then sends fail response accordingly.
     * If the key is valid then it checks whether its value is valid or not,if its invalid
     * then sends fail response accordingly,if its valid then calls its respective
     * process() for further process.
     * 
     * Currently single query accepts only two queries
     * 1.roomsAvailable=true
     * 2.roomsAvailable=false
     * If the query includes anything other than this,then fail response is sent accordingly.
     */
    private void handleSingleQuryParameter(String[] queries, HttpExchange exchange) throws IOException {

        String query = queries[0];
        String[] queryMap = query.split("=");
        /*
         * this check checks whether the single query is complete
         * with the key and value 
         */
        if (queryMap.length == 0 || queryMap.length == 1) {

            Helper.sendFailResponse("Invalid query parameter", 400, exchange);
            return;

        }
        
        String key = queryMap[0];
        String val = queryMap[1];

        switch (key) {
            case "roomsAvailable":
                if (roomValidator.doesItMatch(val, "true")) {
                    //calling method for handling query for avilable rooms
                    processAvailableRoomsParam(exchange);

                } else if (roomValidator.doesItMatch(val, "false")) {
                    //calling method for handling query for unavailable rooms
                    processUnavailableRoomsParam(exchange);

                } else {

                    Helper.sendFailResponse("Invalid value for parameter roomsAvailable.It must be either true or false.", 400,  exchange);

                }
                break;

            default:
                Helper.sendFailResponse("Invalid parameter.", 400,  exchange);
                break;

        }

        return ;
    }
    /*
     * This is a submethod of handleOneQueryParmeter.This is used to get unavailable rooms
     * This calls the service layer getUnavilableRooms() to get the list
     * of unavailable rooms.
     * 
     * If the request is successful,it receives the list of RoomGetResponse object to
     * send to the client.
     */
    private void processUnavailableRoomsParam(HttpExchange exchange) throws IOException {

        try {
            //calling service layer method to get unavailable rooms
            List<RoomGetResponse> unavailableRooms = roomService.getUnavailableRooms();
            if (unavailableRooms.isEmpty()) {
                Helper.sendFailResponse("There are no unavailable rooms", 404,  exchange);
                return;
            }

            //sending response upon success
            Helper.sendSuccess(unavailableRooms, 200, exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }
    }
    /*
     * This is a submethod of handleOneQueryParm.
     * This calls the service layer getAvailablerooms() to get a list
     * of avialable rooms.
     * 
     * If the request is successful then it receives the list of RoomGetResponse object
     * used to send to the client.
     */
    private void processAvailableRoomsParam(HttpExchange exchange) throws IOException {

        
        try {
            //calling service layer method to get available rooms list
            List<RoomGetResponse> avilableRoomsResponse = roomService.getAvailableRooms();

            if (avilableRoomsResponse.isEmpty()) {

                Helper.sendFailResponse("There are no rooms with avilablebeds", 200,  exchange);
                return;

            }
            //sendig success response with the list 
            Helper.sendSuccess(avilableRoomsResponse, 200, exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }
    }

    private void handleOnlyPath(String[] pathSegment, HttpExchange exchange) {
        
        try {

            List<RoomGetResponse> roomsResponseList = roomService.getAllRooms();
            if (roomsResponseList.isEmpty()) {
                Helper.sendFailResponse("There are no rooms in the database", 404,  exchange);
                return;
            }

            Helper.sendSuccess(roomsResponseList, 200, exchange);
            return;

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    /*
     * This is a submethod of handleRoomGwtRequest.
     * This individually handles the pathparameter get request of room resource.
     * 
     * First it checks whether the request also includes the required room number or not.
     * If not then it sends the respective fail response.If it has room number, then it extracts
     * from the pathsegment[] and calls the service layer getroomBuNumber() to get the room.
     * 
     * If the request was successful then it gets the RoomGetResponse object from
     * the service layer which can be sent to the client.
     */
    private void handlePathParameter(String[] pathSegment, HttpExchange exchange) throws IOException {
        String roomnumber = null;

        if (pathSegment.length > 2) {

            roomnumber = pathSegment[2];

        }else{

            Helper.sendFailResponse("something went wrong", 500,  exchange);
            return;

        }

        try {

            double roomNumber = Double.parseDouble(roomnumber);
            RoomGetResponse roomResponse = roomService.getRoomByNumber(roomNumber);
            Helper.sendSuccessResponse(roomResponse, 200, exchange);
            return;

        } catch (NumberFormatException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (Exception e){

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }

    }
    /*
     * This method hadles the post request for room resource.
     * It recieves the details required for creation of room in database.
     * So it deserializes that recieved json request body into a CreaterequestRoom
     * object to send it to the service layer for further processing.
     * 
     * Upon success it receives the RoomCreateResponse object,sends this object to the client as 
     * response.
     * 
     * sends fail responses accordingly if anything goes wrong
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
        if (pathSegment.length == 2 && ! pathSegment[1].matches("room")) {
            
            Helper.sendFailResponse("Invalid request, contains invalid path", 400,  exchange);
            return;

        }
        Gson gson = new GsonBuilder().registerTypeAdapter(RoomCreateRequest.class, new RoomCreateDeserializer()).create();

        try {

            RoomCreateRequest roomRequest = gson.fromJson(json, RoomCreateRequest.class);
            //calling service layer addRoom() for further processing.
            RoomCreateResponse roomResponse = roomService.addRoom(roomRequest);
            Helper.sendSuccessResponse(roomResponse, 201, exchange);
            return;

        } catch (JsonParseException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (IllegalArgumentException e){

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (DuplicateRoomException e){

            Helper.sendFailResponse(e.getMessage(), 409, exchange);
            return;

        } catch (Exception e){

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }

    }
    
}
