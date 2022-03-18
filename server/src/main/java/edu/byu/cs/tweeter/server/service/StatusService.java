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

    private FeedDAO feedDAO;
    private StoryDAO storyDAO;

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

        getStoryDAO().updateStory(request.getStatus());
        // TODO: Figure out how to update other user's feeds when current user posts
        getFeedDAO().updateFeed(request.getStatus());
        return new Response(true);
    }

    public FeedDAO getFeedDAO() {
        if (feedDAO == null) {
            feedDAO = getDaoFactory().getFeedDAO();
        }
        return feedDAO;
    }

    public StoryDAO getStoryDAO() {
        if (storyDAO == null) {
            storyDAO = getDaoFactory().getStoryDAO();
        }
        return storyDAO;
    }
}
