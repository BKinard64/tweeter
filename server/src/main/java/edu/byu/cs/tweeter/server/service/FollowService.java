package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

public class FollowService extends Service {

    private FollowDAO followDAO;

    public FollowService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public FollowingResponse getFollowees(PagedRequest<User> request) {
        verifyPagedRequest(request);

        return getFollowDAO().getFollowees(request);
    }

    public FollowerResponse getFollowers(PagedRequest<User> request) {
        verifyPagedRequest(request);

        return getFollowDAO().getFollowers(request);
    }

    public CountResponse getFollowingCount(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        int count = getFollowDAO().getFolloweeCount(request.getTargetUserAlias());
        return new CountResponse(count);
    }

    public CountResponse getFollowersCount(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        int count = getFollowDAO().getFollowerCount(request.getTargetUserAlias());
        return new CountResponse(count);
    }

    public Response follow(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        getFollowDAO().createFollowee(request.getTargetUserAlias());
        // TODO: Figure out how to update other user's followers when current user follows
        return new Response(true);
    }

    public Response unfollow(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        getFollowDAO().deleteFollowee(request.getTargetUserAlias());
        // TODO: Figure out how to update other user's followers when current user unfollows
        return new Response(true);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        verifyAuthenticatedRequest(request);

        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        return getFollowDAO().queryFollowRelationship(request.getFollowerAlias(), request.getFolloweeAlias());
    }

    public FollowDAO getFollowDAO() {
        if (followDAO == null) {
            followDAO = getDaoFactory().getFollowDAO();
        }
        return followDAO;
    }
}
