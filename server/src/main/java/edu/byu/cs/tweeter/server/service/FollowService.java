package edu.byu.cs.tweeter.server.service;

import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.FollowerDAO;

public class FollowService extends Service {
    public FollowingResponse getFollowees(PagedRequest<User> request) {
        verifyPagedRequest(request);

        return getFollowingDAO().getFollowees(request);
    }

    public FollowerResponse getFollowers(PagedRequest<User> request) {
        verifyPagedRequest(request);

        return getFollowerDAO().getFollowers(request);
    }

    public CountResponse getFollowingCount(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        return new CountResponse(20);
    }

    public CountResponse getFollowersCount(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        return new CountResponse(20);
    }

    public Response follow(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        return new Response(true);
    }

    public Response unfollow(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        return new Response(true);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        verifyAuthenticatedRequest(request);

        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        return new IsFollowerResponse(new Random().nextInt() > 0);
    }

    public FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }

    public FollowerDAO getFollowerDAO() {
        return new FollowerDAO();
    }
}
