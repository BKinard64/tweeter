package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "FollowTask";

    /**
     * The user that is being followed.
     */
    private User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
    }

    @Override
    protected void executeTask() throws IOException, TweeterRemoteException {
        // TODO: Doesn't do anything currently
        TargetUserRequest followRequest = new TargetUserRequest(getAuthToken(), followee.getAlias());
        Response followResponse = getServerFacade().follow(followRequest, "/follow");
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // TODO: Nothing to override currently
    }
}
