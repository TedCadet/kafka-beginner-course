package io.edwardcadet.kafka.wikimedia;


import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class WikimediaChangeHandler implements EventHandler {

  private final Logger log = LoggerFactory.getLogger(WikimediaChangeHandler.class.getSimpleName());
  private KafkaProducer<String, String> kafkaProducer;
  String topic;

  @Override
  public void onOpen() {
    // nothing here
  }

  @Override
  public void onClosed() {
    kafkaProducer.close();
  }

  @Override
  public void onMessage(String event, MessageEvent messageEvent) throws Exception {
    log.info("messageEvent: " + messageEvent.getData());
    // asynchronous
    kafkaProducer.send(new ProducerRecord<>(topic, messageEvent.getData()));
  }

  @Override
  public void onComment(String comment) throws Exception {

  }

  @Override
  public void onError(Throwable t) {
    log.error("Error in Stream Reading", t);
  }
}
