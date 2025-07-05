package com.hostel.management.utility;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hostel.management.model.Payment;
import com.hostel.management.model.Room;
import com.hostel.management.model.Student;


/*
 * This class is used to create both Configuration, SessionFactory
 * objects, also its mainly used to send the SessionFactory object 
 * through its static methods to any method or class which needs this object.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    /*
     * It creates the configuration object and sessionfactory object required for all
     * hibernate operations.It is created only onnce, used everywhere 
     */
    static{
        Configuration config = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Student.class).addAnnotatedClass(Room.class).addAnnotatedClass(Payment.class);
        sessionFactory = config.buildSessionFactory();
    }

    /*
     * returns the session factory object,can be used anywhere 
     */
    public static SessionFactory getSessionFactory(){
        if (sessionFactory == null) {
            throw new IllegalArgumentException("SessinFactory not initialized.");
        }
        return sessionFactory;
    }

    /*
     * closes the session factory object
     */
    public static void shutDown(){
        sessionFactory.close();
    }
}
