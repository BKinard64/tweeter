package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowingHandler implements RequestHandler<PagedRequest<User>, FollowingResponse> {
    @Override
    public FollowingResponse handleRequest(PagedRequest<User> request, Context context) {
        FollowService service = new FollowService(new DynamoDAOFactory());
        return service.getFollowees(request);
    }
}
