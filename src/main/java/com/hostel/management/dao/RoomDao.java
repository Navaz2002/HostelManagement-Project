package com.hostel.management.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;


import com.hostel.management.model.Room;
import com.hostel.management.model.Student;
import com.hostel.management.utility.HibernateUtil;
/*
 * This class is used to only interact with database, only with
 * Room entity.
 * 
 * This provides following methods currently.
 * 
 * public Room fetchRoomByNumber(int roomNumber)
 * public doesRoomExist(int roomNumber)
 * public List<Room> fetchAllRooms()
 * public List<Room> fetchRoomsByAvailability()
 * public List<Room> fetchUnAvailableRooms()
 * public List<Room> fetchAvailableRoomsOfCapacity(int capacity)
 * public Room addRoom(Room room)
 * public Room updateFeeOfRoom(int fee, int roomNumber)
 * public RoomupdateCapacity(int capacity, int bedsAvailable, int roomNumber)
 * public boolean doesRoomHaveStudents(int roomNumber)
 * public boolean deleteRoom(int roomNumber)
 * 
 */
public class RoomDao {

    /*
     * This method returns the room object for provided
     * room number
     */
    public Room fetchRoomByNumber(int roomNumber) {
        Session session = null;
        
        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Room room = session.get(Room.class, roomNumber);

            session.getTransaction().commit();
            session.close();

            return room;

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
     * This method checks if provided room number with room
     * exists in the database or not
     * It returns true if room exists for provided room number, false
     * if not
     */
    public boolean doesRoomExist(int roomNumber) {
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Room room = session.get(Room.class, roomNumber);

            session.getTransaction().commit();
            session.close();

            return room != null;

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
     * This method returns the list of all rooms
     */
    public List<Room> fetchAllRooms() {
        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Room";
            Query q = session.createQuery(hql);
            List<Room> roomsList = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return roomsList;

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
     * This method returns the list of available rooms
     */
    public List<Room> fetchRoomsByAvailability() {
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Room WHERE bedsAvailable <> 0";
            Query q = session.createQuery(hql);
            List<Room> avilableRooms = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return avilableRooms;

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
     * This method returns the list of unavailable rooms
     */
    public List<Room> fetchUnAvailableRooms() {

        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Room WHERE bedsAvailable = 0";
            Query q = session.createQuery(hql);
            List<Room> unAvailableRooms = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return unAvailableRooms;

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
     * This method returns the list of available rooms for
     * provided capacity
     */
    public List<Room> fetchAvailableRoomsOfCapacity(int capacity) {
        
        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Room WHERE bedsAvailable <> 0 AND capacity = :capacity";
            Query q = session.createQuery(hql);
            q.setParameter("capacity", capacity);
            List<Room> availableRooms = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return availableRooms;

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
     * This method is used to save the provided room object
     * to the database.
     */
    public Room addRoom(Room room) {
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.persist(room);

            session.getTransaction().commit();
            session.close();

            return room;

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
     * This method is used to update the fee of
     * provided room number with provided new fee
     */
    public Room updateFeeOfRoom(int fee, int roomNumber) {
        
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Room room = session.get(Room.class, roomNumber);
            room.setFee(fee);
            
            session.getTransaction().commit();
            session.close();

            return room;

        } catch (Exception e) {

            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;

        }finally {
            if (session != null) session.close();
        }
    }

    /*
     * This method us used to update the capacity of provided
     * room number with provided new capacity
     */
    public Room updateCapacity(int capacity, int bedsAvailable, int roomNumber) {
        
        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Room room = session.get(Room.class, roomNumber);
            room.setCapacity(capacity);
            room.setBedsAvailable(bedsAvailable);

            session.getTransaction().commit();
            session.close();

            return room;

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
     * This method is used to check whether provided
     * room number has students in it or not.
     * It returns true if room has students, false if not
     */
    public boolean doesRoomHaveStudents(int roomNumber) {

        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Room room = session.get(Room.class, roomNumber);
            Hibernate.initialize(room.getStudentList());
            List<Student> list = room.getStudentList();
            
            session.getTransaction().commit();
            session.close();

            if (list.isEmpty()) {
                return false ;
            }

            return true;

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
     * this method deletes the room assiciated with the provided
     * room number from database.Throws exception if anything goes
     * unexpected
     */
    public boolean deleteRoom(int roomNumber) {

        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "DELETE FROM Room WHERE roomNumber = :roomNumber";
            Query q = session.createQuery(hql);
            q.setParameter("roomNumber", roomNumber);
            
            int rowsAffected = q.executeUpdate();

            session.getTransaction().commit();
            session.close();

            return rowsAffected > 0;
        } catch (Exception e) {

            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;

        } finally {

            if (session != null) session.close();
        }


    }
    
}
