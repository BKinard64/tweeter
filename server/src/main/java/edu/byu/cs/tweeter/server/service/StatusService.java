package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StatusService {
    public FeedResponse getFeed(FeedRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        return getFeedDAO().getFeed(request);
    }

    public StoryResponse getStory(StoryRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        return getStoryDAO().getStory(request);
    }

    public Response postStatus(StatusRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }

        return new Response(true);
    }

    public FeedDAO getFeedDAO() {
        return new FeedDAO();
    }

    public StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
}
