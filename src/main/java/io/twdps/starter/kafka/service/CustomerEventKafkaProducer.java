package io.twdps.starter.kafka.service;

import io.twdps.starter.kafka.domain.CustomerEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EventProducer {

  public static Logger logger = LoggerFactory.getLogger(EventProducer.class);

  @Value("${kafka.topic}")
  private String starterTopic;

  @Autowired
  private KafkaTemplate template;

  private AtomicInteger simulatedEventId;

  @Async("kafkaProducerExecutor")
  public ListenableFuture<SendResult<Integer, CustomerEventMessage>> sendMessage(final CustomerEventMessage message) {
    ListenableFuture<SendResult<Integer, CustomerEventMessage>> future = this.template.send(starterTopic, message.getEventId(), message);
    return future;
  }
}
