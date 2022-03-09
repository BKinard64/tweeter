package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler implements RequestHandler<PagedRequest<Status>, StoryResponse> {
    @Override
    public StoryResponse handleRequest(PagedRequest<Status> request, Context context) {
        StatusService service = new StatusService();
        return service.getStory(request);
    }
}
