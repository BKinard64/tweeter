package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    private FollowService followService;

    public FollowersPresenter(PagedView<User> view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    protected void getItems(User user) {
        followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                                    getLastItem(), new GetFollowersObserver());
    }

    public class GetFollowersObserver extends GetItemsObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to get followers";
        }
    }
}
