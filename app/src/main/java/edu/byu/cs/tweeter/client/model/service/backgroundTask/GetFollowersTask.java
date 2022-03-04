package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollower);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems(AuthToken authToken, User targetUser, int limit, User lastFollower) throws IOException, TweeterRemoteException {
        String lastFollowerAlias = null;
        if (lastFollower != null) {
            lastFollowerAlias = lastFollower.getAlias();
        }

        FollowerRequest followerRequest = new FollowerRequest(authToken, targetUser.getAlias(), limit, lastFollowerAlias);
        FollowerResponse followerResponse = getServerFacade().getFollowers(followerRequest, "/getfollowers");

        return new Pair<>(followerResponse.getFollowers(), followerResponse.getHasMorePages());
    }
}
