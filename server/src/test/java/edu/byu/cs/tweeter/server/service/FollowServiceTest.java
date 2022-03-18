package edu.byu.cs.tweeter.server.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;

public class FollowServiceTest {

    private PagedRequest<User> request;
    private FollowingResponse expectedResponse;
    private DynamoFollowDAO mockDynamoFollowDAO;
    private FollowService followServiceSpy;

    @Before
    public void setup() {
        AuthToken authToken = new AuthToken();

        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new PagedRequest<>(authToken, currentUser.getAlias(), 3, null);

        // Setup a mock DynamoFollowDAO that will return known responses
        expectedResponse = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockDynamoFollowDAO = Mockito.mock(DynamoFollowDAO.class);
        Mockito.when(mockDynamoFollowDAO.getFollowees(request)).thenReturn(expectedResponse);

        followServiceSpy = Mockito.spy(FollowService.class);
        Mockito.when(followServiceSpy.getFollowDAO()).thenReturn(mockDynamoFollowDAO);
    }

    /**
     * Verify that the {@link FollowService#getFollowees(PagedRequest<User>)}
     * method returns the same result as the {@link DynamoFollowDAO} class.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() {
        FollowingResponse response = followServiceSpy.getFollowees(request);
        Assert.assertEquals(expectedResponse, response);
    }
}
