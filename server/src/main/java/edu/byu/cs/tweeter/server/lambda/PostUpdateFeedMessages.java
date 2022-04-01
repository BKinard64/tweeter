package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.UpdateFeedRequest;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AsyncMsgService;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.SQSService;
import edu.byu.cs.tweeter.util.Triple;

public class PostUpdateFeedMessages implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        int PAGE_LIMIT = 10;

        FollowService service = new FollowService(new DynamoDAOFactory());
        AsyncMsgService asyncMsgService = new SQSService();

        for (SQSEvent.SQSMessage msg : sqsEvent.getRecords()) {
            Gson gson = new Gson();
            Status status = gson.fromJson(msg.getBody(), Status.class);

            boolean hasMorePages = true;
            String startKey = null;

            while (hasMorePages) {
                Triple<List<String>, Boolean, String> triple = service.getFollowersAliases(status.getUser().getAlias(), PAGE_LIMIT, startKey);

                List<String> followerAliases = triple.getFirst();
                hasMorePages = triple.getSecond();
                startKey = triple.getThird();

                UpdateFeedRequest ufRequest = new UpdateFeedRequest(followerAliases, status);
                String messageBody = gson.toJson(ufRequest);
                String sendQueueURL = "https://sqs.us-west-1.amazonaws.com/546208180313/UpdateFeedQueue";

                asyncMsgService.sendMessage(messageBody, sendQueueURL);
            }
            String receiveQueueURL = "https://sqs.us-west-1.amazonaws.com/546208180313/PostStatusQueue";
            asyncMsgService.deleteMessage(receiveQueueURL, msg);
        }
        return null;
    }


}
