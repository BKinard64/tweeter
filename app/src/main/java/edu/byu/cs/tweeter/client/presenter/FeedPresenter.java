package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    private StatusService statusService;

    public FeedPresenter(PagedView<Status> view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void getItems(User user) {
        statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                getLastItem(), new GetFeedObserver());
    }

    public class GetFeedObserver extends GetItemsObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get feed";
        }
    }
}
