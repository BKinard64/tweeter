package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends CountTask {
    private static final String LOG_TAG = "GetFollowingCountTask";

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected void executeTask() throws IOException, TweeterRemoteException {
        // TODO: Doesn't do anything currently
        FollowingCountRequest followingCountRequest = new FollowingCountRequest(getAuthToken(), getTargetUser().getAlias());
        CountResponse followingCountResponse = getServerFacade().getFollowingCount(followingCountRequest, "/getfollowingcount");

        setCount(followingCountResponse.getCount());
    }
}
