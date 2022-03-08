package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
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

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Performs a register and if successful, returns the registered/logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the register response.
     */
    public AuthenticationResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        AuthenticationResponse response = clientCommunicator.doPost(urlPath, request, null, AuthenticationResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
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
    public FeedResponse getFeed(FeedRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        FeedResponse response = clientCommunicator.doPost(urlPath, request, null, FeedResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
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
    public StoryResponse getStory(StoryRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        StoryResponse response = clientCommunicator.doPost(urlPath, request, null, StoryResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
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
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
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
    public FollowerResponse getFollowers(FollowerRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        FollowerResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the number of users that the user specified in the request is following.
     *
     * @param request contains the alias of the user to get the following count of
     * @return success message
     */
    public Response getFollowingCount(FollowingCountRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        Response response = clientCommunicator.doPost(urlPath, request, null, Response.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the number of users that the user specified in the request is being followed by.
     *
     * @param request contains the alias of the user to get the followers count of
     * @return success message
     */
    public Response getFollowersCount(FollowersCountRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        Response response = clientCommunicator.doPost(urlPath, request, null, Response.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the user specified in the request.
     *
     * @param request contains the alias of the user being retrieved.
     * @return the user.
     */
    public UserResponse getUser(UserRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        UserResponse response = clientCommunicator.doPost(urlPath, request, null, UserResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Follows the user specified in the request.
     *
     * @param request contains the alias of the user to follow
     * @return success message
     */
    public Response follow(FollowRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        Response response = clientCommunicator.doPost(urlPath, request, null, Response.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Unfollows the user specified in the request.
     *
     * @param request contains the alias of the user to unfollow
     * @return success message
     */
    public Response unfollow(UnfollowRequest request, String urlPath)
            throws IOException, TweeterRemoteException {

        Response response = clientCommunicator.doPost(urlPath, request, null, Response.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
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

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }
}