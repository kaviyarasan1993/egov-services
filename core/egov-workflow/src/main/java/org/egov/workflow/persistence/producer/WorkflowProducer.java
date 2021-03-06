package org.egov.workflow.persistence.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class WorkflowProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, Object message) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
        future.addCallback(
                new ListenableFutureCallback<SendResult<String, Object>>() {
                    @Override
                    public void onSuccess(SendResult<String, Object> stringTSendResult) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                }
        );
    }
}
