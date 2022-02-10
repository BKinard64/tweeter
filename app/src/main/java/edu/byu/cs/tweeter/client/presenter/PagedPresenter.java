package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.IGetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    protected static final int PAGE_SIZE = 10;

    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public PagedPresenter(PagedView<T> view) {
        super(view);
    }

    public PagedView<T> getPagedView() {
        return (PagedView<T>) getView();
    }

    public T getLastItem() {
        return lastItem;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            getPagedView().setLoadingStatus(true);

            getItems(user);
        }
    }

    protected abstract void getItems(User user);

    public void onUserClick(String userAlias) {
        getUserService().getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias,
                new GetUserObserver());
        getPagedView().displayMessage("Getting user's profile...");
    }

    public abstract class GetItemsObserver extends Observer implements PagedObserver<T> {

        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            getPagedView().setLoadingStatus(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);

            getPagedView().addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            getPagedView().setLoadingStatus(false);

            super.handleFailure(message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            getPagedView().setLoadingStatus(false);

            super.handleException(ex);
        }
    }

    public class GetUserObserver extends Observer implements IGetUserObserver {

        @Override
        public void handleSuccess(User user) {
            getPagedView().goToUserPage(user);
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to get user's profile";
        }
    }
}
