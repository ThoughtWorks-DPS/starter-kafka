package io.twdps.starter.kafka.configuration;

import io.twdps.starter.kafka.domain.CustomerEventMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@EnableKafka
@Configuration
public class KafkaConfiguration {

  public static Logger logger = LoggerFactory.getLogger(KafkaConfiguration.class);

  @Autowired
  private KafkaProducerConfigProperties producerConfigs;

  @Value("${kafka.brokers}")
  private String kafkaBrokerURL;

  @Value("${kafka.topic}")
  private String starterTopic;

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    // list of host:port pairs used for establishing the initial connections to the Kafka cluster
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        kafkaBrokerURL);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "starter-group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        IntegerDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
    );
    //key learning in newest Spring Kafka dependencies - must add the custom package to the trusted packages
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "io.twdps.starter.kafka.domain");
    // allows a pool of processes to divide the work of consuming and processing records

    // automatically reset the offset to the latest offset - should be set externally
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    return props;
  }

  @Bean
  public Map<String, Object> producerConfigs() {
    logger.info("THIS IS THE KAFKA BROKERS URL->{}", kafkaBrokerURL);
    logger.info("THIS IS THE KAFKA TOPIC->{}", starterTopic);
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokerURL);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    props.put(ProducerConfig.ACKS_CONFIG,producerConfigs.getAcks());
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,producerConfigs.getBufferMemory());
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,producerConfigs.getCompressionType());
    props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,producerConfigs.getMaxInflightRequests());
    props.put(ProducerConfig.RETRIES_CONFIG,producerConfigs.getRetries()); //how many times will we retry on failure
    props.put(ProducerConfig.CLIENT_ID_CONFIG, producerConfigs.getClientId());
    // See https://kafka.apache.org/documentation/#producerconfigs for more properties
    return props;
  }

  @Bean
  public ProducerFactory<Integer, CustomerEventMessage> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public ConsumerFactory<Integer, CustomerEventMessage> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, CustomerEventMessage>> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<Integer, CustomerEventMessage> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @Bean
  public KafkaTemplate<Integer, CustomerEventMessage> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean(name = "kafkaProducerExecutor")
  public Executor kafkaProducerExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(3);
    executor.setMaxPoolSize(3);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("kafkaProducerExecutor-");
    executor.initialize();
    return executor;
  }
}
