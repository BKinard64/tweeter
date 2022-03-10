package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler implements RequestHandler<PagedRequest<Status>, FeedResponse> {
    @Override
    public FeedResponse handleRequest(PagedRequest<Status> request, Context context) {
        StatusService service = new StatusService();
        return service.getFeed(request);
    }
}
