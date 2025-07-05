package com.hostel.management.handler;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.hostel.management.dto.response.PaymentGetResponse;
import com.hostel.management.sevice.PaymentService;
import com.hostel.management.utility.Helper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
/*
 * This class is used to handle the requets(POST,PUT, GET, DELETE)
 * of payment resource.
 * 
 * This receieves the exchange object from the server and finds
 * out the request method then handles that method with its respective methods
 * 
 */
public class PaymentHandler implements HttpHandler{

    private PaymentService paymentService = new PaymentService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {

            String requestMethod = exchange.getRequestMethod();

            switch (requestMethod) {
                case "GET":
                    handleGetMethod(exchange);
                    break;
        
                default:
                    Helper.sendFailResponse("Invalid request, method not allowed.", 405, exchange);
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
 


    }
    /*
     * This method checks whether the request is valid or not,
     * checks it has come at the valid resource or not,
     * if any validation fails sends respective fail response.
     * 
     * If all validations complete it calls the another handler() which
     * handles this get request.
     * 
     */
    private void handleGetMethod(HttpExchange exchange) throws IOException {
        
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSegments = path.split("/");
        /*
         * this conditions checks if the pathSegment lenth
         * is valid or not, if its length is below 2 or above 3
         * means the request is invalid , contains invalid resources
         * or paths.
         */
        if (pathSegments.length < 2 || pathSegments.length > 3) {

            Helper.sendFailResponse("Invalid request.", 400,  exchange);

        }

        switch (pathSegments[1]) {
            case "payment":
                handlePaymentGetRequest(exchange, pathSegments, uri);
                break;

            default:
                Helper.sendFailResponse("Resource is not found.", 404,  exchange);
                break;
        }

    }
    /*
     * This method extracts the query from the URI object then checks whether the 
     * request contains query or path parameter or just path then it calls the resppective
     * handler() to handle that paramter
     * 
     * sends fail response if the request conatins a mix of query and path parameter
     * or just query parameter as these parameters are not supported currently.
     * Sends fail response if the request contains some undefined paramters
     *  
     */
    private void handlePaymentGetRequest(HttpExchange exchange, String[] pathSegments, URI uri) throws IOException {

        String query = uri.getQuery();

        boolean hasOnlyPath = false;
        boolean hasPathParam = false;
        boolean hasQueryParam = false;

        if (pathSegments.length == 2 && query == null) hasOnlyPath = true;
        if (pathSegments.length == 3) hasPathParam = true;
        if (query != null && ! query.isBlank()) hasQueryParam = true;

        /*
         * If the query has both path and query parameters 
         * then its invalid query as its currently not supported.
         */
        if (hasQueryParam && hasPathParam) {

            Helper.sendFailResponse("combination of query and path parameters are not supported", 400,  exchange);
            return;
        }
        /*
         * If the request has query parameter ,then it calls its respective
         * handle() to handle 
         */
        if (hasQueryParam) {

            Helper.sendFailResponse("query parameters are not allowed.", 400,  exchange);
            return;

        }
        /*
         * If the request has path parameter then it calls its repective
         * handler() to handle it.
         */
        if (hasPathParam) {
         
            handlePathParamReq(pathSegments, exchange);
            return;
            
        } 
        /*
         * If the request has only Path then calls its respective
         * handler() to handle it.
         */
        if (hasOnlyPath) {

            handleOnlyPathReq(exchange);
            return;

        }

        /*
         * If the request has something undefined other than above queries, then its a
         * invalid request.So send response.
         */

        Helper.sendFailResponse("Invalid request, request contains parmeters which are undefined.", 0, exchange);
        return;
    }
    /*
     * This method handles the only path of get request of
     * Payment resource.
     * 
     * This gets all student payment records from the service layer.
     * 
     * This calls the service layer method() to get all student payment records
     * in a list.Then sends that list through success response if the list is not
     * empty
     * if the list is empty means there are no records in payment so sends fail 
     * response.
     * 
     */
    private void handleOnlyPathReq(HttpExchange exchange) throws IOException {

        try {
            
            List<PaymentGetResponse> studentsPayment = paymentService.getAllStudentPayments();

            if (studentsPayment.isEmpty()) {

                Helper.sendFailResponse("There are no students payment records.", 200,  exchange);
                return;

            }

            Helper.sendSuccessResponseForPaymentRecords(studentsPayment, 200, exchange);
            return;
            
        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500, exchange);
            return;
        }
    }
    /*
     * This method handles the path parameter of get request of
     * Payment resource.
     * 
     * this method calls the service layer getPaymentById() to get the payment
     * records of a student by id
     * 
     * after getting the list of payment records it checks if it empty or not if its empty
     * means there are no payment records of the provided student id so sends 
     * fail response.
     * 
     * if the list contains records then it sends success response.
     */
    private void handlePathParamReq(String[] pathSegments, HttpExchange exchange) throws IOException {
        
        try {

            String id = pathSegments[2];
            double studentId = Double.parseDouble(id);
      
            List<PaymentGetResponse> paymentRecords = paymentService.getPaymentById(studentId);
            /*
             * if the paymentRecords list has no records of payment
             */
            if (paymentRecords.isEmpty()) {

                Helper.sendFailResponse("There are no payment records of student with id "+(int)studentId, 200, exchange);
            }

            Helper.sendSuccessResponseForPaymentRecord(paymentRecords, 200, exchange);
            return;

        } catch (NumberFormatException e) {

            Helper.sendFailResponse("student id must be a number.", 400,  exchange);
            return;
            
        } catch (IllegalArgumentException e) {

            Helper.sendFailResponse(e.getMessage(), 400,  exchange);
            return;

        } catch (Exception e) {

            Helper.sendFailResponse(e.getMessage(), 500,  exchange);
            return;

        }

    }
    
}
