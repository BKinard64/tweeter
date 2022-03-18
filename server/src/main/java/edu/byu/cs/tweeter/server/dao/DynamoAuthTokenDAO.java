package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.service.AuthTokenDAO;

public class DynamoAuthTokenDAO implements AuthTokenDAO {
    @Override
    public void createAuthToken(AuthToken authToken) {

    }

    @Override
    public AuthToken getAuthToken(String token) {
        return null;
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) {

    }
}
