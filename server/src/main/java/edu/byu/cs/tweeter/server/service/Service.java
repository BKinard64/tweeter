package edu.byu.cs.tweeter.server.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.server.dao.DataAccessException;

public abstract class Service {
    private final DAOFactory daoFactory;
    private final AuthTokenDAO authTokenDAO;

    public Service(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        authTokenDAO = daoFactory.getAuthTokenDAO();
    }

    public DAOFactory getDaoFactory() {
        return daoFactory;
    }

    public AuthTokenDAO getAuthTokenDAO() {
        return authTokenDAO;
    }

    protected boolean verifyAuthenticatedRequest(AuthenticatedRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an AuthToken");
        }

        AuthToken authToken;
        try {
            authToken = getAuthTokenDAO().getAuthToken(request.getAuthToken().getToken());
        } catch (DataAccessException e) {
            throw new RuntimeException("[Bad Request] Invalid AuthToken");
        }

        if (isAuthTokenExpired(authToken)) {
            try {
                getAuthTokenDAO().deleteExpiredAuthTokens(authToken.getToken());
            } catch (DataAccessException e) {
                throw new RuntimeException("[Server Error] Failed to delete expired AuthTokens");
            }

            return false;
        }

        return true;
    }

    private boolean isAuthTokenExpired(AuthToken authToken) {
        long TIME_OUT_THRESHOLD = 900000;   // 15 minutes

        SimpleDateFormat format = new SimpleDateFormat("MMM d yyyy h:mm aaa");
        long authTime;
        try {
            authTime = format.parse(authToken.getDatetime()).getTime();
        } catch (Exception e) {
            throw new RuntimeException("[Server Error] Unable to parse date/time");
        }
        long curTime = new Date().getTime();

        return Math.abs(curTime - authTime) >= TIME_OUT_THRESHOLD;
    }

    protected boolean verifyPagedRequest(PagedRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        return verifyAuthenticatedRequest(request);
    }

    protected boolean verifyTargetUserRequest(TargetUserRequest request) {
        if (request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a target user alias");
        }

        return verifyAuthenticatedRequest(request);
    }

    protected void updateAuthTokenActivity(AuthToken authToken) {
        try {
            getAuthTokenDAO().updateAuthTokenTimestamp(authToken.getToken());
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to update AuthToken");
        }
    }
}
