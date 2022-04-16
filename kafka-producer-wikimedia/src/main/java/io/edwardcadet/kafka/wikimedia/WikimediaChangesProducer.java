package io.edwardcadet.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.EventSource.Builder;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

public class WikimediaChangesProducer {

  public static void main(String[] args) throws InterruptedException {

    String bootstrapServer = "127.0.0.1:9092";
    String topic = "wikimedia.recentchange";
    String url = "https://stream.wikimedia.org/v2/stream/recentchange";

    // create Producer Properties
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());

    // create the producer
    KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

    // create a eventHandler to handle the events from the wikimedia stream
    EventHandler eventHandler = new WikimediaChangeHandler(producer, topic);
    EventSource.Builder builder = new Builder(eventHandler, URI.create(url));
    EventSource eventSource = builder.build();

    // start the producer in another thread
    eventSource.start();

    // we produce for 10 minutes and block the program until then
    TimeUnit.MINUTES.sleep(10);
  }

}
