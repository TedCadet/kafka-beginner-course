package io.tedcadet.demos.kafka.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemoCooperative {

  private static final Logger log = LoggerFactory.getLogger(
      ConsumerDemoCooperative.class.getSimpleName());

  public static void main(String[] args) {
    log.info("Kafka consumer");
    String bootstrapServer = "127.0.0.1:9092";
    String groupId = "my-third-application";
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
    properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
        CooperativeStickyAssignor.class.getName());
//    properties.setProperty(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "");

    // create consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    // get a reference to the current thread
    final Thread mainThread = Thread.currentThread();

    // adding the shutdown hook
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
        consumer.wakeup();

        // join main thread to allow the execution of the code in the main Thread
        try {
          mainThread.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    try {
      // subscribe consumer to our topic(s)
      consumer.subscribe(Arrays.asList(topic));

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
    } catch (WakeupException e) {
      log.info("wake up exception, normal");
      // we ignore this is an expected exception when closing a consumer
    } catch (Exception e) {
      log.error("Unexpected exception: " + e);
    } finally {
      log.info("closing consumer");
      consumer.close();
    }
  }
}
