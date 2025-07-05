package com.hostel.management.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hostel.management.model.Payment;
import com.hostel.management.utility.HibernateUtil;

import jakarta.persistence.Query;

/*
 * This class is used to only intercat with the dtabase, only
 * Payment entity.
 * 
 * This provides following methods currently
 * 
 * public boolean doesIdExist(int studentId)
 * public List<Payment> fetchStudentPaymentRecordsById(int studentId)
 * public List<Payment> fetchAllStudentPaymentRecords()
 * 
 */
public class PaymentDao {
    /*
     * This method checks whether provided student id
     * exists in the payment table  or not, if exists it returns
     * true , if not returns false
     */
    public boolean doesIdExist(int studentId) {

        Session session = null;

        try {

            SessionFactory sessionFactory =  HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            Payment studentPayment = session.get(Payment.class, studentId);

            session.getTransaction().commit();
            session.close();

            return studentPayment != null;

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
     * this method retrieves all the payment records of provided
     * student id from the Payment entity
     */
    public List<Payment> fetchStudentPaymentRecordsById(int studentId) {
        
        Session session = null;

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            /*
            *  fetching payment records of student from payment
            *  table
            */

            String hql = "FROM Payment p WHERE p.student.studentId = :id";
            Query q = session.createQuery(hql);
            q.setParameter("id", studentId);

            List<Payment> paymentRecords = q.getResultList();

            session.getTransaction().commit();
            session.close();
           
            return paymentRecords;


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
     * this method retrieves all the students payment records from
     * the payment table.
     * 
     * returns the list of those payment objects
     */
    public List<Payment> fetchAllStudentPaymentRecords() {
        

        Session session = null;

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String hql = "FROM Payment";
            Query q = session.createQuery(hql);
            List<Payment> studentsPayment = q.getResultList();

            session.getTransaction().commit();
            session.close();

            return studentsPayment;


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
