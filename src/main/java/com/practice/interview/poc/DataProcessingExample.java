package com.practice.interview.poc;

import java.util.concurrent.*;

public class DataProcessingExample {

    public static void main(String[] args) {
        int workers = 3;
        CyclicBarrier barrier = new CyclicBarrier(workers, () ->
                System.out.println("All workers finished processing. Merging results...")
        );

        for (int i = 0; i < workers; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    System.out.println("Worker " + id + " processing data...");
                    Thread.sleep(2000);

                    System.out.println("Worker " + id + " waiting at barrier");
                    barrier.await();

                    System.out.println("Worker " + id + " continuing next stage");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}