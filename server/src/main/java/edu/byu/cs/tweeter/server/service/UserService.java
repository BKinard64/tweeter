package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {
    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with real implementation.
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
    }

    public User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    public AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    public FakeData getFakeData() {
        return new FakeData();
    }
}
