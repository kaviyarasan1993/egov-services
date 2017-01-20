package org.egov.pgr.web.persist.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.egov.pgr.web.persist.model.ServiceRequestReq;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PersistController {

	public void assignedRequestsReceiver() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("group.id", "assigned");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> savedRequests = new KafkaConsumer<>(props);
		savedRequests.subscribe(Arrays.asList("ap.public.mseva.assigned"));
		while (true) {
			ConsumerRecords<String, String> records = savedRequests.poll(5000);
			System.err.println("******** polling assignedRequestsReceiver at time " + new Date().toString());
			for (ConsumerRecord<String, String> record : records) {

				ObjectMapper mapper = new ObjectMapper();
				ServiceRequestReq request;
				try {
					request = mapper.readValue(record.value(), ServiceRequestReq.class);
					System.err.println(
							"---------------- Received form topic  ap.public.mseva.assigned -------------------------");
					System.err.println("---------------- Complaint persist --------------");
					pushPersistedRequests(request, "ap.public.mseva.saved");
					System.err.println(
							"---------------- Pushing to topic ap.public.mseva.saved -------------------------");

				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void pushPersistedRequests(ServiceRequestReq request, String topic) throws JsonProcessingException {
		Properties props = new Properties();
		props.put("bootstrap.servers", "kafka:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 1);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer producer = new KafkaProducer<>(props);
		ObjectMapper mapper = new ObjectMapper();

		producer.send(new ProducerRecord<String, String>(topic, request.getServiceRequest().getCrn(),
				mapper.writeValueAsString(request)));
		producer.close();
	}

}