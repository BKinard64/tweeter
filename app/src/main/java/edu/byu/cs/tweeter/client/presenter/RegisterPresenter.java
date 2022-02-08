package edu.byu.cs.tweeter.client.presenter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.view.AuthenticationView;

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

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        getUserService().register(firstName, lastName, username, password, imageBytesBase64, new RegisterObserver((AuthenticationView) getView()));
    }

    public class RegisterObserver extends AuthenticationObserver {
        public RegisterObserver(AuthenticationView view) {
            super(view);
        }

        @Override
        protected String getMessagePrefix() {
            return "Failed to register";
        }
    }
}
