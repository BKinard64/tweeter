package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;

public abstract class Service {
    protected void verifyAuthenticatedRequest(AuthenticatedRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an AuthToken");
        }

        // TODO: Once DynamoDB is set up, verify it is a VALID AuthToken
    }

    protected void verifyPagedRequest(PagedRequest request) {
        verifyAuthenticatedRequest(request);

        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
    }

    protected void verifyTargetUserRequest(TargetUserRequest request) {
        verifyAuthenticatedRequest(request);

        if (request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        }
    }
}
