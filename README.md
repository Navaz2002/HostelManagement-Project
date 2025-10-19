# Project Title
Hostel Management System – Backend (Local APIs)

# Description :
A Java-based backend system to manage hostel operations. It provides local RESTful APIs to handle student registrations, room assignments, and payment tracking.

# Technologies used
->Java<br>
->Hibernate<br>
->Mysql<br>


# Role – Administrator
As an administrator, you can:<br>
<br>
-> Add, view, update, or delete Students<br>
-> Add, view, or update Rooms and manage capacity<br>
-> Record and fetch Payments of students<br>
<br>

# Entities / Resources
<br>
->Student<br>
->Room<br>
->Payment<br>
<br>
STUDENT APIs
<br>
| Method | Endpoint                                 | Description                                       |
|--------|------------------------------------------|---------------------------------------------------|
| GET    | `/student`                               | Get all students                                  |
| GET    | `/student/{studentId}`                   | Get a student by ID                               |
| GET    | `/student?feeStatus=Paid`                | Get students whose fee status is Paid             |
| GET    | `/student?feeStatus=Due`                 | Get students whose fee status is Due              |
| GET    | `/student?collegeName=college`           | Get students belonging to a specific college      |
| GET    | `/student?roomNumber=22`                 | Get students belonging to a specific room         |
| GET    | `/student?joiningDate=2025-07-05`        | Get students who joined hostel on a specific date |
| POST   | `/student`                               | Add a new student                                 |
| PUT    | `/student/{studentId}`                   | Update student details                            |
| DELETE | `/student/{studentId}`                   | Remove a student                                  |


FOLLOWING IS A DETAILED DESCRIPTION ABOUT THE ABOVE MENTIONED METHODS ALONG WITH ENDPOINTS, DESCRIPTION<br>
<br>
  1.You can fetch a list of students whose fee status is Paid<br>

  Endpoint<br>
  /student?feeStatus=Paid<br>
<br>
  2.You can fetch a list of students whose fee status is Due<br>

  Endpoint<br>
  /student?feeStatus=Due<br>
<br>
  3.You can fetch a list of students belonging to a specific college (Available in the database)<br>
<br>
  Endpoint<br>
  /student?collegeName={}<br>
<br>
  4.You can fetch a list of students belonging to a specific room<br>
<br>
  Endpoint<br>
  /student?roomNumber=12<br>
<br>
  5.You can fetch a list of students joined on a specicfic date<br>
<br>
  Endpoint<br>
  /student?joiningDate=2025-07-04<br>
  

FOR POST METHOD OF STUDENT API, you will have to provide the following details of a student<br>

  "studentId": 100,<br>
  "name": "Parker",<br>
  "contactNumber": "0987654321",<br>
  "roomNumber": 29,<br>
  "collegeName": "KPRIT",<br>
  "paidForMonths" : null<br>


PUT METHOD INCLUDES THE FOLLOWING REQUESTS<br>

  1.You can update a student's contact number<br>

  Endpoint<br>
  /student/studentId<br>
  ->Body should include the updating field<br> 

  2.You can update a student's room number<br>

  Endpoint<br>
  /student/studentId<br>
  -.Body should include the updating field<br>

  3.You can update the fee status(Only when you want to change to Paid status, not supported to change to Due)<br>

  EndPoint<br>
  /student/studentId<br>
  ->Body includes the feeStatus = "Paid"<br>
  
DELETE METHOD INCLUDES FOLLOWING<br>

  1.You can delete a student by student id<br>

  Endpoint<br>
  /student/studentId<br>

NOT SUPPORTED FUNCTIONALITIES<br>

  ->Combination of path & query parameters are not allowed<br>
  ->Multiple query parameters are not allowed<br>


# ROOM APIs

| Method | Endpoint                                                  | Description                                        |
|--------|-----------------------------------------------------------|----------------------------------------------------|
| POST   | `/room`                                                   | Add a new room                                     |
| GET    | `/room`                                                   | Get all rooms                                      |
| GET    | `/room/{roomNumber}`                                      | Get a specific room                                |
| GET    | `/room?roomsAvailable=true`                               | Get available rooms                                |
| GET    | `/room?roomsAvailable=false`                              | Get occupied rooms                                 |
| GET    | `/room?roomsAvailable=true&capacity={number}`             | Get available rooms with specified capacity        |
| PUT    | `/room/{roomNumber}`                                      | Update room fee                                    |
| PUT    | `/room/{roomNumber}`                                      | Update room capacity                               |
| DELETE | `/room/{roomNumber}`                                      | Delete a room                                      |



POST METHOD OF ROOM<br> 
  
  1.You can add a new room to the database, providing required following details in the request<br>

  Endpoint<br>
  /room<br>
  Body: "roomNumber" : 12<br>
        "capacity" : 4<br>
        "bedsAvailable" : 4<br>
        "fee" : 6000<br>
        
  
GET METHOD INCLUDES THE FOLLOWING REQUETS<br>

  1.You can fetch a list of all rooms<br>
  <br>
  Endpoint<br>
  /room<br>

  2.You can fetch a single room details<br>
  Endpoint<br>
  /room/roomnumber<br>

  3.You can fetch a list of available rooms<br>

  Endpoint<br>
  /room?roomsAvailable=true<br>

  4.You can fetch a list of occupied rooms<br>

  Endpoint<br>
  /room?roomsAvailable=false<br>

  5.You can fetch a list of available rooms with specified capacity<br>

  Endpoint<br>
  /room?roomsAvailable=true&capacity=3<br>


PUT METHOD REQUEST INCLUDES THE FOLLOWING REQUESTS<br>

  1.You can update fee of room<br>
  
  Endpoint<br>
  /room/room number<br>
  body: "fee" : 7000<br>

  2.You can update the capacity of a room<br>

  Endpoint<br>
  /room/room number<br>

  Request body should include two necessary fields.They are<br>
  1.capacity<br>
  2.bedsAvailable<br>


DELETE METHOD INCLUDES<br> 

  1.You can delete a room from the database<br>

  Endpoint<br>
  /room/room number.<br>

  NOT SUPPORTED REQUETS<br>
  ->Combinations of path & query parameters are not allowed<br>


PAYMENT APIs<br>

| Method | Endpoint               | Description                           |
|--------|------------------------|---------------------------------------|
| GET    | `/payment`             | Get all students' payment records     |
| GET    | `/payment/{studentId}` | Get a specific student's payment records |



  ->This entity currently supports only GET method request.<br>

  ->You can fetch a list of all students payment records<br>

  Endpoint<br>
  /payment<br>

  ->You can fetch a list of payment records of a particular student<br>
  
  Endpoint<br>
  /payment/studentId<br>


# STEPS TO RUN THE PROJECT 
  
1.Clone the repository.<br>
2.Replace the database credentials with your credentials for localhost system.<br>
2.Run the application using your IDE.<br>
3.Use Postman or any API client to hit the above endpoints.<br>                                                                                   
