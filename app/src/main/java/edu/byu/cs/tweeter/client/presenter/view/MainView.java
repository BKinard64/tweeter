package edu.byu.cs.tweeter.client.presenter.view;

public interface MainView extends View {
    void displayPostMessage(String message);
    void clearPostMessage();
    void logException(Exception ex);
    void setCount(boolean isFollowingCount, int count);
    void updateFollowButton(boolean removed);
    void enableFollowButton(boolean value);
}
