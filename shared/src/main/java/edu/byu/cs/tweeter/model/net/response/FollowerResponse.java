package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowerResponse extends PagedResponse<User> {
    public FollowerResponse(List<User> items, boolean hasMorePages) {
        super(items, hasMorePages);
    }
    public FollowerResponse(String message) {
        super(false, message, null, false);
    }
}
