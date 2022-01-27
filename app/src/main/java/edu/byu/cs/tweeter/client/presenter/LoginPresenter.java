package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    public interface View {
        void displayMessage(String message);
        void loginSuccessful(User user);
    }

    private View view;
    private UserService userService;

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void initiateLogin(String username, String password) {
        userService.login(username, password, new LoginObserver());
    }

    public class LoginObserver implements UserService.LoginObserver {
        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.loginSuccessful(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
