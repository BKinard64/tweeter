package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.service.AuthTokenDAO;
import edu.byu.cs.tweeter.server.service.DAOFactory;
import edu.byu.cs.tweeter.server.service.FeedDAO;
import edu.byu.cs.tweeter.server.service.FollowDAO;
import edu.byu.cs.tweeter.server.service.StoryDAO;
import edu.byu.cs.tweeter.server.service.UserDAO;

public class DynamoDAOFactory implements DAOFactory {
    @Override
    public AuthTokenDAO getAuthTokenDAO() {
        return new DynamoAuthTokenDAO();
    }

    @Override
    public UserDAO getUserDAO() {
        return new DynamoUserDAO();
    }

    @Override
    public FeedDAO getFeedDAO() {
        return new DynamoFeedDAO();
    }

    @Override
    public StoryDAO getStoryDAO() {
        return new DynamoStoryDAO();
    }

    @Override
    public FollowDAO getFollowDAO() {
        return new DynamoFollowDAO();
    }
}
