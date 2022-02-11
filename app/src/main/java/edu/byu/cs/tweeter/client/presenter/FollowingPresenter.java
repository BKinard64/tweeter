package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    private FollowService followService;

    public FollowingPresenter(PagedView<User> view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void getItems(User user) {
        followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                getLastItem(), new GetFollowingObserver());
    }

    public class GetFollowingObserver extends GetItemsObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get following";
        }
    }
}
