package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.DataAccessException;

public interface FollowDAO {
    public void createFollowee(User curUser, User followee) throws DataAccessException;
    public void createFollower(User user);
    public FollowingResponse getFollowees(PagedRequest<User> followingRequest);
    public FollowerResponse getFollowers(PagedRequest<User> followerRequest);
    public boolean queryFollowRelationship(String followerAlias, String followeeAlias) throws DataAccessException;
    public void deleteFollowee(User curUser, User followee) throws DataAccessException;
    public void deleteFollower(User user);
}
