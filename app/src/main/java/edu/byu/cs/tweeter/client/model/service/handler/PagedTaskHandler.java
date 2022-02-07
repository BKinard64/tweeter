package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;

public class PagedTaskHandler<T> extends BackgroundTaskHandler<PagedObserver<T>>{
    public PagedTaskHandler(PagedObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver<T> observer) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
