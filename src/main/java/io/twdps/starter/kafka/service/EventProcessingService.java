package io.twdps.starter.kafka.service;

import io.twdps.starter.errors.exceptions.DownstreamTimeoutException;
import io.twdps.starter.kafka.controller.EventController;
import io.twdps.starter.kafka.domain.CustomerEvent;
import io.twdps.starter.kafka.domain.CustomerEventMessage;
import io.twdps.starter.kafka.domain.EventKafkaMetadata;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EventSimulatorService {
  private static final Logger logger =
      LoggerFactory.getLogger(EventController.class);
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

  public EventKafkaMetadata createAndSendEvent(CustomerEvent customerEvent) {

    CustomerEventMessage customerEventMessage = createSimulatedEvent(customerEvent);
    ListenableFuture<SendResult<Integer, CustomerEventMessage>> future
        = eventProducer.sendMessage(customerEventMessage);

    try {
      SendResult<Integer, CustomerEventMessage> sendResult = future.get();
      CustomerEventMessage cem = sendResult.getProducerRecord().value();
      RecordMetadata recordMetadata = sendResult.getRecordMetadata();
      EventKafkaMetadata eventKafkaMetadata = new EventKafkaMetadata(cem.getEventId(),recordMetadata);
      logger.info("successfully wrote eventId:{}, to partition:{} with offset:{}"
          ,eventKafkaMetadata.getEventId(),eventKafkaMetadata.getPartition(),eventKafkaMetadata.getOffset());
      return eventKafkaMetadata;
    } catch (InterruptedException e) {
      throw new DownstreamTimeoutException("500",
          String.format("Interrupted Write to kafka. Cause: %s",e.getLocalizedMessage()));
    } catch (ExecutionException e) {
      throw new DownstreamTimeoutException("500",
          String.format("Exception writing to kafka. Cause: %s",e.getLocalizedMessage()));
    }
  }

}
