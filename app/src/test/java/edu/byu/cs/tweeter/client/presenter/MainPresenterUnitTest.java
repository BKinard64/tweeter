package edu.byu.cs.tweeter.client.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.MainView;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest {

    private MainView mockView;
    private StatusService mockStatusService;

    private MainPresenter mainPresenterSpy;

    @Before
    public void setup() {
        // Create mocks
        mockView = Mockito.mock(MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
    }

    @Test
    public void testPostStatus_postStatusSuccess() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                verifyParameters(invocation);
                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        verifyPostStatus(answer, "Successfully Posted!");
    }

    @Test
    public void testPostStatus_postStatusFailedWithMessage() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                verifyParameters(invocation);
                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
                observer.handleFailure("the error message");
                return null;
            }
        };

        verifyPostStatus(answer, "Failed to post status: the error message");
    }

    @Test
    public void testPostStatus_postStatusFailedWithException() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                verifyParameters(invocation);
                MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
                observer.handleException(new Exception("the exception message"));
                return null;
            }
        };

        verifyPostStatus(answer, "Failed to post status because of exception: the exception message");
    }

    private void verifyParameters(InvocationOnMock invocation) {
        Assert.assertNotNull(invocation.getArgument(1));
        Assert.assertNotNull(invocation.getArgument(2));

        Status status = invocation.getArgument(1, Status.class);
        Assert.assertEquals("Test Status", status.getPost());
    }

    private void verifyPostStatus(Answer<Void> answer, String message) {
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.onStatusPosted("Test Status");

        Mockito.verify(mockView).displayPostMessage("Posting Status...");
        Mockito.verify(mockView).clearPostMessage();
        Mockito.verify(mockView).displayMessage(message);
    }
}
