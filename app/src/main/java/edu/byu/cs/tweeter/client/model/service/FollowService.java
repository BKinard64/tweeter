package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface GetFollowingObserver extends PagedObserver<User>{}

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, GetFollowingObserver getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PagedTaskHandler<User>(getFollowingObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public interface GetFollowersObserver extends PagedObserver<User> {}

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, GetFollowersObserver getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PagedTaskHandler<User>(getFollowersObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public interface GetFollowingCountObserver {
        void handleSuccess(int count);
        void handleFailure(String message);
        void handleException(Exception ex);
    }

    public void getFollowingCount(AuthToken currUserAuthToken, User selectedUser, ExecutorService executor, GetFollowingCountObserver getFollowingCountObserver) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new GetFollowingCountHandler(getFollowingCountObserver));
        executor.execute(followingCountTask);
    }

    // GetFollowingCountHandler

    public interface GetFollowersCountObserver {
        void handleSuccess(int count);
        void handleFailure(String message);
        void handleException(Exception ex);
    }

    public void getFollowersCount(AuthToken currUserAuthToken, User selectedUser, ExecutorService executor, GetFollowersCountObserver getFollowersCountObserver) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetFollowersCountHandler(getFollowersCountObserver));
        executor.execute(followersCountTask);
    }

    // GetFollowersCountHandler

    public interface FollowObserver extends SimpleNotificationObserver {}

    public void follow(AuthToken currUserAuthToken, User selectedUser, FollowObserver followObserver) {
        FollowTask followTask = new FollowTask(currUserAuthToken, selectedUser,
                                                new SimpleNotificationHandler(followObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    // FollowHandler

    public interface UnfollowObserver extends SimpleNotificationObserver {}

    public void unfollow(AuthToken currUserAuthToken, User selectedUser, UnfollowObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken, selectedUser,
                                                        new SimpleNotificationHandler(unfollowObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    // UnfollowHandler

    public interface IsFollowerObserver {
        void handleSuccess(boolean isFollower);
        void handleFailure(String message);
        void handleException(Exception ex);
    }

    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser, IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken,
                currUser, selectedUser, new IsFollowerHandler(isFollowerObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    // IsFollowerHandler

}
