package edu.byu.cs.tweeter.server.service;

public interface AsyncMsgService {
    void sendMessage(String messageBody, String url);
}
