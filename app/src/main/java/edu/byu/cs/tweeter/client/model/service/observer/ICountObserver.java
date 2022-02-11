package edu.byu.cs.tweeter.client.model.service.observer;

public interface ICountObserver extends ServiceObserver {
    void handleSuccess(int count);
}
