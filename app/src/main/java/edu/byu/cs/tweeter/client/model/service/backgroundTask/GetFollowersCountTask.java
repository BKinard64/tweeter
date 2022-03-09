package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends CountTask {
    private static final String LOG_TAG = "GetFollowersCountTask";

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected void executeTask() throws IOException, TweeterRemoteException {
        // TODO: Doesn't do anything currently
        FollowersCountRequest followersCountRequest = new FollowersCountRequest(getAuthToken(), getTargetUser().getAlias());
        CountResponse followersCountResponse = getServerFacade().getFollowersCount(followersCountRequest, "/getfollowerscount");

        setCount(followersCountResponse.getCount());
    }
}
