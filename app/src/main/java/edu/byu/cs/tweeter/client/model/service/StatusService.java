package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public interface GetStoryObserver extends PagedObserver<Status> {}

    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetStoryObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken, user, pageSize, lastStatus,
                                                    new PagedTaskHandler<Status>(getStoryObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    public interface GetFeedObserver extends PagedObserver<Status> {}

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetFeedObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken, user, pageSize, lastStatus,
                                                    new PagedTaskHandler<Status>(getFeedObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    public interface PostStatusObserver extends SimpleNotificationObserver {}

    public void postStatus(AuthToken currUserAuthToken, Status newStatus, PostStatusObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken, newStatus,
                                                        new SimpleNotificationHandler(postStatusObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }

}
