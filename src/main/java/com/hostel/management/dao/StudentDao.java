package com.hostel.management.dao;

import java.time.LocalDate;

import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hostel.management.dto.request.StudentCreateRequest;
import com.hostel.management.dto.request.StudentUpdateRequest;
import com.hostel.management.dto.response.StudentGetResponse;
import com.hostel.management.model.Payment;
//import com.hostel.management.dto.response.StudentUpdateResponse;
import com.hostel.management.model.Room;
import com.hostel.management.model.Student;
//import com.hostel.management.utility.Helper;
import com.hostel.management.utility.HibernateUtil;

import jakarta.persistence.Query;
/*
 * This class is used to only intercat with the database,only
 * with Student entity.
 * 
 * This provides following methods currently
 * 
 * public boolean doesIdExist(int studentId)
 * public boolean roomExist(int roomNumber)
 * public Student saveStudent(StudentCreateRequest createRequest)
 * public StudentGetResponse fetchStudentById(int studentId)
 * public List<Student> fetchAllStudents()
 * public List<Student> fetchStudentsByCollege(String collegeName)
 * public List<Student> fetchStudentsByFeeStatus(String feeStatus)
 * public List<Student> fetchStudentsByRoomNumber(int roomNumber)
 * public List<Student> fecthStudentsByJoiningDate(LocalDate joiningDate)
 * public boolean deleteStudent(int studentId)
 * public int updateStudentContactById(int studentId, StudentUpdateRequest contactRequest)
 * public Student fetchStudent(int studentId)
 * public Student updateStudentRoomById(int studentId, StudentUpdateRequest roomNumUpdate)
 * public int fetchRoomNumByStudentId(int studentId)
 * public Student updateStudentFeeStatusById(double studentId, StudentUpdateRequest feeStatusRequest)
 * 
 */
public class StudentDao {

