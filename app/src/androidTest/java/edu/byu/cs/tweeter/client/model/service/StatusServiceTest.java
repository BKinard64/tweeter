package edu.byu.cs.tweeter.client.model.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest {

    private User currentUser;
    private AuthToken currentAuthToken;

    private StatusService statusServiceSpy;
    private MockGetStoryObserver mockGetStoryObserver;

    private CountDownLatch countDownLatch;

    @Before
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        currentAuthToken = new AuthToken();

        statusServiceSpy = Mockito.spy(new StatusService());

        // Set up an observer for the StatusService that will decrement the countdown latch
        mockGetStoryObserver = new MockGetStoryObserver();

        // Prepare the countdown latch
        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    private class MockGetStoryObserver implements PagedObserver<Status> {
        private boolean success;
        private String message;
        private List<Status> items;
        private boolean hasMorePages;

        @Override
        public void handleSuccess(List<Status> items, boolean hasMorePages) {
            success = true;
            message = null;
            this.items = items;
            this.hasMorePages = hasMorePages;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {

        }

        @Override
        public void handleException(Exception ex) {

        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getItems() {
            return items;
        }

        public boolean hasMorePages() {
            return hasMorePages;
        }
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws InterruptedException {
        statusServiceSpy.getStory(currentAuthToken, currentUser, 3, null, mockGetStoryObserver);
        awaitCountDownLatch();

        Assert.assertTrue(mockGetStoryObserver.isSuccess());
        Assert.assertNull(mockGetStoryObserver.getMessage());

        List<Status> expectedStatuses = new FakeData().getFakeStatuses().subList(0, 3);

        Assert.assertEquals(3, mockGetStoryObserver.getItems().size());
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(expectedStatuses.get(i).getUser(), mockGetStoryObserver.getItems().get(i).getUser());
            Assert.assertEquals(expectedStatuses.get(i).getPost(), mockGetStoryObserver.getItems().get(i).getPost());
            Assert.assertEquals(expectedStatuses.get(i).getMentions(), mockGetStoryObserver.getItems().get(i).getMentions());
            Assert.assertEquals(expectedStatuses.get(i).getUrls(), mockGetStoryObserver.getItems().get(i).getUrls());
        }
        Assert.assertTrue(mockGetStoryObserver.hasMorePages());
    }
}
