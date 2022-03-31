package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class StoryResponse extends PagedResponse<Status> {
    public StoryResponse(List<Status> items, boolean hasMorePages) {
        super(items, hasMorePages);
    }
    public StoryResponse(String message) {
        super(false, message, null, false);
    }
}
