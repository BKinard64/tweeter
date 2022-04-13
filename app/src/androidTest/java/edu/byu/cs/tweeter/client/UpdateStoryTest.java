package edu.byu.cs.tweeter.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.view.MainView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public class UpdateStoryTest {

    private MainView mockView;
    private MainPresenter spyPresenter;
    private MockPostStatusObserver mockPostStatusObserver;
    private ServerFacade serverFacade;

    private CountDownLatch countDownLatch;

    @Before
    public void setup() {
        mockView = Mockito.mock(MainView.class);
        spyPresenter = Mockito.spy(new MainPresenter(mockView));
        mockPostStatusObserver = new MockPostStatusObserver();
        serverFacade = new ServerFacade();

        Mockito.when(spyPresenter.getMainView()).thenReturn(mockView);
        Mockito.when(spyPresenter.createPostStatusObserver()).thenReturn(mockPostStatusObserver);

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    private class MockPostStatusObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            mockView.clearPostMessage();
            mockView.displayMessage("Successfully Posted!");

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception ex) {
            countDownLatch.countDown();
        }
    }

    @Test
    public void testUpdateStorySuccessfully() throws IOException, TweeterRemoteException, InterruptedException {
        // Login
        LoginRequest loginRequest = new LoginRequest("@user_a", "password");
        AuthenticationResponse loginResponse = serverFacade.login(loginRequest, "/login");

        Assert.assertTrue(loginResponse.isSuccess());
        Cache.getInstance().setCurrUser(loginResponse.getUser());
        Cache.getInstance().setCurrUserAuthToken(loginResponse.getAuthToken());

        // Call post status on presenter
        spyPresenter.onStatusPosted("JUnit Test Post.");
        awaitCountDownLatch();

        // Verify correct methods were called on the view
        Mockito.verify(mockView).displayPostMessage("Posting Status...");
        Mockito.verify(mockView).clearPostMessage();
        Mockito.verify(mockView).displayMessage("Successfully Posted!");


        // Get story from database
        PagedRequest<Status> getStoryRequest = new PagedRequest<Status>(Cache.getInstance().getCurrUserAuthToken(),
                                                                Cache.getInstance().getCurrUser().getAlias(),
                                                                10, null);
        StoryResponse getStoryResponse = serverFacade.getStory(getStoryRequest, "/getstory");

        Assert.assertTrue(getStoryResponse.isSuccess());

        // Verify status posted was added to user's story
        List<Status> story = getStoryResponse.getItems();
        Status actualStatus = story.get(0);
        Assert.assertEquals("JUnit Test Post.", actualStatus.getPost());
        Assert.assertEquals(Cache.getInstance().getCurrUser(), actualStatus.getUser());
        Assert.assertNotNull(actualStatus.getDate());
    }
}
