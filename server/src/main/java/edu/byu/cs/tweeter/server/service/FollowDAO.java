package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

public interface FollowDAO {
    public void createFollowee(User user);
    public void createFollower(User user);
    public FollowingResponse getFollowees(PagedRequest<User> followingRequest);
    public FollowerResponse getFollowers(PagedRequest<User> followerRequest);
    public Integer getFolloweeCount(User follower);
    public Integer getFollowerCount(User followee);
    public void deleteFollowee(User user);
    public void deleteFollower(User user);
}
