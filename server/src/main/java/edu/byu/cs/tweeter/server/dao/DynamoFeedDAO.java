package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.service.FeedDAO;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * A DAO for accessing 'feed' data from the database.
 */
public class DynamoFeedDAO extends PagedDAO<Status> implements FeedDAO {

    /**
     * Create a feed in the Feed Table
     *
     * @param status
     */
    @Override
    public void createFeed(Status status) {

    }

    /**
     * Add a Status to the Feed Table
     *
     * @param status
     */
    @Override
    public void updateFeed(Status status) {

    }

    /**
     * Gets the statuses from the database that the user specified in the request has in their feed. Uses
     * information in the request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose feed is to be returned and any
     *                other information required to satisfy the request.
     * @return the statuses.
     */
    @Override
    public FeedResponse getFeed(PagedRequest<Status> request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> allStatuses = getDummyStatuses();
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusesIndex = getItemsStartingIndex(request.getLastItem(), allStatuses);

                for(int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < request.getLimit(); statusesIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusesIndex));
                }

                hasMorePages = statusesIndex < allStatuses.size();
            }
        }

        return new FeedResponse(responseStatuses, hasMorePages);
    }

    /**
     * Gets the count of statuses from the database that the user specified has in their feed. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param user the User whose count of how many statuses is desired.
     * @return said count.
     */
    public Integer getStatusCount(User user) {
        // TODO: uses the dummy data.  Replace with a real implementation.
        assert user != null;
        return getDummyStatuses().size();
    }

    /**
     * Delete a feed from the Feed Table
     *
     * @param status
     */
    @Override
    public void deleteFeed(Status status) {

    }

    /**
     * Returns the list of dummy status data. This is written as a separate method to allow
     * mocking of the statuses.
     *
     * @return the statuses.
     */
    List<Status> getDummyStatuses() {
        return getFakeData().getFakeStatuses();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy statuses.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }
}
