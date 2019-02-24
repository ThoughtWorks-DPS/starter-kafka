package io.twdps.starter.kafka;

import io.twdps.starter.kafka.domain.CustomerEvent;
import io.twdps.starter.kafka.domain.CustomerEventMessage;
import io.twdps.starter.kafka.domain.EventKafkaMetadata;
import io.twdps.starter.kafka.service.CustomerEventKafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1,
    topics = {"customer"})
public class KafkaProducerConsumerTest {

  private CustomerEvent customerEvent = new CustomerEvent();

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private CustomerEventKafkaConsumer eventConsumer;

  @BeforeEach
  private void setup() {

    customerEvent.setCreatedAt(ZonedDateTime.now().toEpochSecond());
    customerEvent.setCustomerId(1000);
    customerEvent.setType("CUSTOMER_ADDED");
  }

  @Test
  public void testReceive() throws Exception {

    HttpEntity<CustomerEvent> request = new HttpEntity<>(customerEvent);
    ResponseEntity<EventKafkaMetadata> response = this.restTemplate.postForEntity("http://localhost:" + port + "/events/customer", request,
        EventKafkaMetadata.class);
    eventConsumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    assertEquals(eventConsumer.getLatch().getCount(), 0);
    assertEquals(response.getBody().getCustomerId(),customerEvent.getCustomerId());
    assertTrue(response.getBody().getOffset() > -1);
  }
}
