# Project Title
Hostel Management System – Backend (Local APIs)

# Description :
A Java-based backend system to manage hostel operations. It provides local RESTful APIs to handle student registrations, room assignments, and payment tracking.

# Technologies used
->Java
->Hibernate
->Mysql


# Role – Administrator
As an administrator, you can:

-> Add, view, update, or delete Students
-> Add, view, or update Rooms and manage capacity
-> Record and fetch Payments of students


# Entities / Resources

->Student
->Room
->Payment

STUDENT APIs

Method                 Endpoint	                                 Description
   
GET	                   /student                    	           Get all students
GET	                   /student/{studentId}	                         Get a student by ID
GET                    /student?feeStatus=Paid                 Get students whose fee status is Paid
GET                    /student?feeStatus=Due                  Get students whose fee status is Due
GET                    /student?collegeName=college            Get students belonging to a specific college
GET                    /student?roomNumber=22                  Get students belonging to a specific room
GET                    /student?joiningDate=2025-07-05         Get students who joined hostel on a specific date
POST	                 /student	                               Add a new student
PUT	                   /student/{studentId}	                         Update student details
DELETE	               /student/{studentId}	                         Remove a student

FOLLOWING IS A DETAILED DESCRIPTION ABOUT THE ABOVE MENTIONED METHODS ALONG WITH ENDPOINTS, DESCRIPTION

  1.You can fetch a list of students whose fee status is Paid

  Endpoint
  /student?feeStatus=Paid

  2.You can fetch a list of students whose fee status is Due

  Endpoint
  /student?feeStatus=Due

  3.You can fetch a list of students belonging to a specific college (Available in the database)

  Endpoint
  /student?collegeName={}

  4.You can fetch a list of students belonging to a specific room

  Endpoint
  /student?roomNumber=12

  5.You can fetch a list of students joined on a specicfic date

  Endpoint
  /student?joiningDate=2025-07-04
  

FOR POST METHOD OF STUDENT API, you will have to provide the following details of a student

  "studentId": 100,
  "name": "Parker",
  "contactNumber": "0987654321",
  "roomNumber": 29,
  "collegeName": "KPRIT",
  "paidForMonths" : null


PUT METHOD INCLUDES THE FOLLOWING REQUESTS

  1.You can update a student's contact number

  Endpoint
  /student/studentId
  ->Body should include the updating field 

  2.You can update a student's room number

  Endpoint
  /student/studentId
  -.Body should include the updating field

  3.You can update the fee status(Only when you want to change to Paid status, not supported to change to Due)

  EndPoint
  /student/studentId
  ->Body includes the feeStatus = "Paid"
  
DELETE METHOD INCLUDES FOLLOWING

  1.You can delete a student by student id

  Endpoint
  /student/studentId

NOT SUPPORTED FUNCTIONALITIES

  ->Combination of path & query parameters are not allowed
  ->Multiple query parameters are not allowed


# ROOM APIs

Method                 Endpoint	                                                  Description

POST                   /room                                                   add a new room
GET	                   /room                    	                             Get all rooms
GET                    /room/{roomNumber}                                      Get a specific room
GET                    /room?roomsAvailable=true                               Get available rooms
GET                    /room?roomsAvailable=false                              Get occupied rooms
GET                    /room?roomsAvailable=true&capacity={number}             Get available rooms with specified capacity
PUT                    /room/{roomNumber}                                      Update room fee
PUT                    /room/{roomNumber}                                      Update room cpacity
DELETE                 /room/{roomNumber}                                      Delete a room



POST METHOD OF ROOM 
  
  1.You can add a new room to the database, providing required following details in the request

  Endpoint
  /room
  Body: "roomNumber" : 12
        "capacity" : 4
        "bedsAvailable" : 4
        "fee" : 6000
        
  
GET METHOD INCLUDES THE FOLLOWING REQUETS

  1.You can fetch a list of all rooms'
  Endpoint
  /room

  2.You can fetch a single room details
  Endpoint
  /room/roomnumber

  3.You can fetch a list of available rooms

  Endpoint
  /room?roomsAvailable=true

  4.You can fetch a list of occupied rooms

  Endpoint
  /room?roomsAvailable=false

  5.You can fetch a list of available rooms with specified capacity

  Endpoint
  /room?roomsAvailable=true&capacity=3


PUT METHOD REQUEST INCLUDES THE FOLLOWING REQUESTS

  1.You can update fee of room
  
  Endpoint
  /room/room number
  body: "fee" : 7000

  2.You can update the capacity of a room

  Endpoint
  /room/room number

  Request body should include two necessary fields.They are
  1.capacity
  2.bedsAvailable


DELETE METHOD INCLUDES 

  1.You can delete a room from the database

  Endpoint
  /room/room number.

  NOT SUPPORTED REQUETS
  ->Combinations of path & query parameters are not allowed


PAYMENT APIs

   Method                 Endpoint	                                 Description
   
   GET	                   /payment                    	           Get all student's payment records
   GET	                   /payment/{studentId}	                   Get a specific student payment records


  ->This entity currently supports only GET method request.

  ->You can fetch a list of all students payment records

  Endpoint
  /payment

  ->You can fetch a list of payment records of a particular student
  
  Endpoint
  /payment/studentId


# HOW TO USE 
  
1.Clone the repository.
2.Run the application using Java.Replace the database credentials with your credentials for localhost system
3.Use Postman or any API client to hit the above endpoints.                                                                                   
