package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;

    private User authenticatedUser;
    private AuthToken authToken;

    public AuthenticateTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void executeTask() throws IOException, TweeterRemoteException {
        Pair<User, AuthToken> authenticationResult = doAuthentication(username, password);

        authenticatedUser = authenticationResult.getFirst();
        authToken = authenticationResult.getSecond();
    }

    protected abstract Pair<User, AuthToken> doAuthentication(String username, String password) throws IOException, TweeterRemoteException;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

    protected Pair<User, AuthToken> createUserAuthTokenPair(AuthenticationResponse response) {
        User authenticatedUser = response.getUser();
        AuthToken authToken = response.getAuthToken();
        return new Pair<>(authenticatedUser, authToken);
    }
}
