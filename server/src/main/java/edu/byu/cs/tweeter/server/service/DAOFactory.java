package edu.byu.cs.tweeter.server.service;

public interface DAOFactory {
    public AuthTokenDAO getAuthTokenDAO();
    public UserDAO getUserDAO();
    public FeedDAO getFeedDAO();
    public StoryDAO getStoryDAO();
    public FollowDAO getFollowDAO();
}
