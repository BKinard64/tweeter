package edu.byu.cs.tweeter.client.presenter.view;

public interface MainView extends View {
    void setCount(boolean isFollowingCount, int count);
    void updateFollowButton(boolean removed);
    void enableFollowButton(boolean value);
    void successfulPost();
    void logoutUser();
}
