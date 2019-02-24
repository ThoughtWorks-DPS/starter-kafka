package io.twdps.starter.kafka.controller;


import io.twdps.starter.kafka.domain.CustomerEvent;
import io.twdps.starter.kafka.domain.CustomerEventMessage;
import io.twdps.starter.kafka.service.EventProducer;
import io.twdps.starter.kafka.service.EventSimulatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class EventController {

  private static final Logger logger =
      LoggerFactory.getLogger(EventController.class);

  @Autowired
  private EventSimulatorService eventSimulatorService;

  @Autowired
  private EventProducer eventProducer;

  @PostMapping("/events/customer")
  public ResponseEntity<CustomerEventMessage> createCustomerEventMessage(
      @RequestBody CustomerEvent customerEvent) {

    CustomerEventMessage customerEventMessage = eventSimulatorService.createSimulatedEvent(customerEvent);
    ListenableFuture<SendResult<Integer, CustomerEventMessage>> future
        = eventProducer.sendMessage(customerEventMessage);

    try {
      SendResult<Integer, CustomerEventMessage> sendResult = future.get();
    } catch (InterruptedException e) {

    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    /*
    future.addCallback(new ListenableFutureCallback<>() {

      @Override
      public void onSuccess(SendResult<Integer, CustomerEventMessage> result) {
        logger.info("Success sending message");
      }

      @Override
      public void onFailure(Throwable ex) {
        logger.error("Failure to send message");
      }

    });
     */
    //handle errors?
    return new ResponseEntity<CustomerEventMessage>(customerEventMessage, HttpStatus.OK);
  }
}


