package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.dynamodbv2.xspec.S;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.util.Pair;

public class GetStoryTest {

    private StatusService spyService;
    private DAOFactory mockDAOFactory;
    private AuthTokenDAO mockAuthTokenDAO;
    private UserDAO mockUserDAO;
    private StoryDAO mockStoryDAO;

    private List<Status> statuses;
    private Pair<List<Status>, Boolean> successPair;
    private AuthToken authToken;
    private User targetUser;
    private int pageLimit;
    private Status lastStatus;
    private PagedRequest<Status> getStoryRequest;

    private StoryResponse expectedResponse;
    private StoryResponse actualResponse;

    @BeforeEach
    public void setup() {
        mockDAOFactory = Mockito.mock(DAOFactory.class);
        mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        mockUserDAO = Mockito.mock(UserDAO.class);
        mockStoryDAO = Mockito.mock(StoryDAO.class);

        spyService = Mockito.spy(new StatusService(mockDAOFactory));
        Mockito.when(spyService.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
        Mockito.when(spyService.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(spyService.getStoryDAO()).thenReturn(mockStoryDAO);

        authToken = new AuthToken("token");
        targetUser = new User("firstName", "lastName", "imageURL");
        List<String> urls = new ArrayList<>();
        List<String> mentions = new ArrayList<>();
        lastStatus = new Status("post2", targetUser, null, urls,  mentions);

        statuses = Arrays.asList(new Status("post1", targetUser, null, urls, mentions),
                                lastStatus,
                                new Status("post3", targetUser, null, urls, mentions),
                                new Status("post4", targetUser, null, urls, mentions));
    }

    @Test
    public void testGetStorySessionExpired() {
        getStoryRequest = new PagedRequest<>();
        Mockito.doReturn(false).when(spyService).verifyPagedRequest(getStoryRequest);
        expectedResponse = new StoryResponse("User Session expired. Logout and log back in to continue.");

        actualResponse = spyService.getStory(getStoryRequest);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetStoryReturnsPageOfStatuses() throws DataAccessException {
        pageLimit = 2;

        // FIRST PAGE
        getStoryRequest = new PagedRequest<>(authToken, targetUser.getAlias(), pageLimit, null);
        Mockito.doReturn(true).when(spyService).verifyPagedRequest(getStoryRequest);

        Mockito.doReturn(targetUser).when(mockUserDAO).getUser(getStoryRequest.getUserAlias());

        successPair = new Pair<>(statuses.subList(0, pageLimit), true);
        Mockito.doReturn(successPair).when(mockStoryDAO).getStory(getStoryRequest, targetUser);

        expectedResponse = new StoryResponse(successPair.getFirst(), successPair.getSecond());
        actualResponse = spyService.getStory(getStoryRequest);

        Assertions.assertEquals(expectedResponse, actualResponse);

        // SECOND PAGE
        getStoryRequest = new PagedRequest<>(authToken, targetUser.getAlias(), pageLimit, lastStatus);
        Mockito.doReturn(true).when(spyService).verifyPagedRequest(getStoryRequest);

        successPair = new Pair<>(statuses.subList(2, pageLimit), false);
        Mockito.doReturn(successPair).when(mockStoryDAO).getStory(getStoryRequest, targetUser);

        expectedResponse = new StoryResponse(successPair.getFirst(), successPair.getSecond());
        actualResponse = spyService.getStory(getStoryRequest);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}
