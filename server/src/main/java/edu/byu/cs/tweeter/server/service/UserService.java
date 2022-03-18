package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService extends Service {

    private UserDAO userDAO;

    public UserService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public AuthenticationResponse login(LoginRequest request) {
        checkForUsernameAndPassword(request.getUsername(), request.getPassword());

        // TODO: Generates dummy data. Replace with real implementation.
        User user = getUserDAO().getUser(request.getUsername());
        // TODO: confirm password

        AuthToken authToken = getDummyAuthToken();
        getAuthTokenDAO().createAuthToken(authToken);
        return new AuthenticationResponse(user, authToken);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        checkForUsernameAndPassword(request.getUsername(), request.getPassword());
        if (request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if (request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing an image");
        }

        // TODO: Generates dummy data. Replace with real implementation.
        User user = getDummyUser();
        getUserDAO().createUser(user);

        AuthToken authToken = getDummyAuthToken();
        return new AuthenticationResponse(user, authToken);
    }

    public UserResponse getUser(TargetUserRequest request) {
        verifyTargetUserRequest(request);

        // TODO: call getUserDAO().getUser(request.getTargetUserAlias());
        User user = getFakeData().findUserByAlias(request.getTargetUserAlias());
        if (user == null) {
            throw new RuntimeException("[Bad Request] Cannot find User with alias: " + request.getTargetUserAlias());
        } else {
            return new UserResponse(user);
        }
    }

    public Response logout(AuthenticatedRequest request) {
        verifyAuthenticatedRequest(request);

        getAuthTokenDAO().deleteAuthToken(request.getAuthToken());
        return new Response(true);
    }

    private void checkForUsernameAndPassword(String username, String password) {
        if (username == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (password == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }
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

    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = getDaoFactory().getUserDAO();
        }
        return  userDAO;
    }
}
