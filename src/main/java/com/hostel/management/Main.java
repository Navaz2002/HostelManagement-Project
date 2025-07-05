package com.hostel.management;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.Scanner;

import com.hostel.management.handler.PaymentHandler;
import com.hostel.management.handler.RoomHandler;
import com.hostel.management.handler.StudentHandler;
import com.hostel.management.shedulers.FeeStatusScheduler;
import com.hostel.management.utility.HibernateUtil;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.sun.net.httpserver.HttpServer;





public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        FeeStatusScheduler scheduler = new FeeStatusScheduler();

        try {

            scheduler.start();
            
            HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
            server.createContext("/student", new StudentHandler());
            server.createContext("/room", new RoomHandler());
            server.createContext("/payment", new PaymentHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Server started.");
            System.out.println("Enter to stop server.");
            scanner.nextLine();
            server.stop(0);

            System.out.println("Server stopped");

     

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            scheduler.shutdown();

            // Closing Hibernate
            HibernateUtil.shutDown();
            System.out.println("Factory closed.");

            // Cleaning up abandoned JDBC connections
            AbandonedConnectionCleanupThread.checkedShutdown();

            // Closing scanner
            scanner.close();
        }
    }

}
