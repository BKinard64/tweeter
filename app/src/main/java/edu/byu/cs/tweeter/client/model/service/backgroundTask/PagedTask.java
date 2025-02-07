package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthenticatedTask {

    public static final String ITEMS_KEY = "followers";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose items are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;
    /**
     * Maximum number of items to return (i.e., page size).
     */
    private int limit;
    /**
     * The last item returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private T lastItem;

    private List<T> items;
    private boolean hasMorePages;

    public PagedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, T lastItem) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public int getLimit() {
        return limit;
    }

    public T getLastItem() {
        return lastItem;
    }

    @Override
    protected void executeTask() throws IOException, TweeterRemoteException {
        Pair<List<T>, Boolean> pageOfUsers = getItems(getAuthToken(), targetUser, limit, lastItem);

        items = pageOfUsers.getFirst();
        hasMorePages = pageOfUsers.getSecond();
    }

    protected abstract Pair<List<T>, Boolean> getItems(AuthToken authToken, User targetUser, int limit, T lastItem) throws IOException, TweeterRemoteException;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }
}
