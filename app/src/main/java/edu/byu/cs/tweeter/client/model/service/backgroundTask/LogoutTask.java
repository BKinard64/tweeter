package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {
    private static final String LOG_TAG = "LogoutTask";

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler, authToken);
    }

    @Override
    protected void executeTask() throws IOException, TweeterRemoteException {
        // TODO: Doesn't do anything currently
        AuthenticatedRequest logoutRequest = new AuthenticatedRequest(getAuthToken());
        Response logoutResponse = getServerFacade().logout(logoutRequest, "/logout");
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // TODO: Nothing to override currently
    }
}
