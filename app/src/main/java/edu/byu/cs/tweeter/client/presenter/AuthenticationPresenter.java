package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.IAuthenticationObserver;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticationView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticationPresenter extends Presenter {

    private UserService userService;

    public AuthenticationPresenter(AuthenticationView view) {
        super(view);
        userService = new UserService();
    }

    public AuthenticationView getAuthView() {
        return (AuthenticationView) getView();
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

    public abstract class AuthenticationObserver extends Observer implements IAuthenticationObserver {

        @Override
        public void handleSuccess(User authenticatedUser, AuthToken authToken) {
            Cache.getInstance().setCurrUser(authenticatedUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            getAuthView().authenticationSuccessful(authenticatedUser);
        }
    }
}
