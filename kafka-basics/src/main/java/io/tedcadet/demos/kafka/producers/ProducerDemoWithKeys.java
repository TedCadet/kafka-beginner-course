package io.tedcadet.demos.kafka.producers;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithKeys {

  private static final Logger log = LoggerFactory.getLogger(
      ProducerDemoWithKeys.class.getSimpleName());

  public static void main(String[] args) {
    log.info("Hello world!");
    // create Producer Properties
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());

    // create the producer
    KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

    for (int i = 0; i < 10; i++) {

      String topic = "demo_java";
      String value = "hello world " + i;
      String key = "id_" + i;

      // create a producer record
      ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

      // send data - asynchronous
      producer.send(producerRecord, (metadata, exception) -> {
        // executes everytime a record is successfully sent or an exception is thrown
        if (exception == null) {
          log.info("received new metadata/ \n" +
              "Topic: " + metadata.topic() + "\n" +
              "Key: " + producerRecord.key() + "\n" +
              "Value: " + producerRecord.value() + "\n" +
              "Partition: " + metadata.partition() + "\n" +
              "offset: " + metadata.offset() + "\n" +
              "timestamp: " + metadata.timestamp());
        } else {
          log.error("Error while producing", exception);
        }
      });
    }

    // flush data
    producer.flush();

    // close producer
    producer.close();
  }
}
