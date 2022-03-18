package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;

public interface FeedDAO {
    public void createFeed(Status status);
    public FeedResponse getFeed(PagedRequest<Status> feedRequest);
    public void deleteFeed(Status status);
}