    /*
     * This method checks whether provided student id
     * exists in the database or not, if exists it returns
     * true if not returns false
     */
    public boolean doesIdExist(int studentId){

        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Student student = session.get(Student.class, studentId);

            session.getTransaction().commit();
            session.close();
            System.out.println("returning");
            return student != null;

        } catch (Exception e) {

            if(session.getTransaction() != null)session.getTransaction().rollback();
            throw e;

        }finally{

            if (session != null) {
                session.close();
            }

        }
    }
    
    /*
     * this method checks whether provided room has beds available 
     * or not, it returns true if room has available beds ,false
     * if room has no beds available.
     */
    public boolean roomExist(int roomNumber) {

        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Room room = session.get(Room.class, roomNumber);
            int availableBeds = room.getBedsAvailable();
            
            session.getTransaction().commit();
            session.close();

            return availableBeds != 0;

        } catch (Exception e) {
            if(session.getTransaction() != null) session.getTransaction().rollback();
            throw e;

        } finally {

            if(session != null) session.close();

        }

    }

    /*
     * This method creates a student object with provided details with CreateStudentRequest
     * object, then saves to the database
     */
    public Student saveStudent(StudentCreateRequest createRequest) throws IllegalStateException {

        Session session = null;

        int months = createRequest.getPaidForMonths();

        try {

            Student student = new Student();
            student.setStudentId(createRequest.getStudentId());
            student.setName(createRequest.getName());
            student.setCollegeName(createRequest.getCollegeName());
            student.setContactNumber(createRequest.getContactNumber());
            student.setFeeStatus("Paid");
            student.setJoiningDate(LocalDate.now());

            Payment payment = new Payment(student, months);

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Room room = session.get(Room.class, createRequest.getRoomNumber());
            student.setRoom(room);

            //adding student payment object to the student payment list
            student.addPayment(payment);

            session.persist(student);

            // reducing room available beds as student is alloted to this room.
            room.setBedsAvailable(room.getBedsAvailable() - 1);

            session.getTransaction().commit();
            session.close();

            return student;

        } catch (Exception e){

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {

                session.getTransaction().rollback();

            }

            throw e;

        } finally {

            if (session != null) session.close();

        }
    }


    /*
     * This method returns the student object of provided
     * student id
     */
    public StudentGetResponse fetchStudentById(int studentId) {

        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Student student = session.get(Student.class, studentId);

            session.getTransaction().commit();
            session.close();

            Room room = student.getRoom();
            int roomNumber = room.getRoomNumber();

            StudentGetResponse studentResponse = new StudentGetResponse(student, roomNumber);
            return studentResponse;
            
        } catch (Exception e) {

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {

                session.getTransaction().rollback();

            }

            throw e;

        } finally {

            if (session != null) session.close();
        }
    }

    /*
     * This method returns the list of students
     */
    public List<Student> fetchAllStudents() {
        Session session = null;

        try {
            
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Student";
            Query q = session.createQuery(hql);
            List<Student> studentsList = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return studentsList;

        } catch (Exception e) {

            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {

            if (session != null) session.close(); 
        }
    }

    /*
     * This method returns the list of students belonging to
     * the provided college name
     */
    public List<Student> fetchStudentsByCollege(String collegeName){
        Session session = null;

        try {
            
            SessionFactory sessionFactroy = HibernateUtil.getSessionFactory();
            session = sessionFactroy.openSession();
            session.beginTransaction();

            String hql = "FROM Student WHERE collegeName = :collegeName";
            Query q = session.createQuery(hql);
            q.setParameter("collegeName", collegeName);
            List<Student> students = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return students;

        } catch (Exception e) {

            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {

            if (session != null) session.close(); 

        }
    }

    /*
     * Thuis method returns the list of students based on
     * the  feeStatus provided to it (feeStatu may be(Paid, Due))
     * 
     * If the feeStatus is Paid then it returns the list of students whose 
     * feeStatus is Paid
     * 
     * If the feeStatus provided to it is Due then it returns the list of
     * students whose feeStatus is Due
     */
    public List<Student> fetchStudentsByFeeStatus(String feeStatus) {
        
        Session session = null;

        try {
            System.out.println("In dao");
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Student WHERE feeStatus = :feeStatus";
            Query q = session.createQuery(hql);
            q.setParameter("feeStatus", feeStatus);
            List<Student> students = q.getResultList();

            session.getTransaction().commit();
            session.close();
            System.out.println("Retuirning students by feestatus");
            return students;

        } catch (Exception e) {

            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {
            if (session != null) session.close(); 
        }
    }

    /*
     * This method returns the list of students who belong to the
     * provided room number.
     */
    public List<Student> fetchStudentsByRoomNumber(double roomNumber) {

        Session session = null;
        roomNumber = (int) roomNumber;
        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql ="FROM Student WHERE room.roomNumber = :roomNumber";
            Query q = session.createQuery(hql);
            q.setParameter("roomNumber", roomNumber);
            List<Student> students = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return students;

        } catch (Exception e) {

            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {
            if (session != null) session.close(); 
        }
    }

    /*
     * This method returns the list of students who joined on specific
     * date provided to it.
     */
    public List<Student> fetchStudentsByJoiningdate(LocalDate joiningDate) {
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Student WHERE joiningDate = :date";
            Query q = session.createQuery(hql);
            q.setParameter("date", joiningDate);
            List<Student> students = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return students;

        } catch (Exception e) {

            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {
            if (session != null) session.close();
        }
    }

    /*
     * This method deletes the student record associated with the id
     * provided to it
     */
    public boolean deleteStudent(int studentId) {
        
        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Room room = student.getRoom();
            /*
             * removing this student obj from his room object's studentList
             * as student will no longer exist in that room
             */
            for (Student s : room.getStudentList()) {
                if (s.getStudentId() == studentId) {

                    room.getStudentList().remove(s);
                    break;

                }
            }
            //increasing this student room beds by 1 as student leaves the room
            room.setBedsAvailable(room.getBedsAvailable() + 1);
            //actually deleting student from db
            session.remove(student);

            student = session.get(Student.class, studentId);

            session.getTransaction().commit();
            session.close();


            return student == null;

        } catch (Exception e) {

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {

            if (session != null) session.close();

        }
    }

    /*
     * This method checks whether provided contact number exists
     * in the database or not,it returns true if it exists, false
     * it does not exist.
     */
    public boolean doesContactExist(String contactNumber) {
        

        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "SELECT 1 FROM Student WHERE contactNumber = :contact";
            Query q = session.createQuery(hql);
            q.setParameter("contact", contactNumber);
            List<Integer> list = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return ! list.isEmpty() ;


        } catch (Exception e) {

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {
            if (session != null) session.close();
        }
    }

    /*
     * This method updates the contact number of provided student id ,
     * with provided new contact number
     */
    public Student updateStudentContactById(int studentId, StudentUpdateRequest contactRequest) {
        
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "UPDATE Student SET contactNumber = :contact WHERE studentId = :id";
            Query q = session.createQuery(hql);
            q.setParameter("contact", contactRequest.getContactNumber());
            q.setParameter("id", studentId);
            int affectedRows = q.executeUpdate();

            Student updated = session.get(Student.class, studentId);

            session.getTransaction().commit();
            session.close();

            return updated;

        } catch (Exception e) {

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {
            if (session != null) session.close();
        }
    }

    /*
     * This method returns the student object of provided
     * student id.
     */
    public Student fetchStudent(int studentId) {
        
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Student student = session.get(Student.class, studentId);

            session.getTransaction().commit();
            session.close();

            return student;

        } catch (Exception e) {

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {
            if (session != null) session.close();
        }
    }

    /*
     * this method is used to update the room of provided
     * student id.It first increases the available beds of student's old
     * room as the student leaves the room one bed becomes available, 
     * then assigns student to the another  provided room, then decreases the available 
     * beds of this new room by 1 as student is assigned to it.
     * Also saves the new room object to student object and vice versa.
     * 
     * @returnType returns 1 indicating the successful update, 
     */
    public Student updateStudentRoomNumById(int studentId, StudentUpdateRequest roomNumUpdate) {

        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            /*
             * Increasing the available beds of student's old room as student 
             * changes to another room
             */
            Student student = session.get(Student.class, studentId);
            Room room = student.getRoom();
            room.setBedsAvailable(room.getBedsAvailable() + 1);

            /*
             * assigning the student new room
             */
            Room newRoom = session.get(Room.class, roomNumUpdate.getRoomNumber());
            student.setRoom(newRoom);
            newRoom.setBedsAvailable(newRoom.getBedsAvailable() - 1);

            session.getTransaction().commit();
            session.close();

            return student;

        } catch (Exception e) {

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {

            if (session != null) session.close();

        }
    }

    /*
     * this method returns the room number of provided student
     * id
     */
    public int fetchRoomNumByStudentId(int studentId) {
        
        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            
            session.getTransaction().commit();
            session.close();

            return student.getRoomNumber();

        } catch (Exception e) {
            
            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {

            if (session != null) session.close();

        }
    }
    /*
     * this method updates the student feeStatus
     * provided to it, its used only to set feeStatus as
     * Paid if student pays fee.
     * 
     * 
     */
    public Student updateFeeStatusById(double studentId, StudentUpdateRequest feeStatusRequest) {
        
        int months = feeStatusRequest.getPaidforMonths();

        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            student.setFeeStatus("Paid");

            Payment payment = new Payment(student, months);

            //adding new payment to this student payment list 
            student.addPayment(payment);

            //Query to get this student's latest payment record
            String hql = "FROM Payment p WHERE p.student.studentId = :id ORDER BY p.payment_Date DESC";
            Query q = session.createQuery(hql);
            q.setParameter("id", student.getStudentId());
            q.setMaxResults(1);
            Payment latesPayment = (Payment)q.getSingleResult();
            /*
             * now updating this student's paid till for months provided as
             * this student paid fee
             */
            payment.setPaid_till(latesPayment.getPaid_till().plusMonths(months));

            session.persist(payment);

            session.getTransaction().commit();
            session.close();

            return student;

        } catch (Exception e) {

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {

            if (session != null) session.close();

        }
    }
    
}
