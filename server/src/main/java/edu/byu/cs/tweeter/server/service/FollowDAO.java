package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.util.Pair;
import edu.byu.cs.tweeter.util.Triple;

public interface FollowDAO {
    public void createFollowee(User curUser, User followee) throws DataAccessException;
    public Pair<List<String>, Boolean> getFollowees(PagedRequest<User> followingRequest) throws DataAccessException;
    public Pair<List<String>, Boolean> getFollowers(PagedRequest<User> followerRequest) throws DataAccessException;
    public Triple<List<String>, Boolean, String> getFollowersAliases(String followeeAlias, int limit, String startKey) throws DataAccessException;
    public List<String> getAllFollowers(String followeeAlias) throws DataAccessException;
    public boolean queryFollowRelationship(String followerAlias, String followeeAlias) throws DataAccessException;
    public void deleteFollowee(User curUser, User followee) throws DataAccessException;
}
