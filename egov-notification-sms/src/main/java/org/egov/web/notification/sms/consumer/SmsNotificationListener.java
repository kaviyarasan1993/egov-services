package org.egov.web.notification.sms.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.web.notification.sms.model.SMSRequest;
import org.egov.web.notification.sms.services.MessagePriority;
import org.egov.web.notification.sms.services.sms.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;

public class SmsNotificationListener {

	@Autowired
	@Qualifier("smsService")
	private SMSService smsService;

	@KafkaListener(id = "${kafka.topics.notification.sms.id}", topics = "${kafka.topics.notification.sms.name}", group = "${kafka.topics.notification.sms.group}")
	public void listen(ConsumerRecord<String, SMSRequest> record) {
		System.err.println("***** received message [key" + record.key() + "] + value [" + record.value()
				+ "] from topic egov-notification-sms");
		SMSRequest request = record.value();
		smsService.sendSMS(request.getMobileNumber(), request.getMessage(), MessagePriority.HIGH);
	}

}