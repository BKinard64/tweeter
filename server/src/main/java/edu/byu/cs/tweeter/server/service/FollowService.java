package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DataAccessException;

public class FollowService extends Service {

    private FollowDAO followDAO;
    private UserDAO userDAO;

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

        try {
            int count = getUserDAO().getFolloweeCount(request.getTargetUserAlias());
            return new CountResponse(count);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to get following count");
        }
    }

    public CountResponse getFollowersCount(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        try {
            int count = getUserDAO().getFollowerCount(request.getTargetUserAlias());
            return new CountResponse(count);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to get followers count");
        }
    }

    public Response follow(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        addFollowee(request);

        return new Response(true);
    }

    public Response unfollow(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        removeFollowee(request);

        return new Response(true);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        verifyAuthenticatedRequest(request);

        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        try {
            boolean isFollower = getFollowDAO().queryFollowRelationship(request.getFollowerAlias(), request.getFolloweeAlias());
            return new IsFollowerResponse(isFollower);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Could not determine following relationship");
        }
    }

    private void addFollowee(TargetUserRequest request) {
        try {
            User curUser = getCurrentUser(request.getAuthToken());
            User targetUser = getTargetUser(request.getTargetUserAlias());

            getFollowDAO().createFollowee(curUser, targetUser);
            getUserDAO().updateFollowersCount(request.getTargetUserAlias(), 1);
            getUserDAO().updateFollowingCount(curUser.getAlias(), 1);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to add followee");
        }
    }

    private void removeFollowee(TargetUserRequest request) {
        try {
            User curUser = getCurrentUser(request.getAuthToken());
            User targetUser = getTargetUser(request.getTargetUserAlias());

            getFollowDAO().deleteFollowee(curUser, targetUser);
            getUserDAO().updateFollowersCount(request.getTargetUserAlias(), -1);
            getUserDAO().updateFollowingCount(curUser.getAlias(), -1);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to remove followee");
        }
    }

    private User getCurrentUser(AuthToken authToken) throws DataAccessException {
        String curUserAlias = getAuthTokenDAO().getUserAlias(authToken.getToken());
        return getUserDAO().getUser(curUserAlias);
    }

    private User getTargetUser(String targetUserAlias) throws DataAccessException {
        return getUserDAO().getUser(targetUserAlias);
    }

    public FollowDAO getFollowDAO() {
        if (followDAO == null) {
            followDAO = getDaoFactory().getFollowDAO();
        }
        return followDAO;
    }

    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = getDaoFactory().getUserDAO();
        }
        return userDAO;
    }
}
