package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends BackgroundTask {
    private static final String LOG_TAG = "PostStatusTask";

    /**
     * Auth token for logged-in user.
     */
    private AuthToken authToken;
    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private Status status;

    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(messageHandler);
        this.authToken = authToken;
        this.status = status;
    }

    @Override
    protected void executeTask() {
        // TODO: Nothing to override currently
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // TODO: Nothing to override currently
    }
}
