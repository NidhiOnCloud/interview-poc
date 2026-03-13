package com.practice.interview.poc;

import java.util.concurrent.*;

public class DashboardAggregator {

    public static void main(String[] args) {

        int services = 3;

        ExecutorService executor = Executors.newFixedThreadPool(services);

        CyclicBarrier barrier = new CyclicBarrier(services, () ->
                System.out.println("All services responded. Aggregating dashboard response...")
        );

        executor.submit(() -> callService("Profile Service", barrier));
        executor.submit(() -> callService("Order Service", barrier));
        executor.submit(() -> callService("Recommendation Service", barrier));

        executor.shutdown();
    }

    private static void callService(String service, CyclicBarrier barrier) {
        try {
            System.out.println(service + " call started");

            Thread.sleep((long) (Math.random() * 3000)); // simulate latency

            System.out.println(service + " response received");

            barrier.await(); // wait for others

            System.out.println(service + " proceeding after aggregation stage");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}