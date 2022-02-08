package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.view.View;

public class Presenter {

    private View view;

    public Presenter(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
