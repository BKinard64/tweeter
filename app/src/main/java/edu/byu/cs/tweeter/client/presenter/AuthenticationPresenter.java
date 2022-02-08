package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.AuthenticationView;

public class AuthenticationPresenter extends Presenter {

    private UserService userService;

    public AuthenticationPresenter(AuthenticationView view) {
        super(view);
        userService = new UserService();
    }

    public UserService getUserService() {
        return userService;
    }

    protected void validateUsernameAndPassword(String username, String password) {
        if (username.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (username.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (username.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

}
