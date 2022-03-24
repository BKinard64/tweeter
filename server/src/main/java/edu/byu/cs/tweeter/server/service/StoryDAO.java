package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.util.Pair;

public interface StoryDAO {
    public void addStatus(Status status) throws DataAccessException;
    public Pair<List<Status>, Boolean> getStory(PagedRequest<Status> storyRequest, User targetUser) throws DataAccessException;
    public void deleteStory(Status status);
}
