package com.hostel.management.shedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hostel.management.tasks.FeeStatusTask;

public class FeeStatusScheduler {
    
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void start(){
        
        FeeStatusTask task = new FeeStatusTask();
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.DAYS);

    }


    public void shutdown() {

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Forcing shutdown...");
                scheduler.shutdownNow();
            } else {
                System.out.println("Scheduler stopped cleanly.");
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
    }
}
