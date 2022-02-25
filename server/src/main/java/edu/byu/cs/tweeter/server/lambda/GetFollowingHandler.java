package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

public class GetFollowingHandler implements RequestHandler<FollowingRequest, FollowingResponse> {
    @Override
    public FollowingResponse handleRequest(FollowingRequest input, Context context) {
//        FollowService service = new FollowService();
//        return service.getFollowees(request);
        return null;
    }
}
