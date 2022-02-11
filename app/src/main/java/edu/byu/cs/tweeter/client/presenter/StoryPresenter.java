package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    private StatusService statusService;

    public StoryPresenter(PagedView<Status> view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void getItems(User user) {
        statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                getLastItem(), new GetStoryObserver());
    }

    public class GetStoryObserver extends GetItemsObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get story";
        }
    }
}
