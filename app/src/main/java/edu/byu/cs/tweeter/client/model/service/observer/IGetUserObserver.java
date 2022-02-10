package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.User;

public interface IGetUserObserver extends ServiceObserver {
    void handleSuccess(User user);
}
