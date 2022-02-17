package edu.byu.cs.tweeter.client.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.MainView;

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

    }

    @Test
    public void testPostStatus_postStatusFailedWithMessage() {

    }

    @Test
    public void testPostStatus_postStatusFailedWithException() {

    }
}
