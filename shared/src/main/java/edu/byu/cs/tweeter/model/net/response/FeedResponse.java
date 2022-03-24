package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedResponse extends PagedResponse<Status> {
    public FeedResponse(List<Status> items, boolean hasMorePages) {
        super(items, hasMorePages);
    }
    public FeedResponse(String message) {
        super(false, message, null, false);
    }
}
