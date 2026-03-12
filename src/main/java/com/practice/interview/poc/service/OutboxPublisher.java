package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.OutboxEvent;
import com.practice.interview.poc.entity.repo.OutboxRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxPublisher {

   /* private final OutboxRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {

        List<OutboxEvent> events =
                repository.findByPublishedFalse();

        for (OutboxEvent event : events) {
            kafkaTemplate.send("payment-topic", event.getPayload());
            event.setPublished(true);
            repository.save(event);
        }
    }*/
}