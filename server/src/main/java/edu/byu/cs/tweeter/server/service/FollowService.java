package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowService {
    public FollowingResponse getFollowees(FollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        return getFollowingDAO().getFollowees(request);
    }

    public FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }
}
