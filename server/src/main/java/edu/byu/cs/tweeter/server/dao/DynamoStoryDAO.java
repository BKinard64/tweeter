package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.service.StoryDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import edu.byu.cs.tweeter.util.Triple;

/**
 * A DAO for accessing 'story' data from the database.
 */
public class DynamoStoryDAO extends PagedDAO<Status> implements StoryDAO {
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private final DynamoDB dynamoDB = new DynamoDB(client);

    private final Table storyTable = dynamoDB.getTable("story");

    /**
     * Add a status to the Story Table
     *
     * @param status
     */
    @Override
    public void addStatus(Status status) throws DataAccessException {
        SimpleDateFormat format = new SimpleDateFormat("MMM d yyyy h:mm aaa");
        try {
            Date date = format.parse(status.getDate());
            storyTable.putItem(new Item()
                                    .withPrimaryKey("sender_alias", status.getUser().getAlias(), "timestamp", date.getTime())
                                    .withString("post", status.getPost()));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Gets the statuses from the database that the user specified in the request has in their story. Uses
     * information in the request object to limit the number of statuses returned and to return the
     * next set of statuses after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose story is to be returned and any
     *                other information required to satisfy the request.
     * @param targetUser
     * @return the statuses.
     */
    @Override
    public Pair<List<Status>, Boolean> getStory(PagedRequest<Status> request, User targetUser) throws DataAccessException {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        ItemCollection<QueryOutcome> items;
        Iterator<Item> iterator;
        Item item;

        List<Status> feedPage = new ArrayList<>(request.getLimit());
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        items = queryStoryTable(request);

        iterator = items.iterator();
        while (iterator.hasNext()) {
            item = iterator.next();

            long timestamp = item.getLong("timestamp");
            Date date = new Date(timestamp);
            String datetime = statusFormat.format(date);
            String post = item.getString("post");

            Status status = new Status(post, targetUser, datetime, null, null);
            feedPage.add(status);
        }

        boolean hasMorePages = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null;

        return new Pair<>(feedPage, hasMorePages);
    }

    /**
     * Delete a story from the Story Table
     *
     * @param status
     */
    @Override
    public void deleteStory(Status status) {

    }

    private ItemCollection<QueryOutcome> queryStoryTable(PagedRequest<Status> request) throws DataAccessException {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":handle", request.getUserAlias());

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("sender_alias = :handle").withValueMap(valueMap)
                .withMaxResultSize(request.getLimit())
                .withScanIndexForward(false);
        if (request.getLastItem() != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMM d yyyy h:mm aaa");
            Date date;
            try {
                date = format.parse(request.getLastItem().getDate());
            } catch (Exception e) {
                throw new RuntimeException("[Server Error] Unable to parse date/time");
            }
            querySpec.withExclusiveStartKey("sender_alias", request.getUserAlias(), "timestamp", date.getTime());
        }

        try {
            return storyTable.query(querySpec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}

