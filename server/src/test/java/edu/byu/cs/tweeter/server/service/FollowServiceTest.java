package edu.byu.cs.tweeter.server.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;
import edu.byu.cs.tweeter.util.Pair;

public class FollowServiceTest {

    private PagedRequest<User> request;
    private FollowingResponse expectedResponse;
    private Pair<List<String>, Boolean> expectedPair;
    private DynamoFollowDAO mockDynamoFollowDAO;
    private DynamoUserDAO mockDynamoUserDAO;
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
        expectedPair = new Pair<List<String>, Boolean>(Arrays.asList(resultUser1.alias, resultUser2.alias, resultUser3.alias), false);
        mockDynamoUserDAO = Mockito.mock(DynamoUserDAO.class);
        expectedResponse = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockDynamoFollowDAO = Mockito.mock(DynamoFollowDAO.class);
        try {
            Mockito.when(mockDynamoFollowDAO.getFollowees(request)).thenReturn(expectedPair);
            Mockito.when(mockDynamoUserDAO.getUser(resultUser1.getAlias())).thenReturn(resultUser1);
            Mockito.when(mockDynamoUserDAO.getUser(resultUser2.getAlias())).thenReturn(resultUser2);
            Mockito.when(mockDynamoUserDAO.getUser(resultUser3.getAlias())).thenReturn(resultUser3);
        } catch (edu.byu.cs.tweeter.server.dao.DataAccessException e) {
            e.printStackTrace();
        }

        followServiceSpy = Mockito.spy(FollowService.class);
        Mockito.when(followServiceSpy.getFollowDAO()).thenReturn(mockDynamoFollowDAO);
        Mockito.when(followServiceSpy.getUserDAO()).thenReturn(mockDynamoUserDAO);
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
