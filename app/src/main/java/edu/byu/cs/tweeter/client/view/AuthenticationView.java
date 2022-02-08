package edu.byu.cs.tweeter.client.view;

import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticationView extends View {
    void authenticationSuccessful(User user);
}
