package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.AuthenticationView;

public class LoginPresenter extends AuthenticationPresenter {

    public LoginPresenter(AuthenticationView view) {
        super(view);
    }

    public void validateLogin(String username, String password) {
        validateUsernameAndPassword(username, password);
    }

    public void initiateLogin(String username, String password) {
        getUserService().login(username, password, new LoginObserver());
    }

    public class LoginObserver extends AuthenticationObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to login";
        }
    }
}
