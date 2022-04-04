package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.server.service.FollowDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import edu.byu.cs.tweeter.util.Triple;

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
    public Pair<List<String>, Boolean> getFollowees(PagedRequest<User> request) throws DataAccessException {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        ItemCollection<QueryOutcome> items;
        Iterator<Item> iterator;
        Item item;

        List<String> followeeAliases = new ArrayList<>(request.getLimit());

        items = queryFollowsTable(request);

        iterator = items.iterator();
        while (iterator.hasNext()) {
            item = iterator.next();
            String userAlias = item.getString("followee_handle");
            followeeAliases.add(userAlias);
        }

        boolean hasMorePages = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null;

        return new Pair<>(followeeAliases, hasMorePages);
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
    public Pair<List<String>, Boolean> getFollowers(PagedRequest<User> request) throws DataAccessException {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        ItemCollection<QueryOutcome> items;
        Iterator<Item> iterator;
        Item item;

        List<String> followerAliases = new ArrayList<>(request.getLimit());

        items = queryFollowsIndex(request);

        iterator = items.iterator();
        while (iterator.hasNext()) {
            item = iterator.next();
            String userAlias = item.getString("follower_handle");
            followerAliases.add(userAlias);
        }

        boolean hasMorePages = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null;

        return new Pair<>(followerAliases, hasMorePages);
    }

    @Override
    public Triple<List<String>, Boolean, String> getFollowersAliases(String followeeAlias, int limit, String startKey) throws DataAccessException {
        assert limit > 0;
        assert followeeAlias != null;

        ItemCollection<QueryOutcome> items;
        Iterator<Item> iterator;
        Item item;

        List<String> followerAliases = new ArrayList<>(limit);

        items = queryFollowsIndex(followeeAlias, limit, startKey);

        iterator = items.iterator();
        while (iterator.hasNext()) {
            item = iterator.next();
            String userAlias = item.getString("follower_handle");
            followerAliases.add(userAlias);
        }

        boolean hasMorePages = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null;
        if (hasMorePages) {
            startKey = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey().get("follower_handle").getS();
        }

        return new Triple<>(followerAliases, hasMorePages, startKey);
    }

    @Override
    public List<String> getAllFollowers(String followeeAlias) throws DataAccessException {
        ItemCollection<QueryOutcome> items;
        Iterator<Item> iterator;
        Item item;

        List<String> followerAliases = new ArrayList<>();

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":handle", followeeAlias);

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("followee_handle = :handle").withValueMap(valueMap);

        Index followsIndex = followsTable.getIndex("follows-index");
        try {
            items = followsIndex.query(querySpec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }

        iterator = items.iterator();
        while (iterator.hasNext()) {
            item = iterator.next();
            String userAlias = item.getString("follower_handle");
            followerAliases.add(userAlias);
        }

        return followerAliases;
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

    private ItemCollection<QueryOutcome> queryFollowsTable(PagedRequest<User> request) throws DataAccessException {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":handle", request.getUserAlias());

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("follower_handle = :handle").withValueMap(valueMap)
                .withMaxResultSize(request.getLimit());
        if (request.getLastItem() != null) {
            querySpec.withExclusiveStartKey("follower_handle", request.getUserAlias(), "followee_handle", request.getLastItem().getAlias());
        }

        try {
            return followsTable.query(querySpec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private ItemCollection<QueryOutcome> queryFollowsIndex(PagedRequest<User> request) throws DataAccessException {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":handle", request.getUserAlias());

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("followee_handle = :handle").withValueMap(valueMap)
                .withMaxResultSize(request.getLimit());
        if (request.getLastItem() != null) {
            querySpec.withExclusiveStartKey("followee_handle", request.getUserAlias(), "follower_handle", request.getLastItem().getAlias());
        }

        Index followsIndex = followsTable.getIndex("follows-index");
        try {
            return followsIndex.query(querySpec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private ItemCollection<QueryOutcome> queryFollowsIndex(String followeeAlias, int limit, String startKey) throws DataAccessException {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":handle", followeeAlias);

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("followee_handle = :handle").withValueMap(valueMap)
                .withMaxResultSize(limit);
        if (startKey != null) {
            querySpec.withExclusiveStartKey("followee_handle", followeeAlias, "follower_handle", startKey);
        }

        Index followsIndex = followsTable.getIndex("follows-index");
        try {
            return followsIndex.query(querySpec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
