package io.tedcadet.demos.kafka.consumers;

import java.time.Duration;
import java.util.Collections;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ConsumerDemo {

  private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());

  public static void main(String[] args) {
    log.info("Kafka consumer");
    String bootstrapServer = "127.0.0.1:9092";
    String groupId = "my-second-application";
    String topic = "demo_java";

    // consumer configs
    Properties properties = new Properties();
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // create consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    // subscribe consumer to our topic
    consumer.subscribe(Collections.singletonList(topic));
    // subscribe to many topics
    //consumer.subscribe(Arrays.asList(topic));

    // poll for new data

    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

      for (ConsumerRecord<String, String> record : records) {
        log.info("Partition: " + record.partition() + "\n" +
            "Key: " + record.key() + "\n" +
            "Value: " + record.value() + "\n" +
            "offset: " + record.offset() + "\n");
      }
    }

  }
}
