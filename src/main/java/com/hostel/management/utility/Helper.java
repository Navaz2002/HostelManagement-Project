package com.hostel.management.utility;

import java.io.IOException;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import com.hostel.management.dto.response.PaymentGetResponse;
import com.hostel.management.dto.response.RoomCreateResponse;
import com.hostel.management.dto.response.RoomGetResponse;
import com.hostel.management.dto.response.RoomUpdateResponse;
import com.hostel.management.dto.response.StudentGetResponse;
import com.hostel.management.dto.response.StudentCreateResponse;
import com.hostel.management.dto.response.StudentUpdateResponse;
import com.hostel.management.error.ErrorResponse;

import com.hostel.management.success.SuccessResponse;
import com.sun.net.httpserver.HttpExchange;

/*
 * This class used to send the responses to the client.
 * 
 */
public class Helper {
    
    /*
     * this method is used to send the fail response to the client.
     * It uses a ErrorResponse object to send the response.
     */
    public static void sendFailResponse(String message, int statusCode, HttpExchange exchange) throws IOException{

        ErrorResponse error = new ErrorResponse("error", message, statusCode);
        String json = GsonFactory.getGsonInstance().toJson(error);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
        
    }

    /*
     * this method is used to send the success response for successful creation
     * of student object.
     * It uses CreateStudentRequest objcet containing the details of student
     * to send response.
     */
    public static void sendSuccessResponse(StudentCreateResponse studentResponse, int statusCode, Gson gson, HttpExchange exchange) throws IOException,JsonParseException{

        String json = gson.toJson(studentResponse);

        exchange.getResponseHeaders().add("content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
        
    }
    /*
     * this method is used to send success response for get request of
     * student resource.
     * It uses StudentGetResponse object which contains the details of a 
     * student requested by client 
     */
    public static void sendSuccessResponse(int statusCode, HttpExchange exchange,StudentGetResponse studentGetResponse) throws IOException {
       
        String json = GsonFactory.getGsonBuilder().toJson(studentGetResponse);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
      
        
    }

    /*
     * this method is used to send the response for get request of
     * student resource.
     * It sends the StudentGetResponse object list containing student's
     * details requested by client
     */
    public static void sendSuccessResponse(List<StudentGetResponse> studentsList, int statusCode, HttpExchange exchange) throws IOException {

        String json = GsonFactory.getGsonBuilder().toJson(studentsList);
   
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
        
    }
    /*
     * this method is used to send the response for get request of
     * room resource.
     * It sends the RoomGetResponse object containing the student details 
     * requested by the clent.
     */
    public static void sendSuccessResponse(RoomGetResponse roomResponse, int statusCode, HttpExchange exchange) throws IOException {

       
        String json = GsonFactory.getGsonInstance().toJson(roomResponse);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();

    }
    /*
     * this method is used to send response for get request of
     * room resource.
     * It sends RoomGetResponse object list containing students details requested
     * by the client
     */
    public static void sendSuccess(List<RoomGetResponse> roomsResponseList, int statusCode, HttpExchange exchange) throws IOException {
       
        String json = GsonFactory.getGsonInstance().toJson(roomsResponseList);

        exchange.getResponseHeaders().add("content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
        
    }

    /*
     * this method is used to send the success response for successful creation of room object,
     * it takes RoomCreateResponse object which contains all the details of created room,
     */
    public static void sendSuccessResponse(RoomCreateResponse roomResponse, int statusCode, HttpExchange exchange) throws IOException {
        
        String json = GsonFactory.getGsonInstance().toJson(roomResponse);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
    }

    /*
     * this method is used to send response for put request of
     * room resource.
     * It sends the RoomUpdateResponse object containing the details of updated
     * room
     */
    public static void sendSuccessResponse(RoomUpdateResponse feeUpdateResponse, int statusCode,
            HttpExchange exchange) throws IOException {
                
        String json = GsonFactory.getGsonInstance().toJson(feeUpdateResponse);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
    }
    /*
     * this method is just used to send the success response with
     * just a message.
     */
    public static void sendSuccessResponse(String message, int statusCode, HttpExchange exchange) throws IOException {

        SuccessResponse resp = new SuccessResponse("success", message, statusCode);
        String json = GsonFactory.getGsonInstance().toJson(resp);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
    }
    /*
     * this method is used to send the response for put request of
     * student resource.
     * It sends StudentUpdateResponse object containing the details of 
     * updated student.
     */
    public static void sendSuccessResponse(StudentUpdateResponse updateResponse, int statusCode,
        HttpExchange exchange) throws IOException {

        String json = GsonFactory.getGsonBuilder().toJson(updateResponse);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();

    }

    public static void sendSuccessResponseForPaymentRecord(List<PaymentGetResponse> response, int statusCode, HttpExchange exchange) throws IOException {
        
        String json = GsonFactory.getDateTimeGson().toJson(response);
        
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
        
    }
    /*
     * This method is used to send the success response for the
     * get request of Payment resource.
     * 
     * It sends the list containing all students payment records
     * to the client.
     */
    public static void sendSuccessResponseForPaymentRecords(List<PaymentGetResponse> studentsPayments, int statusCode,
            HttpExchange exchange) throws IOException {
        
        String json = GsonFactory.getDateTimeGson().toJson(studentsPayments);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
        
    }

   


}
