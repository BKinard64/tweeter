package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    public interface View {
        void displayMessage(String message);
        void setCount(boolean isFollowingCount, int count);
        void updateFollowButton(boolean removed);
        void enableFollowButton(boolean value);
    }

    private View view;
    private UserService userService;
    private FollowService followService;
    private StatusService statusService;

    public MainPresenter(View view) {
        this.view = view;
        userService = new UserService();
        followService = new FollowService();
        statusService = new StatusService();
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        followService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                        executor, new GetFollowersCountObserver());
        followService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                        executor, new GetFollowingCountObserver());
    }

    public void onFollowButtonClick(boolean wasFollowing, User selectedUser) {
        if (wasFollowing) {
            followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                    new UnfollowObserver(selectedUser));
            view.displayMessage("Removing " + selectedUser.getName() + "...");
        } else {
            followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                    new FollowObserver(selectedUser));
            view.displayMessage("Adding " + selectedUser.getName() + "...");
        }
    }

    public class GetFollowersCountObserver implements FollowService.GetFollowersCountObserver {
        @Override
        public void handleSuccess(int count) {
            view.setCount(false, count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }

    public class GetFollowingCountObserver implements FollowService.GetFollowingCountObserver {
        @Override
        public void handleSuccess(int count) {
            view.setCount(true, count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get following count because of exception: " + ex.getMessage());
        }
    }

    public class FollowObserver implements FollowService.FollowObserver {
        private User selectedUser;

        public FollowObserver(User selectedUser) {
            this.selectedUser = selectedUser;
        }

        @Override
        public void handleSuccess() {
            updateSelectedUserFollowingAndFollowers(selectedUser);
            view.updateFollowButton(false);
            view.enableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to follow: " + message);
            view.enableFollowButton(true);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to follow because of exception: " + ex.getMessage());
            view.enableFollowButton(true);
        }
    }

    public class UnfollowObserver implements FollowService.UnfollowObserver {
        private User selectedUser;

        public UnfollowObserver(User selectedUser) {
            this.selectedUser = selectedUser;
        }

        @Override
        public void handleSuccess() {
            updateSelectedUserFollowingAndFollowers(selectedUser);
            view.updateFollowButton(true);
            view.enableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to unfollow: " + message);
            view.enableFollowButton(true);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
            view.enableFollowButton(true);
        }
    }
}
