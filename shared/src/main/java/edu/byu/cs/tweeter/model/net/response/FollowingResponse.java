package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowingResponse extends PagedResponse<User> {
    public FollowingResponse(List<User> items, boolean hasMorePages) {
        super(items, hasMorePages);
    }
    public FollowingResponse(String message) {
        super(false, message, null, false);
    }
}
