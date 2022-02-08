package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.view.AuthenticationView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticationObserver implements IAuthenticationObserver {
    private AuthenticationView view;

    public AuthenticationObserver(AuthenticationView view) {
        this.view = view;
    }

    @Override
    public void handleSuccess(User authenticatedUser, AuthToken authToken) {
        Cache.getInstance().setCurrUser(authenticatedUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        view.authenticationSuccessful(authenticatedUser);
    }

    @Override
    public void handleFailure(String message) {
        view.displayMessage(getMessagePrefix() + ": " + message);
    }

    @Override
    public void handleException(Exception ex) {
        view.displayMessage(getMessagePrefix() + " because of exception: " + ex.getMessage());
    }

    protected abstract String getMessagePrefix();
}
