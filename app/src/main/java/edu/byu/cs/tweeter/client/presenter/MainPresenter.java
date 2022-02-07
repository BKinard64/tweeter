package edu.byu.cs.tweeter.client.presenter;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    public interface View {
        void displayMessage(String message);
        void setCount(boolean isFollowingCount, int count);
        void updateFollowButton(boolean removed);
        void enableFollowButton(boolean value);
        void successfulPost();
        void logoutUser();
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
        followService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                        new GetFollowersCountObserver());
        followService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                        new GetFollowingCountObserver());
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

    public void setFollowButton(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                                Cache.getInstance().getCurrUser(), selectedUser,
                                new IsFollowerObserver());
    }

    public void onLogoutButtonClicked() {
        userService.logout(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public void onStatusPosted(String post) throws ParseException, MalformedURLException {
        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(),
                                        parseURLs(post), parseMentions(post));
        statusService.postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus,
                                new PostStatusObserver());
    }

    private String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    private List<String> parseURLs(String post) throws MalformedURLException {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    private int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public class GetFollowersCountObserver implements CountObserver {
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

    public class GetFollowingCountObserver implements CountObserver {
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

    public class FollowObserver implements SimpleNotificationObserver {
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

    public class UnfollowObserver implements SimpleNotificationObserver {
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

    public class IsFollowerObserver implements edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver {
        @Override
        public void handleSuccess(boolean isFollower) {
            view.updateFollowButton(!isFollower);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }

    public class LogoutObserver implements SimpleNotificationObserver {
        @Override
        public void handleSuccess() {
            view.logoutUser();
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }
    }

    public class PostStatusObserver implements SimpleNotificationObserver {
        @Override
        public void handleSuccess() {
            view.successfulPost();
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to post status because of exception: " + ex.getMessage());
        }
    }
}
