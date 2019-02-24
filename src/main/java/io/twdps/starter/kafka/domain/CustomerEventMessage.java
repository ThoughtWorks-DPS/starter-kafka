package io.twdps.starter.kafka.domain;


public class CustomerEventMessage {

  private int eventId;
  private long createdAt;
  private long lastModified;
  private String type;
  private int customerId;

  public CustomerEventMessage() {
  }

  public CustomerEventMessage(int eventId, int customerId, long createdAt, long lastModified, String type) {
    this.eventId = eventId;
    this.createdAt = createdAt;
    this.lastModified = lastModified;
    this.type = type;
    this.customerId = customerId;
  }

  public int getEventId() {
    return eventId;
  }

  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public long getLastModified() {
    return lastModified;
  }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }
}
