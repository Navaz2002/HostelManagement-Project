package com.hostel.management.sevice;

import java.time.LocalDate;
import java.util.List;


//import com.google.gson.Gson;
import com.hostel.management.Exception.DuplicatestudentException;
import com.hostel.management.dao.StudentDao;
import com.hostel.management.dto.request.StudentCreateRequest;
import com.hostel.management.dto.request.StudentUpdateRequest;
import com.hostel.management.dto.response.StudentCreateResponse;
import com.hostel.management.dto.response.StudentGetResponse;
import com.hostel.management.dto.response.StudentUpdateResponse;
//import com.hostel.management.model.Room;
import com.hostel.management.model.Student;
//import com.hostel.management.utility.Helper;
import com.hostel.management.validation.StudentValidator;
/*
 * This class is used to validate the details/fields it gets from the
 * handlerclass by any (POST, PUT, GET,DELETE) request and interacts
 * with StudentDao class methods to actully finish the task assigned
 * to it by the caller.
 * 
 */
public class StudentService {

    private StudentDao studentDao = new StudentDao();
    private StudentValidator studentValidator = new StudentValidator();
    private StudentGetResponse studentGetResponse = new StudentGetResponse();

    /*
     * this method recieves the createStudentRequest object from the the handlePostRequest.
     * This request object is required for creation of Student.This object contains all the
     * fields sent by the client.
     * 
     * This method validates the fields, if any validation fails it throw back respective
     * exceptions.If everything is valid then it calls the studentDao save method for actual
     * creation of Student. 
     */
    public StudentCreateResponse addStudent(StudentCreateRequest createRequest) throws IllegalArgumentException,IllegalStateException,DuplicatestudentException  {

        /*
         * This condition checks if the name field is valid or not.It uses a isNameValid()
         * for the validation.
         */
        if (! studentValidator.isNameValid(createRequest.getName())) {
            throw new IllegalArgumentException("Invalid name.It must contain only alphabets.");
        }

        /*
         * This condition checks if the college name field is valid or not.It also
         * uses the same isNamValid() for validation.If validation fails throws respective
         * exception
         */
        if (! studentValidator.isNameValid(createRequest.getCollegeName())) {
            throw new IllegalArgumentException("Invalid college name.It must contain only alphabets.");
        }

        /*
         * This condition checks whether the contact field is valid or not, it
         * uses isContactValid() for validation.If fails throws respective exception
         */
        if (! studentValidator.isContactValid(createRequest.getContactNumber())) {
            throw new IllegalArgumentException("Invalid contact number.It must contain only digits from (0 - 9).");
        }
        /*
        * This condition checks whether provided room number for student
        * is positive or not, if not throws respective exception
        */
        if (! studentValidator.isPositive(createRequest.getRoomNumber())) {
            throw new IllegalArgumentException("room number must be a positive value");
        }

        /*
         * This condition checks provided paidForMonths field like its positive or not,
         * if any validation fails throws back respective exception
         */
            
         if (! studentValidator.isPositive(createRequest.getPaidForMonths())) {
            throw new IllegalArgumentException("paidformonths field value must be positive");
        }
        

        try {

            /*
            * This checks whether the provided room number for student has avilable beds or
            * not, if not it throws respective exception
            */
            if (! studentDao.roomExist(createRequest.getRoomNumber())) {
                throw new IllegalArgumentException("Room is unavaialable.");
            }

            /*
             * this checks if provided student id already exists in the database or not.It uses
             * doesIdExist() for this validation.If exists means its a duplicate id so throws
             * respective exception
             */
            if (studentDao.doesIdExist(createRequest.getStudentId())) {
                throw new DuplicatestudentException("Duplicate student id "+createRequest.getStudentId()+" , already exists in the database.");
            }

            // calling the DAO'S method for actual creation of student  
            Student student = studentDao.saveStudent(createRequest);
            /*
             * creating a StudentCreateResponse object for sending response,this
             * object contains the details of newly created Student .
             */
            StudentCreateResponse studentResponse = new StudentCreateResponse(student);

            return studentResponse;

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (DuplicatestudentException e) {

            throw e;

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {

            throw e ;

        }
    
    }

    /*
     * this method receives the getStudentRequest object from pathParameter request
     * for the retrieval of student by id from handleGetRequest method.
     * 
     * this method validates the student id, if any validation fails it throws
     * respective exception back to the caller.
     * 
     * If all validations complete then it calls the dao's fetchStudentById() to
     * actually fetch the student object from database.
     */
    public StudentGetResponse getStudentById(double studentId) {

        /*
         * this validates whether the provided student id is valid integer or not.
         * It uses isInteger() for validation.If validation fails throws respective
         * exception
         */
        if (! studentValidator.isInteger(studentId)) {

            throw new IllegalArgumentException("Invalid student id.Id must be an integer value.");

        }

        if (! studentValidator.isPositive((int) studentId)) {

            throw new IllegalArgumentException("Invalid student id.Id must be a postive value.");

        }

        try {

            /*
             * this validates whether provided student id exists in the dtabase or not.
             * It uses doesIdExist() for validation.If validation fails means the id is 
             * invalid does not exist so throws respective exception back
             */
            if (! studentDao.doesIdExist((int)studentId)) {
                throw new IllegalArgumentException("Provided student id does not exist in database.Provide valid id.");
            }

            //calling the DAo's fetchStudentById() to get the student object
            StudentGetResponse studentGetResponse = studentDao.fetchStudentById((int)studentId);
            return studentGetResponse;

        } catch (Exception e) {
            throw e;
        }
       
    }

    /*
     * this method is called from handleGetRequest for only path request
     * where the client wants to get all students.So this method calls the
     * dao's fetchAllStudents() to get a list of Students and return to the 
     * caller
     */
    public List<StudentGetResponse> getAllStudents() {

        try {

            //calling and receiving the list of students
            List<Student> studentsList = studentDao.fetchAllStudents();
            /*
             * getting the StudentGetResponse object list with exact details of students as studentsList
             * of type Student.
             * This object list is used for sending the response for get methods.
             */
            List<StudentGetResponse> studentResponseList = studentGetResponse.getStudentGetResponseList(studentsList);

            return studentResponseList;

        } catch (Exception e) {
            throw e;
        }
        
    }


    /*
     * this method receives the collegeName from the handler class to 
     * get the list of students belonging to the provided college.
     * 
     * this then calls the dao's fetchStudentsByCollege() to actually 
     * get the students belonging the provided college
     * 
     * Then it returns the recieved list from the dao's method 
     */
    public List<StudentGetResponse> getStudentsOfCollege(String collegeName) {
        try {

            /*
             * receiving the list of students belonging to the provided
             * college from the dao's fetchStudentsByCollege()
             */
            List<Student> studentList = studentDao.fetchStudentsByCollege(collegeName);
            /*
             * getting the StudentGetResponse object list with exact details of the student list
             * from the StudentGetResponse class.This list used for sending the response
             * to the get request
             */
            List<StudentGetResponse> studentResponseList = studentGetResponse.getStudentGetResponseList(studentList);
            return studentResponseList;

        } catch (Exception e) {

            throw e;
            
        }
    }

    /*
     * this method is called from the handler calss to get the
     * list of students by their feeStatus{Paid, due} 
     * 
     * This then calls the dao's fetchStudentsByFeeStatus() to get the 
     * list of students by feeStatus.Then it returns the StudentGetResponse object
     * list with exact student details as Student list.
     */
    public List<StudentGetResponse> getStudentsByFeeStatus(String feestatus) {
        
        try {
  
            List<Student> students = studentDao.fetchStudentsByFeeStatus(feestatus);
            List<StudentGetResponse> studentsList = studentGetResponse.getStudentGetResponseList(students);
            return studentsList;
        } catch (Exception e) {
            throw e;
        }

    }
    
    /*
     * This method is called to get the students belonging to 
     * a specific room provided to this method.
     * 
     * This first checks whether the provided room number exists in
     * the database or not,if not throws respective exception back.
     * 
     * After completing the validation then calls the dao's fetchStudentsByRoomNumber() to
     * get the students belonging to that room.
     * 
     * After getting student list, this uses the list to get the StudentGetResponse
     * object list for sending response.
     */
    public List<StudentGetResponse> getStudentsByRoomNumber(double roomNumber) {

        /*
         * This checks whether the room number provided is
         * valid or not.It uses the isInteger() for validation.
         * if validation fails throws respective exception.
         */
        if (! studentValidator.isInteger(roomNumber)) {
            throw new IllegalArgumentException("Room number must be an integer.");
        }
        /*
         * this condition checks if provided room number is positive or not
         * if not it throws respoective exception
         */
        if (! studentValidator.isPositive((int) roomNumber)) {
            throw new IllegalArgumentException("Room number must be a positive value.");
        }
        
        try {

            List<Student> students = studentDao.fetchStudentsByRoomNumber(roomNumber);
            List<StudentGetResponse> studentsofRoom = studentGetResponse.getStudentGetResponseList(students);
            return studentsofRoom;

        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * this method is called for getting the students who joined on
     * a specific date.
     * 
     * This calls the dao's fetchStudentsByJoiningDate() to actually get the students
     * who joined on specific date.
     * 
     * Then returns the studentGetResponse object list containing the students
     * joined on specific date.
     */
    public List<StudentGetResponse> getStudentsByJoiningDate(LocalDate joiningDate) {

        try {

            List<Student> students = studentDao.fetchStudentsByJoiningdate(joiningDate);
            List<StudentGetResponse> studentsList = studentGetResponse.getStudentGetResponseList(students);

            return studentsList;

        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * This method validates the student id for the removal of student
     * operation.If any validation fails it throws respective exception back.
     * 
     * If all validations complete, then it calls the dao's deleteeStudentById()
     * to actually delete the student from database.
     */
    public void removeStudent(double studentId) throws Exception {
        
        /*
         * this validates if the provided student id is valid or
         * not,it uses isInteger() for validation.If validation fails throws 
         * respective exception.
         */
        if (! studentValidator.isInteger(studentId)) {
            throw new IllegalArgumentException("Student Id must be an integer value.");
        }

        /*
         * This validates whether provided id exists in the database or
         * not.It use dao's  doesIdExist() for this.If validation fails means
         * the id is invalid so throws respective exception.
         * 
         */
        if (! studentDao.doesIdExist((int)studentId)) {
            throw new IllegalArgumentException("Student Id "+studentId+ " does not exist in the database.Provide valid id.");
        }
        
        try {

            //calling actual dao's method to delete the student by id.
            if (! studentDao.deleteStudent((int) studentId)) {
                throw new Exception("Delete operation failed due to unexpected error.");
            }

        } catch (Exception e) {
            throw e;
        }
    }


    /*
     * 
     */
    public StudentUpdateResponse updateStudentContact(StudentUpdateRequest contactRequest, double studentId) {
        

        /*
         * This validates whether provided contact valid or not.
         * It uses isContactValis() for validation.If validation fails 
         * then throws respective exception.
         */
        if (! studentValidator.isContactValid(contactRequest.getContactNumber())) {

            throw new IllegalArgumentException("Invalid contact number. Contact numbers must be of 10 digits (0 - 9)");

        }

        /*
         * This validates whether provided new contact of student already
         * exists in the database or not.If it exists means its a duplicate
         * contact so throws respective exception.
         * 
         * It uses doesContactExist() for validation
         */
        if (studentDao.doesContactExist(contactRequest.getContactNumber())) {

            throw new IllegalArgumentException("Duplicate contact number.Contact number "+contactRequest.getContactNumber()+ " already exists in the database.");

        }

        /*
         * This validates whether provided student id for contact update is
         * valid or not.If validation fails, then its invalid id so throws
         * respective exception.
         */
        if (! studentValidator.isInteger(studentId)) {

            throw new IllegalArgumentException("Student id must be an integer value.");

        }

        /*
         * This validates whether provided student id exists in the database or 
         * not, if it does not exist means the id is invalid so throws respective
         * exception
         * 
         * It uses dao's doesIdExist() for validation.
         */
        if (! studentDao.doesIdExist((int) studentId)) {

            throw new IllegalArgumentException("Invalid student id , student id "+studentId+" does not exist in the database.");

        }

        try {

            //calling the dao's updateStudentContactById() to actually update
            //student's contact.
            Student updatedFeeStatus = studentDao.updateStudentContactById((int) studentId, contactRequest);
            StudentUpdateResponse updateResponse = new StudentUpdateResponse(updatedFeeStatus);

            return updateResponse;

        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * this method basically returns the response object of student after
     * student object was updated (update may be contact, roomNumber).
     * 
     */
    public StudentUpdateResponse getStudentAfterUpdate(double studentId) {

        /*
         * This checks whether student id is valid integer value
         * or not.if not throws respective exception
         */
        if (! studentValidator.isInteger(studentId)) {
            
            throw new IllegalArgumentException("Student id cannot be a floating value, it must be an integer value.Provide valid id.");

        }

        /*
         * This checks whether provided student id exists in the database
         * or not.If not means its an invalid id so throws respective exception
         */
        if (! studentDao.doesIdExist((int) studentId)) {

            throw new IllegalArgumentException("Student id " +studentId+" does not exist in database.");

        }

        try {

            Student student = studentDao.fetchStudent((int)studentId);
            int roomNum = studentDao.fetchRoomNumByStudentId((int) studentId);

            StudentUpdateResponse contactUpdateResponse = new StudentUpdateResponse(student);
            contactUpdateResponse.setRoomNumber(roomNum);

            return contactUpdateResponse;

        } catch (Exception e) {
            
            throw e;
        }

    }

    /*
     * this method receives the request object for updating room number of a student.It
     * also receives the student id, first it validates the student id, room number to make sure
     * client has sent correct values or not, if not it throws respective exceptions to the caller.
     * If everything is valid it further calls the studentDao's roomnumber update method for actual
     * update.
     * @return it returns true if update was successful, false if not
     */
    public StudentUpdateResponse updateStudentRoomNum(StudentUpdateRequest roomNumUpdate, double studentId) {
        /*
         * this condition checks whether the studentId type is integer or not
         * if not it throws illegalArgument exception
         */
        if (! studentValidator.isInteger(studentId)) {

            throw new IllegalArgumentException("Student id must be an integer value.");

        }

        /*
         * this condition checks whether provided room number is valid or not, 
         * does it exist in the database or not.If it does not exist it throws
         * the illegalArgument exception
         */
        if (! (roomNumUpdate.getRoomNumber() > 0) ||  ! (roomNumUpdate.getRoomNumber() <= 30)) {

            throw new IllegalArgumentException("Invalid room number.Room number " +roomNumUpdate.getRoomNumber()+ " does not exist.");

        }

        try {

            /*
             * this condition checks whether provided student id exists in the database or
             * not , if not it throws illegalArgument exception
             */
            if (! studentDao.doesIdExist((int) studentId)) {

                throw new IllegalArgumentException("Student id does not exist in the database.Provide valid id.");

            }

            /*
             * this condition checks whether provided room has available
             * beds or not.It throws exception if there are no available beds
             * in the room.
             */
            if (! studentDao.roomExist(roomNumUpdate.getRoomNumber())) {

                throw new IllegalStateException("Cannot assign student.No beds available in the room "+roomNumUpdate.getRoomNumber());

            }

            Student student  = studentDao.updateStudentRoomNumById((int) studentId, roomNumUpdate);

            StudentUpdateResponse updateResponse = new StudentUpdateResponse(student);
            return updateResponse;


        } catch (Exception e) {

            throw e;

        }
    }

    public StudentUpdateResponse updateFeeStatusOfStudent(StudentUpdateRequest feeStatusRequest, double studentId) throws Exception {
        /*
         * this condition checks whether provided string value for feeStatus
         * is valid string or not, if not throws respective exception.
         */
        if (! studentValidator.isNameValid(feeStatusRequest.getFeeStatus())) {

            throw new IllegalArgumentException("Fee status field is invalid.Does not include valid string.");   

        }
        /*
         * this condition checks if string value is Paid or not
         * if not throws respective exception
         */
        if (! feeStatusRequest.getFeeStatus().matches("Paid")) {

            throw new IllegalArgumentException("Fee status field value must be Paid.");   

        }
        /*
         * this checks provided paidForMonths field is valid or not
         * like its integer,positive or not.
         * It throws respective exceptions if any validation fails
         */

        if (! studentValidator.isInteger(feeStatusRequest.getPaidforMonths())) {
            throw new IllegalArgumentException("paidformonths field must be an integer value.");   
        }

        if (! ( feeStatusRequest.getPaidforMonths() > 0 ) ) {
            throw new IllegalArgumentException("paidformonths value must be a positive number.");   
        }


        try {
            /*
             * calling the method to actually update the feeStatus
             */
            Student student = studentDao.updateFeeStatusById(studentId, feeStatusRequest);
            /*
             * if received sobject after update is null means update was failed
             * so throws respective exception
             */
            if (student == null) {

                throw new Exception("update was unsuccessful due to unexpected error.");

            }
            /*
             * assigning the updated details of Student object to StudentUpdateResponse
             * object which is used to send the response
             */
            StudentUpdateResponse feeStatusResponse = new StudentUpdateResponse(student);

            return feeStatusResponse;

        } catch (Exception e) {

            throw e;
            
        }
    }

    
}
