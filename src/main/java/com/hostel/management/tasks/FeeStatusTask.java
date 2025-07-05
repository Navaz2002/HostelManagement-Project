package com.hostel.management.tasks;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hostel.management.model.Payment;
import com.hostel.management.model.Student;
import com.hostel.management.utility.HibernateUtil;

import jakarta.persistence.Query;

public class FeeStatusTask implements Runnable{
    /*
     * This task retrieves the lates 1 payment record of each
     * student and compares the payment's paid_Till with today's date to see if month is
     * completed or not, if month completes then it gets the student obj belongs to that
     * payment and sets the student feeStatus as Due.
     *   
     * This task runs every day to check every student's paid_till dates
     */
    @Override
    public void run() {

        Session session = null;
        LocalDate todayDate = LocalDate.now();

        try {

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            /*
             * retrieving payment list which contains the 
             * latest payment record of each student
             */
            String hql = "FROM Payment p " +
                        "WHERE p.payment_Date = (" +
                        "SELECT MAX(p2.payment_Date) " +
                        "FROM Payment p2 " +
                        "WHERE p2.student.studentId = p.student.studentId" +
                        ")";
            
            Query q = session.createQuery(hql);
            List<Payment> studentPayments = q.getResultList();

            for (Payment p : studentPayments) {
                /*
                 * comparing this payment paid_till with today's date to see 
                 * if month is completed or not , if month completes then get
                 * that payment student and update feeStatus as Due.
                 */
                if (todayDate.isAfter(p.getPaid_till())) {
                    Student s = session.get(Student.class, p.getStudent().getStudentId());
                    s.setFeeStatus("Due");

                }
            }


            session.getTransaction().commit();
            session.close();


        } catch (Exception e) {

            if (session.getTransaction() != null && session.getTransaction().getStatus().canRollback()) {
                session.getTransaction().rollback();
            }

            e.printStackTrace();

        } finally {

            if (session != null) session.close();
            
        }
    }
    
}
