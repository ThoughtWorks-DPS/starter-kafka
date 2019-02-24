package io.twdps.starter.kafka.service;

import io.twdps.starter.kafka.domain.CustomerEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class CustomerEventKafkaConsumer {

  public static Logger logger = LoggerFactory.getLogger(CustomerEventKafkaConsumer.class);

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  @KafkaListener(
      topics = "${kafka.topic}",
      containerFactory = "kafkaListenerContainerFactory")
  public void eventListener(@Payload CustomerEventMessage event,
                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
    // process event message
    logger.info("Received Message with event type:{}, customerID:{} on partition:{}", event.getType(), event.getCustomerId(), partition);
    latch.countDown(); // here to make it easy to test
  }

}
