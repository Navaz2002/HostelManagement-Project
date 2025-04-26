package com.hostel.management;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//import java.lang.module.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.SourceType;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.spi.SourceDescriptor;

import com.hostel.management.model.Payment;
import com.hostel.management.model.Room;
import com.hostel.management.model.Student;

import jakarta.persistence.Query;

//import com.mysql.cj.Query;

public class Main {
     public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);

        Configuration config = new Configuration().configure().addAnnotatedClass(Student.class).addAnnotatedClass(Room.class).addAnnotatedClass(Payment.class);
        SessionFactory factory = config.buildSessionFactory();

        addRooms(factory);
        System.out.println();
        System.out.println("Welcome to Hostel Managment :");
        System.out.println("Please select the options as per your requirement!");
        System.out.println();

        boolean run = true;
        Integer operation = -1;

        while (run) {
            System.out.println("1.Enter 1 for student operations :");
            System.out.println("2.Enter 2 for room operations :");
            System.out.println("3.Enter 3 for payment operations :");
            System.out.println("0.To exit");

            try {
                operation = scanner.nextInt();
                System.out.println(operation);
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Input mismatch.");
                scanner.nextLine();
            }

            switch (operation) {
                case 1:
                    studentOperations(scanner, factory);
                    break;
                case 2:
                    roomOperations(scanner, factory);
                    break;
                case 3:
                    paymentOperations(scanner, factory);
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("Please select the correct option");
                    System.out.println();
                    break;
            }

        }

        factory.close();

        thankYou("Exiting");
    }

    private static void thankYou(String message) {
        System.out.print(message);
        for(int i=0; i<5; i++){
            System.out.print(".");
          
        }
    }

    private static void paymentOperations(Scanner scanner, SessionFactory factory) {
        
    }

    private static void roomOperations(Scanner scanner, SessionFactory factory) {
       // System.out.println("Welcome to room operations ");
        System.out.println();
        boolean run = true;

        while (run) {
            System.out.println("Please enter the option you require.");
            System.out.println();
            System.out.println("1.Check available rooms with beds");
            System.out.println("2.Get details of a room number");
            System.out.println("3.Add a new Room.");
            System.out.println("4.Update room features.");
            System.out.println("5.Get students of a room");
            System.out.println("6 to delete a room.");
            System.out.println("0.Exit.");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    showRooms(factory);
                    break;
                case 2:
                    getDetailsOfRoom(scanner,factory);
                    break;
                case 3:
                    addRoom(scanner,factory);
                    break;
                case 4:
                    updateRoom(scanner, factory);
                    break;
                case 5:
                    getStudentsOfRoom(scanner, factory);
                    break;
                case 6:
                    deleteRoom(scanner, factory);
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("Enter the valid option.");
                    break;
            }
        }
    }

    private static void deleteRoom(Scanner scanner, SessionFactory factory) {

        int roomNumber;
        try {
            System.out.println("Enter room number to delete room");
            roomNumber = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type.Require integer but provided other.");
            scanner.nextLine();
            return;
        }

        if(! doesRoomExist(roomNumber, factory)){
            System.out.println("Provided room number does not exist in the databse.Provide valid room number.");
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        String hql = "DELETE FROM Room WHERE roomNumber = :roomNumber";
        Query q = session.createQuery(hql);
        q.setParameter("roomNumber", roomNumber);
        int result = q.executeUpdate();

        if (result != 0) {
            System.out.println("Room number "+roomNumber+ " delted from database successfully");
        } else {
            System.out.println("Operation failed.");
        }

        session.getTransaction().commit();
        session.close();


    }

    private static void getStudentsOfRoom(Scanner scanner, SessionFactory factory) {
        
        int roomNumber;
        try {
            System.out.println("Please enter room number to get its students info");
            roomNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input.Required integer but provided other.");
            return;
        }

        if(! doesRoomExist(roomNumber, factory)){
            System.out.println("Room number "+roomNumber+ " does not exist in the database");
            System.out.println("Enter valid room number");
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, roomNumber);
        List<Student> studentList = room.getStudentList();
        if(studentList != null) {
            for (Student student : studentList) {
                System.out.println(student);
            }
        }else {
            System.out.println("There are no students in this room ");
        }
       
        session.getTransaction().commit();
        session.close();
    }

    private static void updateRoom(Scanner scanner, SessionFactory factory) {
        System.out.println("Please select the operation you require.");
        boolean run = true;

        while (run) {
            System.out.println("Select the option");
            System.out.println();
            System.out.println("1.Update room cpacaity");
            System.out.println("2.update fee.");
            System.out.println("3.Update both capacity and fee.");
            System.out.println("0.Go back to main.");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    updateCapacity(scanner, factory);
                    break;
                case 2:
                    updateFee(scanner, factory);
                    break;
                case 3:
                    updateCapacity(scanner, factory);
                    updateFee(scanner, factory);
                    break;
                case 0:
                    run = false;
                default:
                    System.out.println("Please eneter valid option.");
                    break;
            }
        }
    }

    private static void updateFee(Scanner scanner, SessionFactory factory) {
        boolean run = true;
        int option = 0;
        while (run) {
            System.out.println("Select the option .");
            System.out.println();
            System.out.println("1 to increase fee.");
            System.out.println("2 to decrease fee.");
            System.out.println("0 to go back.");

            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input type.Requires integer but provided other.");
                scanner.nextLine();
            }

            switch (option) {
                case 1:
                    increaseFee(scanner, factory);
                    break;
                case 2:
                    decreaseFee(scanner, factory);
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    break;
            }
        }

    }

    private static void decreaseFee(Scanner scanner, SessionFactory factory) {
        int roomNumber;
        int decreasedFee;

        try {
            System.out.println("Enter the room number.");
            roomNumber = scanner.nextInt();
            scanner.nextLine();
            if(! doesRoomExist(roomNumber, factory)){
                System.out.println("Provided room number does not exist in the database.");
                System.out.println();
                return;
            }
            System.out.println("Enter the new fee.");
            decreasedFee = scanner.nextInt();
            scanner.nextLine();
            Room room = getRoomObject(roomNumber, factory);
            if(room.getFee() <= decreasedFee){
                System.out.println("New fee must be less than the existing one.");
                System.out.println();
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input type.Required integer but provide other.");
            System.out.println();
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, roomNumber);
        room.setFee(decreasedFee);

        session.getTransaction().commit();
        session.close();

        System.out.println("Fee has been reduced successfully.");
        System.out.println(); 
    }

    private static void increaseFee(Scanner scanner, SessionFactory factory) {
        int roomNumber;
        int increasedfee;

        try {
            System.out.println("Enter the room number.");
            roomNumber = scanner.nextInt();
            scanner.nextLine();
            if(! doesRoomExist(roomNumber, factory)){
                System.out.println("Provided room number does not exist in the database.");
                return;
            }
            System.out.println("Enter the new fee for room number "+roomNumber);
            increasedfee = scanner.nextInt();
            scanner.nextLine();
            Room room = getRoomObject(roomNumber, factory);
            if (room.getFee() > increasedfee || room.getFee() == increasedfee) {
                System.out.println("Provided new fee is lesser than or equal to existing fee.");
                System.out.println("New fee should be greater than existing one.");
                System.out.println();
                return;
            }
        } catch (Exception e) {
            System.out.println("Invalid input type.Required integer but provided other.");
            scanner.nextLine();
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, roomNumber);
        room.setFee(increasedfee);

        session.getTransaction().commit();
        session.close();

        System.out.println("Fee updated successfully.");
        System.out.println();

        
    }

    private static void updateCapacity(Scanner scanner, SessionFactory factory) {
        boolean run = true;
         while (run) {
            System.out.println("Seelect the option as per requirement.");
            System.out.println("1.Increase capacity");
            System.out.println("2.decrease capacity.");
            System.out.println("3.Increase capacity for rooms ");
            System.out.println("Decrease capacity for rooms");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    increaseCapacity(scanner, factory);
                    break;
                case 2:
                    decreaseCapacity(scanner, factory);
                    break;
                case 3:
                    increaseCapacityOfRooms(scanner, factory);
                    break;
                case 4:
                    decreaseCapacityOfRooms(scanner, factory);
                    break;
                case 5:
                    setAvialableBeds(scanner, factory);
                case 0:
                    run = false;   
                    break;
                default:
                    System.out.println("Enter valid option");
                    break;
            }
         }

       
    }

    /*
     * This method is used to update the available beds of provided room
     * number.
     */
    private static void setAvialableBeds(Scanner scanner, SessionFactory factory) {
        int updatedAvailableBeds;
        int roomNumber;

        try {
            System.out.println("Enter the room number to update its available beds.");
            roomNumber = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type..Required room number but provided other.");
            scanner.nextLine();
            return;
        }

        if(! doesRoomExist(roomNumber, factory)){
            System.out.println("Provided room number does not exist in the database.Provide valid room number.");
            return;
        }

        try {
            System.out.println("Enter the available beds for room number "+roomNumber);
            updatedAvailableBeds = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type.Required integer value but provide other.");
            scanner.nextLine();
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, roomNumber);

        if(updatedAvailableBeds > room.getCapacity()){
            System.out.println("Provided available beds for room number "+roomNumber+ " exceeds the room capacity.Provide available beds within capacity.");
            session.getTransaction().commit();
            session.close();
            return;
        }

        room.setBedsAvailable(updatedAvailableBeds);
        session.getTransaction().commit();
        session.close();

        System.out.println("Available beds for room number "+roomNumber+ " updated successfully.");

        
    }

    private static void decreaseCapacityOfRooms(Scanner scanner, SessionFactory factory) {
        
    }

    /*
     * This method is used to increase the cpacity of provided room numbers.
     * It works when the new increasing capacity is same for n room numbers.
     */

    private static void increaseCapacityOfRooms(Scanner scanner, SessionFactory factory) {

        int noOfRooms;
        try {
            System.out.println("Please provide the number of rooms whose capacity you want to increase");
            noOfRooms = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input.Required integer but provide other.");
            scanner.nextLine();
            return;
        }

        System.out.println("Please provide the room numbers whose new increasing capacity is same");
        List<Integer> roomNumbers = new ArrayList<>();

        for(int i=0; i<noOfRooms; i++){
            try {
                roomNumbers.add(scanner.nextInt());
                if( ! doesRoomExist(roomNumbers.get(i), factory)){
                    System.out.println("Provided room does not exist in the database.");
                    //scanner.nextLine();
                    return;
                }
            } catch (Exception e) {
                System.out.println("Invalid input.Required number but provided other");
                scanner.nextLine();
                return;
            }
        }
        scanner.nextLine();

        int increasedCapacity ;
        try {
            System.out.println("Please eneter the new capacity for all the mentioned rooms");
            increasedCapacity = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input.Required number but provided other");
            scanner.nextLine();
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        for (int i=0; i<noOfRooms; i++) {

            Room room = session.get(Room.class, roomNumbers.get(i));
            int olderCapacity = room.getCapacity();
            int oldBeds = room.getBedsAvailable();
            room.setCapacity(increasedCapacity);

            if (oldBeds == 0) {
                room.setBedsAvailable(increasedCapacity - olderCapacity);
            }else if(oldBeds == olderCapacity) {
                room.setBedsAvailable(increasedCapacity);
            }else{
                room.setBedsAvailable(increasedCapacity - (olderCapacity - oldBeds));
            }

        }

        session.getTransaction().commit();
        session.close();

        System.out.println("Capacity increased for the given rooms successfully.");
        
    }

    private static void decreaseCapacity(Scanner scanner, SessionFactory factory) {

        int roomNumber;
        try {
            System.out.println("Enter room number to decrease capacity.");
            roomNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input.Required integer but provided other.");
            return;
        }
        if(! doesRoomExist(roomNumber, factory)){
            System.out.println("Room number "+roomNumber+" does not exist in the database");
            System.out.println("Provide valid room number");
            return;
        }

        int newCapacity;
        try {
            System.out.println("Enter new capacity for room number "+roomNumber);
            newCapacity = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input.Required integer but provide other.");
            return;
        }

        int updatedBeds ;
        try {
            System.out.println("Please enter the avialble beds for this new capacity of the room "+roomNumber);
            updatedBeds = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input.Required integer but provided other.");
            scanner.nextLine();
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, roomNumber);
        room.setCapacity(newCapacity);
        room.setBedsAvailable(updatedBeds);

        session.getTransaction().commit();
        session.close();

        System.out.println("capacity for room number "+roomNumber+ " updated successfully.");

    }

    private static void increaseCapacity(Scanner scanner, SessionFactory factory) {
        
        int roomNumber;
        try {
            System.out.println("Enter room number to increase capacity.");
            roomNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input.Required integer but provided other.");
            return;
        }
        if(! doesRoomExist(roomNumber, factory)){
            System.out.println("Room number "+roomNumber+" does not exist in the database");
            System.out.println("Provide valid room number");
            return;
        }

        int newCapacity;
        try {
            System.out.println("Enter new capacity for room number "+roomNumber);
            newCapacity = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input.Required integer but provide other.");
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, roomNumber);
        int oldCpacity = room.getCapacity();
        room.setCapacity(newCapacity);

        int updatedBeds = room.getBedsAvailable();
        if(updatedBeds == 0){
            updatedBeds = (newCapacity - oldCpacity);
        }else{
            updatedBeds = newCapacity - updatedBeds;
        }
       
        room.setBedsAvailable(updatedBeds);

        session.getTransaction().commit();
        session.close();

        System.out.println("capacity increased for room number "+roomNumber+ " successfully.");

    }

    /*
     * This method is used to add a new room into the dsatabase
     * it adds the room after full validation only.
     */
    private static void addRoom(Scanner scanner, SessionFactory factory) {

        int roomNumber;
        try {
            System.out.println("Please enter room number");
            roomNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Input mismatch.Require integer but provided other.");
            System.out.println();
            return;
        }

        if(doesRoomExist(roomNumber, factory)){
            System.out.println("Room number "+roomNumber+" already exists in database.");
            return;
        }

        int capacity;
        try {
            System.out.println("Enter its capacity : ");
            capacity = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Input mismatch.Required integer but provided other.");
            return;
        }

        int fee;
        try {
            System.out.println("Enter its fee : ");
            fee = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Input mismatch.Required integer but provided other.");
            return;
        }

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setCapacity(capacity);
        room.setBedsAvailable(capacity);
        room.setFee(fee);

        Session session = factory.openSession();
        session.beginTransaction();

        session.persist(room);

        session.getTransaction().commit();
        session.close();

        System.out.println("New room added to the database successfully");
        System.out.println();

      
    }

    private static void getDetailsOfRoom(Scanner scanner, SessionFactory factory) {
       
        int roomNumber;
        try {
            System.out.println("Please enter the room number to get its details.");
            roomNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Input mismatch.Required integer but provided other.");
            return;
        }

        if(! doesRoomExist(roomNumber, factory)){
            System.out.println("Provided room number does not exist in database.Provide valid room number");
            System.out.println();
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, roomNumber);
        System.out.println(room);
        System.out.println("");
        // List<Student> studentList = room.getStudentList();
        // for (Student student : studentList) {
        //     System.out.println(student);
        // }

        session.getTransaction().commit();
        session.close();
    }

    private static void studentOperations(Scanner scanner, SessionFactory factory) {
        boolean run = true;
        while (run) {
            System.out.println("Please select the options you require");
            System.out.println();
            System.out.println("1.Add a new student.");
            System.out.println("2.Get details of a student.");
            System.out.println("3.Get details of all students.");
           // System.out.println("4.Get a list of student who did not pay fee.");
           // System.out.println("5.Get a list of students who paid fee.");
            System.out.println("6.Update details of a student");
            System.out.println("7.Delete a student");
            System.out.println("0.To go back to main operations interface");
            int option = -1;
            try {
                option = scanner.nextInt();
                scanner.nextLine();

            } catch (Exception e) {
                System.out.println("Input mismatch.Require integer value but provide other.");
                scanner.nextLine();
            }

            switch (option) {
                case 1:
                    addStudent(scanner, factory);
                    break;
                case 2:
                    getDetailsOfStudent(scanner, factory);
                    break;
                case 3:
                    getDetailsOfStudents(factory);
                    break;
                case 4:
                    getDueList(factory);
                    break;
                case 5:
                    getPaidList(factory);
                    break;
                case 6:
                    updateStudent(scanner, factory);
                case 7:
                    deleteStudent(scanner, factory);
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("Please select the valid option");
                    break;
            }
    
        }
        // System.out.println("Please select the options you require");
        // System.out.println();
        // System.out.println("1.Add a new student.");
        // System.out.println("2.Get details of a student.");
        // System.out.println("3.Get details of all students.");
        // System.out.println("4.Get a list of student who did not pay fee.");
        // System.out.println("5.Get a list of students who paid fee.");
        // System.out.println("6.Update details of a student");
        // int option = scanner.nextInt();
        // scanner.nextLine();

        // switch (option) {
        //     case 1:
        //         addStudent(scanner, factory);
        //         break;
        //     case 2:
        //         getDetailsOfStudent(scanner, factory);
        //         break;
        //     case 3:
        //         getDetailsOfStudents(factory);
        //         break;
        //     case 4:
        //         getDueList(factory);
        //         break;
        //     case 5:
        //         getPaidList(factory);
        //         break;
        //     case 6:
        //         updateStudent(scanner, factory);
        //     default:
        //         break;
        // }
    }

    private static void deleteStudent(Scanner scanner, SessionFactory factory) {
       
        int studentId;
        try {
            System.out.println("Enter student id to delete the student record.");
            studentId = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.required integer but provided other.");
            return;
        }
        scanner.nextLine();

        if(! doesIdExist(factory, studentId)){
            System.out.println("Invalid id.Provided id does not exist in the database.");
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Student student = session.get(Student.class, studentId);
        Room room = session.get(Room.class, student.getRoomNumber());
        room.removeStudent(factory, studentId);

        String hql = "DELETE FROM Student WHERE studentId = :id";
        Query q = session.createQuery(hql);
        q.setParameter("id", studentId);
        int result = q.executeUpdate();

        int updatingBeds = room.getBedsAvailable();
        room.setBedsAvailable(updatingBeds + 1);

        session.getTransaction().commit();
        session.close();

        if (result == 1) {
            System.out.println("Student with id "+studentId+" successfully removed from database.");
        } else {
            System.out.println("Operation failed");
        }
        
    }

    private static void updateStudent(Scanner scanner, SessionFactory factory) {
        int studentId;
        try {
            System.out.println("Please enter student id to update details");
            studentId = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.Required integer but provided other");
            return;
        }

        if(! doesIdExist(factory, studentId)){
            System.out.println("Provided student id does not exist in the database.Provide valid id");
            return;
        }

        System.out.println("Please enter the option you require :");
        boolean run = true;
        while (run) {
            System.out.println("1.To update contact number.");
            System.out.println("2.To update room number.");
            System.out.println("3.To update fee status.");
            System.out.println("0.To go back");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    updateContactNumber(scanner, factory, studentId);
                    break;
                case 2:
                    updateRoomNumber(scanner, factory, studentId);
                    break;
                case 3:
                    updateFeeStatus(scanner, factory,studentId);
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("Please enter the valid option");
                    System.out.println();
                    break;
            }
        }
      
        

    }

    /*
     * This method is used to update the fee status of a student.
     * It updates as paid or due depending on the input provided by the administarator.
     * If provided as paid ,it updates status as paid,if providedas due it updates as due.
     */
    private static void updateFeeStatus(Scanner scanner, SessionFactory factory, int studentId) {

        String feeStatus;
        
        System.out.println("Enter the fee status (paid/due).Enter any one of two ,either paid or due.");
        feeStatus = scanner.nextLine();
        if(! isNameValid(feeStatus)){
            System.out.println("Invalid input.Do not enetr any specialcharacters.");
            System.out.println();
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Student student = session.get(Student.class, studentId);
        student.setFeeStatus(feeStatus, factory);

        session.getTransaction().commit();
        session.close();

        System.out.println("Fee status updated successfully.");
        System.out.println();


      
    }

    private static void updateRoomNumber(Scanner scanner, SessionFactory factory, int studentId) {
        int newRoomNumber;
        try {
            System.out.println("Pleas enter new room number ");
            newRoomNumber = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Input mismatch.Required integer but provided other");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        if(! doesRoomExist(newRoomNumber, factory)){
            System.out.println("Provided room number does not exist");
            return;
        }

        if(! areBedsAvailable(newRoomNumber,factory)){
            System.out.println("There are no beds available in room "+newRoomNumber);
            System.out.println("Choose another room");
            System.out.println();
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Student student = session.get(Student.class, studentId);
        int roomNumber = student.getRoomNumber();
        Room room = session.get(Room.class, roomNumber);
        room.removeStudent(factory, studentId);
        int avialableBeds = room.getBedsAvailable();
        room.setBedsAvailable(avialableBeds+1);  //Now one bed becomes avaialable in  old room

        Room newRoom = session.get(Room.class, newRoomNumber);
        student.setRoom(newRoom);
        avialableBeds = newRoom.getBedsAvailable();
        newRoom.setBedsAvailable(avialableBeds-1); //Now in new room avaialablebeds get decreased 

        session.getTransaction().commit();
        session.close();

        System.out.println();
        System.out.println("Room number updated successfully");
        System.out.println();
    }

    /*
     * This method is used to check the availble beds of provided room number.
     * It returns true if that room has beds,false if no bed available in that.
     */
    private static boolean areBedsAvailable(int newRoomNumber, SessionFactory factory) {
        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, newRoomNumber);

        session.getTransaction().commit();
        session.close();

        if(room.getBedsAvailable() != 0){
            return true;
        }
        return false;
    }

    /*
     * This method is used to check provided room number exists in the database or not
     * it return true if room exists,false if not
     */
    private static boolean doesRoomExist(int newRoomNumber, SessionFactory factory) {
        Session session = factory.openSession();
        session.beginTransaction();

        Room room = session.get(Room.class, newRoomNumber);

        session.getTransaction().commit();
        session.close();

        if(room != null)return true;
        return false;
    }

    private static void updateContactNumber(Scanner scanner, SessionFactory factory, int studentId) {

        System.out.println("Please enter student new contact number.");
        String newContactNumber = scanner.nextLine();

        if(! isContactValid(newContactNumber)){
            System.out.println("Invalid contact number.Contact number must have 10 digits without space:");
            return;
        }

        if(doesContactExist(factory, newContactNumber, studentId)){
            System.out.println("Duplicate contact found "+newContactNumber+". Please provide valid contact.");
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();

        Student student = session.get(Student.class, studentId);
        student.setContactNumber(newContactNumber);

        session.getTransaction().commit();
        session.close();

        System.out.println();
        System.out.println("Contact Number updated successfully.");
        System.out.println();

    }

    private static boolean doesContactExist(SessionFactory factory, String contactNumber, int studentId) {
        String hql = "SELECT contactNumber FROM Student WHERE contactNumber = :contactNumber AND studentId <> :studentId";
        Session session = factory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(hql);
        query.setParameter("contactNumber", contactNumber);
        query.setParameter("studentId", studentId);

        try {
            String contact = (String)query.getSingleResult();
            session.getTransaction().commit();
            session.close();
            // System.out.println("Duplicate contact found : "+contact);

            return true;
        } catch (Exception e) {
            session.getTransaction().commit();
            session.close();
            System.out.println("No duplicate .Proceed to update");
            return false;
        }

        

    }

    private static void getPaidList(SessionFactory factory) {
        String hql = "SELECT "
        Session session = factory.openSession();

    }

    private static void getDueList(SessionFactory factory) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDueList'");
    }

    /*
     * This method prints the details of all student from the database
     */
    private static void getDetailsOfStudents(SessionFactory factory) {

        Session session = factory.openSession();
        session.beginTransaction();
        String hql = "FROM Student ";

        Query query = session.createQuery(hql);
        List<Student> studentList = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (studentList != null) {
            for(Student student : studentList){
                System.out.println(student);
            }
        }else{
            System.out.println("There are no students in the database.");
        }

        System.out.println();
    }

    private static void getDetailsOfStudent(Scanner scanner, SessionFactory factory) {
        int studentId;

        try {
            System.out.println("Please enter student id to get details:");
            studentId = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Input mismatch.Required integer but provided other.");
            return;
        }

        Session session = factory.openSession();
        session.beginTransaction();
        Student student = session.get(Student.class, studentId);
        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("There is no student in the database with id "+studentId);
            System.out.println();
        }

        session.getTransaction().commit();
        session.close();
        return;
    }

    /*
    * This method is used to add a new student into the database
    * takes student info from console and type checks everything
    * once everything is completed it saves the student object into the database
    */
    private static void addStudent(Scanner scanner, SessionFactory factory) {
        if(! roomsExist(factory)){
            System.out.println("Rooms unavialble");
            return;
        }

        int studentId;

        try {
            System.out.println("Please enter student Id : ");
            studentId = scanner.nextInt();
            scanner.nextLine()
;        } catch (InputMismatchException e) {
            System.out.println("Input mismatch required integer but provided other");
            return;
        }

        if(doesIdExist(factory,studentId)){
            System.out.println("Provided id exists in the database.Provide another.");
            System.out.println();
            return;
        }
        
        System.out.println("Please enter student full name.Do not add any special characters other than alphabets.");
        String name = scanner.nextLine();
        if(! isNameValid(name)){
            System.out.println("Invalid name.Do not add any special charcaters other than alphabets");
            return;
        }

        System.out.println("Enter student contact number :");
        String contactNumber = scanner.nextLine();
        if(! isContactValid(contactNumber)){
            System.out.println("Invalid contact number.Enter only digits from 0 to 9.");
            return;
        }

        System.out.println("Enter student college name :");
        String collegeName = scanner.nextLine();
        if(! isNameValid(collegeName)){  //This method makes sure that provided name is valid
            System.out.println("Invalid name.Do not add any special characters.");
            return;
        }

        System.out.println("Enter joining date (yyyy-mm-dd) :");
        String date = scanner.nextLine();
        LocalDate joinDate;
        try {
            joinDate = LocalDate.parse(date);
        } catch (Exception e) {
            System.out.println("Please enter date in correct format (yyyy-mm-dd)");
            return;
        }

        /*
         * This method shows available rooms with available beds 
         * so that administrator can choose one room for student as student wishes to
         */
        showRooms(factory);
        System.out.println("Provide the room number for student from avialable rooms :");
        int roomNumber;
        try {
            roomNumber = scanner.nextInt();
        } catch (InputMismatchException e) {
           System.out.println("Input mismatch.Requires integer provided other.");
           return;
        }

        /*
         * this method provides the room object of the room number provided to student.We
         * get this room object to store in student object.
         */
        Room room = getRoomObject(roomNumber, factory);
        if(room == null){
            System.out.println("Provided room number does not exist in the database");
            return;
        }
        
        Student student = new Student();

        student.setStudentId(studentId);
        student.setName(name);
        student.setContactNumber(contactNumber);
        student.setCollegeName(collegeName);
        student.setJoiningDate(joinDate);
        student.setRoom(room);
        student.setFeeStatus("paid", factory);

        Session session = factory.openSession();
        session.beginTransaction();

        session.persist(student);

        Payment pay = new Payment();
        pay.setPaymentDateTime(LocalDateTime.now());
        pay.setStudent(student);

        session.persist(pay);


        room = session.get(Room.class, roomNumber);
        int availableBeds = room.getBedsAvailable();
        if(availableBeds != 0){
            room.setBedsAvailable(availableBeds - 1);
        }else{
            System.out.println("Provided room number has no beds available.Please check the room again.");
            session.getTransaction().commit();
            session.close();
            return;
        }

        session.getTransaction().commit();

        session.close();
  //may be you do not need removeBeds think here for this
       // room.removeBeds(factory, room.getRoomNumber());

        
        

        System.out.println("Student details saved successfully.");
        System.out.println();

    }

    /*
     * This method checks provided studentId exists in the db or not
     * if exists it returns true,if not false;
     */
    private static boolean doesIdExist(SessionFactory factory, int studentId) {

        Session session = factory.openSession();
        session.beginTransaction();
        
        Student student = session.get(Student.class, studentId);
        session.getTransaction().commit();
        session.close();

        if(student == null)return false;
        return true;

    }

    /*
     * This method returns the room object for given room number
     * 
     */
    private static Room getRoomObject(int roomNumber, SessionFactory factory) {
        Session session = factory.openSession();
        session.beginTransaction();
        Room room = session.get(Room.class, roomNumber);
        session.getTransaction().commit();
        return room;
    }

    /*
     * This method is used to show all available rooms with avaialabl beds in the
     * database.It helps the administrator to assighn the room for student on student wish
     */
    private static void showRooms(SessionFactory factory) {
        String query = "FROM Room WHERE bedsAvailable <> 0";
        Session session = factory.openSession();
        session.beginTransaction();

        Query q = session.createQuery(query);
        List<Room> list = q.getResultList();
    
        System.out.println("Available rooms ");
        for(Room room : list){
            System.out.println("Room number "+room.getRoomNumber()+" beds available "+room.getBedsAvailable());
        }

        session.getTransaction().commit();
        session.close();
    }

    /*
     * This method is used to check provided contact number is valid or not
     * contact number is valid if it has 10 digits without any space.
     * @return returns true if its valid,false if its not.
     */
    private static boolean isContactValid(String contactNumber) {

        return contactNumber != null && contactNumber.matches("\\d{10}");
    }

    /*
     * this method is used to check provided name is valid or not
     * name is valid only if it has alphabets(small/upper/mixOfBoth) and spaces
     * name is invalid if it has any special characters or digits any characters other than alphabets and spaces.
     */
    private static boolean isNameValid(String name) {

        return name != null && name.matches("[a-zA-Z ]+");
    }

    /*
     * This method is used to check if rooms avialable in the database or not
     * @return returns true if rooms exist,false if no rooms avialable.
     */
    private static boolean roomsExist(SessionFactory factory) {

        String query = "SELECT sum(bedsAvailable) FROM Room";
        Session session = factory.openSession();
        session.beginTransaction();

        Query q =  session.createQuery(query);
        Long sum = (Long)q.getSingleResult();
        session.getTransaction().commit();
        session.close();

        if(sum == null || sum == 0)return false;
        return true;
    }

    /*
     * This method is used to add all available room objects from 1 to 30 into
     * the database once the main method is executed.Because rooms are static so
     * we need to insert them into database first.
     */
    private static void addRooms(SessionFactory factory) {
        Session session = factory.openSession();
        session.beginTransaction();

        for(int i=1; i<31; i++){
            Room room = new Room();
            if (i<19) {
                room.setRoomNumber(i);
                room.setCapacity(4);
                room.setBedsAvailable(4);
                room.setFee(5000);
                session.save(room);
                    //session.flush();
            }else{
                room.setRoomNumber(i);
                room.setCapacity(2);
                room.setBedsAvailable(2);
                room.setFee(5500);
                session.save(room);
                    //session.flush();
                }
            
        }
        session.getTransaction().commit();
        session.close();
    }
}
