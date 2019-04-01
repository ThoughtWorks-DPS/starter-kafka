package io.twdps.starter.kafka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaProducerConfigProperties {

  private String acks;
  long bufferMemory;
  String compressionType;
  int retries;
  int maxInflightRequests;
  int batchSize;
  String clientId;
  int deliveryTimeout;

  public KafkaProducerConfigProperties() {
  }

  public String getAcks() {
    return acks;
  }

  public void setAcks(String acks) {
    this.acks = acks;
  }

  public long getBufferMemory() {
    return bufferMemory;
  }

  public void setBufferMemory(long bufferMemory) {
    this.bufferMemory = bufferMemory;
  }

  public String getCompressionType() {
    return compressionType;
  }

  public void setCompressionType(String compressionType) {
    this.compressionType = compressionType;
  }

  public int getRetries() {
    return retries;
  }

  public void setRetries(int retries) {
    this.retries = retries;
  }

  public int getMaxInflightRequests() {
    return maxInflightRequests;
  }

  public void setMaxInflightRequests(int maxInflightRequests) {
    this.maxInflightRequests = maxInflightRequests;
  }

  public int getBatchSize() {
    return batchSize;
  }

  public void setBatchSize(int batchSize) {
    this.batchSize = batchSize;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public int getDeliveryTimeout() {
    return deliveryTimeout;
  }

  public void setDeliveryTimeout(int deliveryTimeout) {
    this.deliveryTimeout = deliveryTimeout;
  }
}
