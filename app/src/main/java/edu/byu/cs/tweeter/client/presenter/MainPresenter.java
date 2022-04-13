package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.ICountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserverInterface;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.view.MainView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {

    public interface View extends MainView {}

    private FollowService followService;
    private StatusService statusService;

    public MainPresenter(MainView view) {
        super(view);
        followService = new FollowService();
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public MainView getMainView() {
        return (MainView) getView();
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                        new CountObserver(false));
        followService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                        new CountObserver(true));
    }

    public void onFollowButtonClick(boolean wasFollowing, User selectedUser) {
        if (wasFollowing) {
            followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                    new FollowRelationshipObserver(selectedUser, true));
            getMainView().displayMessage("Removing " + selectedUser.getName() + "...");
        } else {
            followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                                    new FollowRelationshipObserver(selectedUser, false));
            getMainView().displayMessage("Adding " + selectedUser.getName() + "...");
        }
    }

    public void setFollowButton(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                                Cache.getInstance().getCurrUser(), selectedUser,
                                new IsFollowerObserver());
    }

    public void onLogoutButtonClicked() {
        getUserService().logout(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public void onStatusPosted(String post) {
        getMainView().displayPostMessage("Posting Status...");

        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(),
                    parseURLs(post), parseMentions(post));

            getStatusService().postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus,
                    createPostStatusObserver());
        } catch (Exception ex) {
            getMainView().logException(ex);
            getMainView().displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    public SimpleNotificationObserver createPostStatusObserver() {
        return new PostStatusObserver();
    }

    private String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    private List<String> parseURLs(String post) {
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

    public class CountObserver extends Observer implements ICountObserver {

        private boolean isFollowingCount;

        public CountObserver(boolean isFollowingCount) {
            this.isFollowingCount = isFollowingCount;
        }

        @Override
        public void handleSuccess(int count) {
            getMainView().setCount(isFollowingCount, count);
        }

        @Override
        protected String getMessagePrefix() {
            if (isFollowingCount) {
                return "Failed to get following count";
            } else {
                return "Failed to get followers count";
            }
        }
    }

    public class FollowRelationshipObserver extends Observer implements SimpleNotificationObserver {
        private User selectedUser;
        private boolean unfollowUser;

        public FollowRelationshipObserver(User selectedUser, boolean unfollowUser) {
            this.selectedUser = selectedUser;
            this.unfollowUser = unfollowUser;
        }

        @Override
        public void handleSuccess() {
            updateSelectedUserFollowingAndFollowers(selectedUser);
            getMainView().updateFollowButton(unfollowUser);
            getMainView().enableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            super.handleFailure(message);
            getMainView().enableFollowButton(true);
        }

        @Override
        public void handleException(Exception ex) {
            super.handleException(ex);
            getMainView().enableFollowButton(true);
        }

        @Override
        protected String getMessagePrefix() {
            if (unfollowUser) {
                return "Failed to unfollow";
            } else {
                return "Failed to follow";
            }
        }
    }

    public class IsFollowerObserver extends Observer implements IsFollowerObserverInterface {
        @Override
        public void handleSuccess(boolean isFollower) {
            getMainView().updateFollowButton(!isFollower);
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to determine following relationship";
        }
    }

    public class LogoutObserver extends Observer implements SimpleNotificationObserver {
        @Override
        public void handleSuccess() {
            getMainView().logoutUser();
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to logout";
        }
    }

    public class PostStatusObserver extends Observer implements SimpleNotificationObserver {
        @Override
        public void handleSuccess() {
            getMainView().clearPostMessage();
            getMainView().displayMessage("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            getMainView().clearPostMessage();
            super.handleFailure(message);
        }

        @Override
        public void handleException(Exception ex) {
            getMainView().clearPostMessage();
            super.handleException(ex);
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to post status";
        }
    }
}
