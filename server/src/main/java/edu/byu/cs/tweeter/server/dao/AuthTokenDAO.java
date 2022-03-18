package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenDAO {
    public void createAuthToken(AuthToken authToken);
    public AuthToken getAuthToken(String token);
    public void deleteAuthToken(AuthToken authToken);
}
