package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.CountTask;
import edu.byu.cs.tweeter.client.model.service.observer.ICountObserver;

public class CountHandler extends BackgroundTaskHandler<ICountObserver> {
    public CountHandler(ICountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, ICountObserver observer) {
        int count = data.getInt(CountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
