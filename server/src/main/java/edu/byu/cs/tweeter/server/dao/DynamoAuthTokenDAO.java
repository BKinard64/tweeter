package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.service.AuthTokenDAO;

public class DynamoAuthTokenDAO implements AuthTokenDAO {
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-1")
            .build();
    private final DynamoDB dynamoDB = new DynamoDB(client);

    private final Table authTokenTable = dynamoDB.getTable("authtoken");

    @Override
    public void createAuthToken(AuthToken authToken, String alias) throws DataAccessException {
        try {
            authTokenTable.putItem(new Item().withPrimaryKey("token_value", authToken.getToken())
                                    .withString("alias", alias)
                                    .withNumber("timestamp", new Date().getTime()));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public AuthToken getAuthToken(String token) throws DataAccessException {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("token_value", token);
        try {
            Item item = authTokenTable.getItem(spec);

            SimpleDateFormat format = new SimpleDateFormat("MMM d yyyy h:mm aaa");
            long timestamp = item.getLong("timestamp");
            Date date = new Date(timestamp);
            String datetime = format.format(date);

            return new AuthToken(token, datetime);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public String getUserAlias(String token) throws DataAccessException {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(new PrimaryKey("token_value", token));
        try {
            Item item = authTokenTable.getItem(spec);
            return item.getString("alias");
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void updateAuthTokenTimestamp(String token) throws DataAccessException {
        UpdateItemSpec spec = new UpdateItemSpec().withPrimaryKey("token_value", token)
                                    .withUpdateExpression("set #ts = :val")
                                    .withNameMap(new NameMap().with("#ts", "timestamp"))
                                    .withValueMap(new ValueMap().withNumber(":val", new Date().getTime()))
                                    .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            authTokenTable.updateItem(spec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("token_value", authToken.getToken()));
        try {
            authTokenTable.deleteItem(deleteItemSpec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void deleteExpiredAuthTokens() throws DataAccessException {
        ItemCollection<ScanOutcome> items;
        Iterator<Item> iterator;
        Item item;

        items = getExpiredAuthTokens();
        iterator = items.iterator();

        while (iterator.hasNext()) {
            item = iterator.next();
            String token = item.getString("token_value");
            DeleteItemSpec spec = new DeleteItemSpec()
                                        .withPrimaryKey(new PrimaryKey("token_value", token));
            try {
                authTokenTable.deleteItem(spec);
            } catch (Exception e) {
                throw new DataAccessException(e);
            }
        }
    }

    private ItemCollection<ScanOutcome> getExpiredAuthTokens() throws DataAccessException {
        long curTime = new Date().getTime();
        long expThreshold = curTime - 900000;

        ScanSpec spec = new ScanSpec()
                .withFilterExpression("#ts <= :val")
                .withNameMap(new NameMap().with("#ts", "timestamp"))
                .withValueMap(new ValueMap().withNumber(":val", expThreshold));

        try {
            return authTokenTable.scan(spec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
