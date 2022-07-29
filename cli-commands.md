# Start Kafka
zookeeper-server-start.sh path-to-the-config-files-of-kafka/zookeeper.properties
kafka-server-start.sh path-to-the-config-files-of-kafka/server.properties

# Topics

## list topics
kafka-topics.sh --bootstrap-server localhost:9092 --list

---
## create a topic
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic_name

## alter a topic
kafka-topics.sh --bootstrap-server localhost:9092 --alter --topic demo_java --partitions 3

## create a topic with partitions
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic_name --partitions 3

## create a topic with partitions and replication_factors
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic_name --partitions 3 --replication-factor 2

---
## Describe a topic
kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic topic_name

## Delete a topic
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic topic_name

# Producers

## open a producer console
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic topic_name

## produce with properties
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic topic_name --producer-property acks=all

## produce with keys
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic topic_name --property parse.key=true  --property key.separator=:

# Consumers

## open a consumer console
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic_name

## consume from the beginning of the topic
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic_name --from-beginning

## consume and display key, values and timestamp
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic_name --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true

# Consumers in group

1. Start one consumer
  1. kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --group my-first-application
2. Start one producer and start producing
3. Start another consumer part of the same group. 
  3. kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --group my-first-application
4. Start another consumer part of a different group from beginning
  4. kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic first_topic --group my-second-application --from-beginning

## list groups
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list

## describe one specific group
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group group-name

# Offsets

## reset the offsets to the beginning of each partition
* execute flag is needed
* topic flag is needed
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --execute --topic first-topic

## shift offsets (forward)
* shift-by flag with positive integer
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --shift-by 2 --execute --topic first_topic

## shift offsets (backward)
* shift-by flag with negative integer
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --shift-by 2 --execute --topic first_topic




