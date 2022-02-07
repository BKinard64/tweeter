package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticationHandler extends BackgroundTaskHandler<AuthenticationObserver>{
    public AuthenticationHandler(AuthenticationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, AuthenticationObserver observer) {
        User authenticatedUser = (User) data.getSerializable(AuthenticateTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);

        observer.handleSuccess(authenticatedUser, authToken);
    }
}
