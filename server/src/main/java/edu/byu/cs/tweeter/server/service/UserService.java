package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService extends Service {

    private UserDAO userDAO;

    public UserService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public AuthenticationResponse login(LoginRequest request) {
        checkForUsernameAndPassword(request.getUsername(), request.getPassword());

        User user = validateUser(request);
        AuthToken authToken = createAuthToken(request);

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

        validateUniqueUsername(request);

        User user = createUser(request);
        AuthToken authToken = createAuthToken(request);

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

    private User validateUser(LoginRequest request) {
        try {
            User user = getUserDAO().getUser(request.getUsername(), request.getPassword());
            return user;
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Invalid password")) {
                throw new RuntimeException("[Bad Request] Password is invalid.");
            } else {
                throw new RuntimeException("[Bad Request] User cannot be found.");
            }
        }
    }

    private void validateUniqueUsername(RegisterRequest request) {
        User user = null;
        try {
            user = getUserDAO().getUser(request.getUsername());
        } catch (DataAccessException e) {
            // Ignore
        }
        if (user != null) {
            throw new RuntimeException("[Bad Request] Username is already taken");
        }
    }

    private User createUser(RegisterRequest request) {
        String s3ImgUrlString = uploadImageToS3(request.getImage(), request.getUsername());
        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), s3ImgUrlString);
        try {
            getUserDAO().createUser(user, request.getPassword());
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to add User to database\n" + e.getMessage());
        }
        return user;
    }

    private AuthToken createAuthToken(LoginRequest request) {
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString());
        try {
            getAuthTokenDAO().createAuthToken(authToken, request.getUsername());
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to add AuthToken to database\n" + e.getMessage());
        }
        return authToken;
    }

    private String uploadImageToS3(String imgByteString, String alias) {
        byte[] imgByteArray = Base64.getDecoder().decode(imgByteString);
        ByteArrayInputStream bis = new ByteArrayInputStream(imgByteArray);

        AmazonS3 s3 = AmazonS3ClientBuilder
                        .standard()
                        .withRegion("us-west-1")
                        .build();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        metadata.setContentLength(imgByteArray.length);
        PutObjectRequest putObjectRequest = new PutObjectRequest("bk-byu-cs-340", alias.substring(1) + ".png",
                                                                bis, metadata)
                                                    .withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(putObjectRequest);

        return s3.getUrl("bk-byu-cs-340", alias.substring(1) + ".png").toString();
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
        return userDAO;
    }
}
