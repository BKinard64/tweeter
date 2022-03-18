package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;

public interface StoryDAO {
    public void createStory(Status status);
    public StoryResponse getStory(PagedRequest<Status> storyRequest);
    public void deleteStory(Status status);
}
