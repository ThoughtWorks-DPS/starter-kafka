package io.twdps.starter.kafka.service;

import io.twdps.starter.kafka.domain.CustomerEvent;
import io.twdps.starter.kafka.domain.CustomerEventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EventSimulatorService {

  private AtomicInteger simulatedEventId;
  private Random random;

  @Autowired
  private EventProducer eventProducer;

  public EventSimulatorService() {
    this.simulatedEventId = new AtomicInteger();
    this.simulatedEventId.set(0);
    random = new Random();
  }

  public CustomerEventMessage createSimulatedEvent(CustomerEvent customerEvent) {

    int eventId = simulatedEventId.incrementAndGet();
    long modified = ZonedDateTime.now().toEpochSecond();
    return new CustomerEventMessage(eventId, customerEvent.getCustomerId(), customerEvent.getCreatedAt(), modified, customerEvent.getType());
  }

}
