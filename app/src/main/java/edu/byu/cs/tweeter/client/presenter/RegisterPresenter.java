package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {

    public interface View {
        void displayMessage(String message);
        void registerSuccessful(User user);
    }

    private View view;
    private UserService userService;

    public RegisterPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }


    public void initiateRegister(String firstName, String lastName, String username, String password, String imageBytesBase64) {
        userService.register(firstName, lastName, username, password, imageBytesBase64, new RegisterObserver());
    }

    public class RegisterObserver implements UserService.RegisterObserver {
        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.registerSuccessful(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }
    }
}
