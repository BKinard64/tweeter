package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public interface AsyncMsgService {
    void sendMessage(String messageBody, String url);
    void deleteMessage(String url, SQSEvent.SQSMessage msg);
}
