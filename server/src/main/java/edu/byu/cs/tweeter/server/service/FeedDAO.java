package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.util.Triple;

public interface FeedDAO {
    public void addStatus(String receiverAlias, Status status) throws DataAccessException;
    public Triple<List<String>, List<Status>, Boolean> getFeed(PagedRequest<Status> feedRequest) throws DataAccessException;
    public void deleteFeed(Status status);
}
