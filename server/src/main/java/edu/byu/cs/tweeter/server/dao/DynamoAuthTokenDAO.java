package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import java.util.Date;

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
    public AuthToken getAuthToken(String token) {
        return null;
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
    public void deleteAuthToken(AuthToken authToken) throws DataAccessException {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("token_value", authToken.getToken()));
        try {
            authTokenTable.deleteItem(deleteItemSpec);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
