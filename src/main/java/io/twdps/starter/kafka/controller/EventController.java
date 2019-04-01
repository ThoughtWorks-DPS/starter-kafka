package io.twdps.starter.kafka.controller;


import io.twdps.starter.kafka.domain.CustomerEvent;
import io.twdps.starter.kafka.domain.EventKafkaMetadata;
import io.twdps.starter.kafka.service.EventProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

  private static final Logger logger =
      LoggerFactory.getLogger(EventController.class);

  @Autowired
  private EventProcessingService eventProcessingService;

  @PostMapping("/events/customer")
  public ResponseEntity<EventKafkaMetadata> createCustomerEventMessage(
      @RequestBody CustomerEvent customerEvent) {

    EventKafkaMetadata eventKafkaMetadata = eventProcessingService.createAndSendEvent(customerEvent);
    return new ResponseEntity<EventKafkaMetadata>(eventKafkaMetadata, HttpStatus.OK);
  }
}


