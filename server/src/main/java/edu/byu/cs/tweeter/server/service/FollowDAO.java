package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowDAO {
    public void createFollowee(User curUser, User followee) throws DataAccessException;
    public void createFollower(User user);
    public Pair<List<String>, Boolean> getFollowees(PagedRequest<User> followingRequest) throws DataAccessException;
    public Pair<List<String>, Boolean> getFollowers(PagedRequest<User> followerRequest) throws DataAccessException;
    public boolean queryFollowRelationship(String followerAlias, String followeeAlias) throws DataAccessException;
    public void deleteFollowee(User curUser, User followee) throws DataAccessException;
    public void deleteFollower(User user);
}
