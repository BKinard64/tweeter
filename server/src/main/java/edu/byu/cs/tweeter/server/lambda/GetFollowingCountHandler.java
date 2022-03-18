package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.TargetUserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowingCountHandler implements RequestHandler<TargetUserRequest, CountResponse> {
    @Override
    public CountResponse handleRequest(TargetUserRequest request, Context context) {
        FollowService service = new FollowService(new DynamoDAOFactory());
        return service.getFollowingCount(request);
    }
}
