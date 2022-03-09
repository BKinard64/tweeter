package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask {
    private static final String LOG_TAG = "IsFollowerTask";

    public static final String IS_FOLLOWER_KEY = "is-follower";

    /**
     * The alleged follower.
     */
    private User follower;
    /**
     * The alleged followee.
     */
    private User followee;
    /**
     * The status of the following relationship
     */
    private boolean isFollower;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    protected void executeTask() throws IOException, TweeterRemoteException {
        // TODO: Does nothing currently
        IsFollowerRequest isFollowerRequest = new IsFollowerRequest(getAuthToken(), follower.getAlias(), followee.getAlias());
        IsFollowerResponse isFollowerResponse = getServerFacade().isFollower(isFollowerRequest, "/isfollower");

        isFollower = isFollowerResponse.isFollower();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }
}
