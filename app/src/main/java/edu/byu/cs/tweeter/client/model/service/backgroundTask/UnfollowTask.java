package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "UnfollowTask";

    /**
     * The user that is being unfollowed.
     */
    private User followee;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
    }

    @Override
    protected void executeTask() throws IOException, TweeterRemoteException {
        // TODO: Doesn't do anything currently
        UnfollowRequest unfollowRequest = new UnfollowRequest(getAuthToken(), followee.getAlias());
        Response unfollowResponse = getServerFacade().unfollow(unfollowRequest, "/unfollow");
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // TODO: Nothing to override currently
    }
}
