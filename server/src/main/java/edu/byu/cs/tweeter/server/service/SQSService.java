package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SQSService implements AsyncMsgService {
    private final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    @Override
    public void sendMessage(String messageBody, String url) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(url)
                .withMessageBody(messageBody);

        sqs.sendMessage(send_msg_request);
    }

    @Override
    public void deleteMessage(String url, SQSEvent.SQSMessage msg) {
        sqs.deleteMessage(url, msg.getReceiptHandle());
    }
}
