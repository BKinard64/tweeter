package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.service.FollowDAO;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * A DAO for accessing "follow relationship" data from the database.
 */
public class DynamoFollowDAO extends PagedDAO<User> implements FollowDAO {
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private final DynamoDB dynamoDB = new DynamoDB(client);

    private final Table followsTable = dynamoDB.getTable("follows");

    /**
     * Add a followee to the Follows Table
     * @param curUser
     * @param followee
     */
    @Override
    public void createFollowee(User curUser, User followee) throws DataAccessException {
        try {
            followsTable.putItem(new Item()
                                .withPrimaryKey("follower_handle", curUser.getAlias(), "followee_handle", followee.getAlias())
                                .withString("follower_name", curUser.getName())
                                .withString("followee_name", followee.getName()));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Add a follower to the Follows Table
     *
     * @param user
     */
    @Override
    public void createFollower(User user) {

    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    @Override
    public FollowingResponse getFollowees(PagedRequest<User> request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<User> allFollowees = getDummyUsers();
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getItemsStartingIndex(request.getLastItem(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    /**
     * Gets the users from the database that the user specified in the request is followed by. Uses
     * information in the request object to limit the number of followers returned and to return the
     * next set of followers after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followers are to be returned and any
     *                other information required to satisfy the request.
     * @return the followers.
     */
    @Override
    public FollowerResponse getFollowers(PagedRequest<User> request) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<User> allFollowers = getDummyUsers();
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers != null) {
                int followersIndex = getItemsStartingIndex(request.getLastItem(), allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
        }

        return new FollowerResponse(responseFollowers, hasMorePages);
    }

    /**
     * Determines if the first user specified is following the second user specified
     *  @param followerAlias
     * @param followeeAlias
     * @return*/
    @Override
    public boolean queryFollowRelationship(String followerAlias, String followeeAlias) throws DataAccessException {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("follower_handle", followerAlias, "followee_handle", followeeAlias);
        try {
            Item item = followsTable.getItem(spec);
            if (item == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Delete a followee from the Follows Table
     *  @param curUser
     * @param followee
     */
    @Override
    public void deleteFollowee(User curUser, User followee) throws DataAccessException {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey("follower_handle", curUser.getAlias(), "followee_handle", followee.getAlias());
        try {
            followsTable.deleteItem(deleteItemSpec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Delete a follower from the Follows Table
     *
     * @param user
     */
    @Override
    public void deleteFollower(User user) {

    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyUsers() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }
}
