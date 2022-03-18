package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<PagedRequest<User>, FollowerResponse> {

    @Override
    public FollowerResponse handleRequest(PagedRequest<User> request, Context context) {
        FollowService followService = new FollowService(new DynamoDAOFactory());
        return followService.getFollowers(request);
    }
}
