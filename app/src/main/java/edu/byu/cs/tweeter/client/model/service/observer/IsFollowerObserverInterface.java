package edu.byu.cs.tweeter.client.model.service.observer;

public interface IsFollowerObserverInterface extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}
