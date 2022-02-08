package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface IAuthenticationObserver extends ServiceObserver {
    void handleSuccess(User authenticatedUser, AuthToken authToken);
}
