package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    private static final String SERVER_URL = "https://ganaeo2std.execute-api.us-west-1.amazonaws.com/dev";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public AuthenticationResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        AuthenticationResponse response = clientCommunicator.doPost(urlPath, request, null, AuthenticationResponse.class);

        return (AuthenticationResponse) verifyResponseSuccess(response);
    }

    /**
     * Performs a register and if successful, returns the registered/logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the register response.
     */
    public AuthenticationResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        AuthenticationResponse response = clientCommunicator.doPost(urlPath, request, null, AuthenticationResponse.class);

        return (AuthenticationResponse) verifyResponseSuccess(response);
    }

    /**
     * Returns the statuses of users that the user specified in the request is following. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains information about the user whose feed is to be returned and any
     *                other information required to satisfy the request.
     * @return the statuses.
     */
    public FeedResponse getFeed(PagedRequest<Status> request, String urlPath)
            throws IOException, TweeterRemoteException {

        FeedResponse response = clientCommunicator.doPost(urlPath, request, null, FeedResponse.class);

        return (FeedResponse) verifyResponseSuccess(response);
    }

    /**
     * Returns the statuses of the user specified in the request. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains information about the user whose story is to be returned and any
     *                other information required to satisfy the request.
     * @return the statuses.
     */
    public StoryResponse getStory(PagedRequest<Status> request, String urlPath)
            throws IOException, TweeterRemoteException {

        StoryResponse response = clientCommunicator.doPost(urlPath, request, null, StoryResponse.class);

        return (StoryResponse) verifyResponseSuccess(response);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(PagedRequest<User> request, String urlPath)
            throws IOException, TweeterRemoteException {

        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

        return (FollowingResponse) verifyResponseSuccess(response);
    }

    /**
     * Returns the users that the user specified in the request is being followed by. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers.
     */
    public FollowerResponse getFollowers(PagedRequest<User> request, String urlPath)
            throws IOException, TweeterRemoteException {

        FollowerResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerResponse.class);

        return (FollowerResponse) verifyResponseSuccess(response);
    }

    /**
     * Returns the number of users that the user specified in the request is following.
     *
     * @param request contains the alias of the user to get the following count of
     * @return the count
     */
    public CountResponse getFollowingCount(TargetUserRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        CountResponse response = clientCommunicator.doPost(urlPath, request, null, CountResponse.class);

        return (CountResponse) verifyResponseSuccess(response);
    }

    /**
     * Returns the number of users that the user specified in the request is being followed by.
     *
     * @param request contains the alias of the user to get the followers count of
     * @return the count
     */
    public CountResponse getFollowersCount(TargetUserRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        CountResponse response = clientCommunicator.doPost(urlPath, request, null, CountResponse.class);

        return (CountResponse) verifyResponseSuccess(response);
    }

    /**
     * Returns the user specified in the request.
     *
     * @param request contains the alias of the user being retrieved.
     * @return the user.
     */
    public UserResponse getUser(TargetUserRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        UserResponse response = clientCommunicator.doPost(urlPath, request, null, UserResponse.class);

        return (UserResponse) verifyResponseSuccess(response);
    }

    /**
     * Follows the user specified in the request.
     *
     * @param request contains the alias of the user to follow
     * @return success message
     */
    public Response follow(TargetUserRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        Response response = clientCommunicator.doPost(urlPath, request, null, Response.class);

        return verifyResponseSuccess(response);
    }

    /**
     * Unfollows the user specified in the request.
     *
     * @param request contains the alias of the user to unfollow
     * @return success message
     */
    public Response unfollow(TargetUserRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        Response response = clientCommunicator.doPost(urlPath, request, null, Response.class);

        return verifyResponseSuccess(response);
    }

    /**
     * Determines if the users specified have a following relationship
     *
     * @param request contains the aliases of the users whose following relationship is being queried
     * @return whether or not a following relationship exists
     */
    public IsFollowerResponse isFollower(IsFollowerRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        IsFollowerResponse response = clientCommunicator.doPost(urlPath, request, null, IsFollowerResponse.class);

        return (IsFollowerResponse) verifyResponseSuccess(response);
    }

    /**
     * Post a status to the database
     *
     * @param request contains the status to be posted
     * @return success message
     */
    public Response postStatus(StatusRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        Response response = clientCommunicator.doPost(urlPath, request, null, Response.class);

        return verifyResponseSuccess(response);
    }

    /**
     * Log the current user out (removing their AuthToken from the database)
     *
     * @param request the AuthToken to remove
     * @return success message
     */
    public Response logout(AuthenticatedRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        Response response = clientCommunicator.doPost(urlPath, request, null, Response.class);

        return verifyResponseSuccess(response);
    }

    private Response verifyResponseSuccess(Response response) {
        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }
}