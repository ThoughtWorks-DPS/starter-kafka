package io.twdps.starter.kafka.domain;

import org.apache.kafka.clients.producer.RecordMetadata;

public class EventKafkaMetadata {

  private int customerId;
  private int eventId;
  private int partition;
  private long offset;
  private String topic;

  public EventKafkaMetadata() {
  }

  public EventKafkaMetadata(int customerId, int eventId, RecordMetadata recordMetadata) {
    this.customerId = customerId;
    this.eventId = eventId;
    if (recordMetadata.hasOffset()) {
      offset = recordMetadata.offset();
    } else {
      offset = -1l;
    }
    partition = recordMetadata.partition();
    topic = recordMetadata.topic();
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getEventId() {
    return eventId;
  }

  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  public int getPartition() {
    return partition;
  }

  public void setPartition(int partition) {
    this.partition = partition;
  }

  public long getOffset() {
    return offset;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
}
