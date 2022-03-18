package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;

public class StatusService extends Service {
    public StatusService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public FeedResponse getFeed(PagedRequest<Status> request) {
        verifyPagedRequest(request);

        return getFeedDAO().getFeed(request);
    }

    public StoryResponse getStory(PagedRequest<Status> request) {
        verifyPagedRequest(request);

        return getStoryDAO().getStory(request);
    }

    public Response postStatus(StatusRequest request) {
        verifyAuthenticatedRequest(request);

        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }

        return new Response(true);
    }

    public FeedDAO getFeedDAO() {
        return getDaoFactory().getFeedDAO();
    }

    public StoryDAO getStoryDAO() {
        return getDaoFactory().getStoryDAO();
    }
}
