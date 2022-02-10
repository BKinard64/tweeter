package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.view.View;

public class Presenter {

    private View view;

    public Presenter(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public abstract class Observer implements ServiceObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage(getMessagePrefix() + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage(getMessagePrefix() + " because of exception: " + ex.getMessage());
        }

        protected abstract String getMessagePrefix();
    }
}
