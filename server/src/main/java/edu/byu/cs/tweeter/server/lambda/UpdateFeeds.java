package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.net.request.UpdateFeedRequest;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AsyncMsgService;
import edu.byu.cs.tweeter.server.service.SQSService;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeeds implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        StatusService service = new StatusService(new DynamoDAOFactory());
        AsyncMsgService asyncMsgService = new SQSService();

        for (SQSEvent.SQSMessage msg : sqsEvent.getRecords()) {
            Gson gson = new Gson();
            UpdateFeedRequest request = gson.fromJson(msg.getBody(), UpdateFeedRequest.class);

            service.updateFeeds(request.getFollowerAliases(), request.getStatus());

            String queueURL = "https://sqs.us-west-1.amazonaws.com/546208180313/UpdateFeedQueue";
            asyncMsgService.deleteMessage(queueURL, msg);
        }
        return null;
    }
}
