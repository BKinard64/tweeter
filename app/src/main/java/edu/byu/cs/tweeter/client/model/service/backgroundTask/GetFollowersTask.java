package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends BackgroundTask {
    private static final String LOG_TAG = "GetFollowersTask";

    public static final String FOLLOWERS_KEY = "followers";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * Auth token for logged-in user.
     */
    private AuthToken authToken;
    /**
     * The user whose followers are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;
    /**
     * Maximum number of followers to return (i.e., page size).
     */
    private int limit;
    /**
     * The last follower returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private User lastFollower;

    private List<User> followers;
    private boolean hasMorePages;

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler);
        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastFollower = lastFollower;
    }

    @Override
    protected void executeTask() {
        Pair<List<User>, Boolean> pageOfUsers = getFollowers();

        followers = pageOfUsers.getFirst();
        hasMorePages = pageOfUsers.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(FOLLOWERS_KEY, (Serializable) followers);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }

    private Pair<List<User>, Boolean> getFollowers() {
        Pair<List<User>, Boolean> pageOfUsers = getFakeData().getPageOfUsers(lastFollower, limit, targetUser);
        return pageOfUsers;
    }
}
