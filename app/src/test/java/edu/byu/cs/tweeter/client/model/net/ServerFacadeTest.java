package edu.byu.cs.tweeter.client.model.net;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest {

    private ServerFacade serverFacade;
    private FakeData fakeData;

    @Before
    public void setup() {
        serverFacade = new ServerFacade();
        fakeData = new FakeData();
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest("FirstName", "LastName", "@username", "password", "imageByteString");
        AuthenticationResponse response = serverFacade.register(request, "/register");

        Assert.assertTrue(response.isSuccess());
        Assert.assertNull(response.getMessage());

        User expectedUser = fakeData.getFirstUser();
        AuthToken expectedAuthToken = fakeData.getAuthToken();

        Assert.assertEquals(expectedUser, response.getUser());
        Assert.assertEquals(expectedAuthToken.getToken(), response.getAuthToken().getToken());
    }

    @Test
    public void testRegister_invalidRequest_noFirstName() {
        RegisterRequest request = new RegisterRequest(null, "LastName", "@username", "password", "imageByteString");

        Assert.assertThrows(TweeterRequestException.class, () -> {serverFacade.register(request, "/register");});
    }

    @Test
    public void testRegister_invalidRequest_noLastName() {
        RegisterRequest request = new RegisterRequest("FirstName", null, "@username", "password", "imageByteString");

        Assert.assertThrows(TweeterRequestException.class, () -> {serverFacade.register(request, "/register");});
    }

    @Test
    public void testRegister_invalidRequest_noUsername() {
        RegisterRequest request = new RegisterRequest("FirstName", "LastName", null, "password", "imageByteString");

        Assert.assertThrows(TweeterRequestException.class, () -> {serverFacade.register(request, "/register");});
    }

    @Test
    public void testRegister_invalidRequest_noPassword() {
        RegisterRequest request = new RegisterRequest("FirstName", "LastName", "@username", null, "imageByteString");

        Assert.assertThrows(TweeterRequestException.class, () -> {serverFacade.register(request, "/register");});
    }

    @Test
    public void testRegister_invalidRequest_noImage() {
        RegisterRequest request = new RegisterRequest("FirstName", "LastName", "@username", "password", null);

        Assert.assertThrows(TweeterRequestException.class, () -> {serverFacade.register(request, "/register");});
    }

    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        PagedRequest<User> request = new PagedRequest<>(new AuthToken(), "@username", 3, null);
        FollowerResponse response = serverFacade.getFollowers(request, "/getfollowers");

        Assert.assertTrue(response.isSuccess());
        Assert.assertNull(response.getMessage());

        List<User> expectedFollowers = fakeData.getFakeUsers().subList(0, 3);

        Assert.assertEquals(expectedFollowers, response.getItems());
        Assert.assertTrue(response.isSuccess());
    }

    @Test
    public void testGetFollowers_invalidRequest_noAuthToken() {
        PagedRequest<User> request = new PagedRequest<>(null, "@username", 3, null);

        Assert.assertThrows(TweeterRequestException.class, () -> {serverFacade.getFollowers(request, "/getfollowers");});
    }

    @Test
    public void testGetFollowers_invalidRequest_noUsername() {
        PagedRequest<User> request = new PagedRequest<>(new AuthToken(), null, 3, null);

        Assert.assertThrows(TweeterRequestException.class, () -> {serverFacade.getFollowers(request, "/getfollowers");});
    }

    @Test
    public void testGetFollowers_invalidRequest_nonPositiveLimit() {
        PagedRequest<User> request = new PagedRequest<>(new AuthToken(), "@username", -1, null);

        Assert.assertThrows(TweeterRequestException.class, () -> {serverFacade.getFollowers(request, "/getfollowers");});
    }

    @Test
    public void testGetFollowingCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        TargetUserRequest request = new TargetUserRequest(new AuthToken(), "@username");
        CountResponse response = serverFacade.getFollowingCount(request, "/getfollowingcount");

        Assert.assertTrue(response.isSuccess());
        Assert.assertNull(response.getMessage());

        Assert.assertEquals(20, response.getCount());
    }

    @Test
    public void testGetFollowingCount_invalidRequest_noAuthToken() {
        TargetUserRequest request = new TargetUserRequest(null, "@username");

        Assert.assertThrows(TweeterRequestException.class, ()->{serverFacade.getFollowingCount(request, "/getfollowingcount");});
    }

    @Test
    public void testGetFollowingCount_invalidRequest_noUsername() {
        TargetUserRequest request = new TargetUserRequest(new AuthToken(), null);

        Assert.assertThrows(TweeterRequestException.class, ()->{serverFacade.getFollowingCount(request, "/getfollowingcount");});
    }
}
