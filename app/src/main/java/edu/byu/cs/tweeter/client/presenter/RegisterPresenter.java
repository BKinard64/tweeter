package edu.byu.cs.tweeter.client.presenter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.presenter.view.AuthenticationView;

public class RegisterPresenter extends AuthenticationPresenter {

    public RegisterPresenter(AuthenticationView view) {
        super(view);
    }

    public void validateRegistration(String firstName, String lastName, String username, String password, boolean isImageMissing) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }

        validateUsernameAndPassword(username, password);

        if (isImageMissing) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public void initiateRegister(String firstName, String lastName, String username, String password, ByteArrayOutputStream bos) {
        byte[] imageBytes = bos.toByteArray();
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        getUserService().register(firstName, lastName, username, password, imageBytesBase64, new RegisterObserver());
    }

    public class RegisterObserver extends AuthenticationObserver {
        @Override
        protected String getMessagePrefix() {
            return "Failed to register";
        }
    }
}
