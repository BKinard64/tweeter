package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;

public interface FollowDAO {
    public void createFollowee(String followerAlias);
    public void createFollower(User user);
    public FollowingResponse getFollowees(PagedRequest<User> followingRequest);
    public FollowerResponse getFollowers(PagedRequest<User> followerRequest);
    public IsFollowerResponse queryFollowRelationship(String followerAlias, String followeeAlias);
    public Integer getFolloweeCount(String followerAlias);
    public Integer getFollowerCount(String followeeAlias);
    public void deleteFollowee(String followerAlias);
    public void deleteFollower(User user);
}
