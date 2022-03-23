package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.service.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class DynamoUserDAO implements UserDAO {
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                                                    .standard()
                                                    .withRegion("us-west-1")
                                                    .build();
    private final DynamoDB dynamoDB = new DynamoDB(client);

    private final Table userTable = dynamoDB.getTable("user");

    @Override
    public void createUser(User user, String password) throws DataAccessException {
        String salt = getSalt();
        String securePassword = getSecurePassword(password, salt);
        try {
            userTable.putItem(new Item().withPrimaryKey("alias", user.getAlias())
                                .withString("password", securePassword)
                                .withString("first_name", user.getFirstName())
                                .withString("last_name", user.getLastName())
                                .withString("image_url", user.getImageUrl())
                                .withString("salt", salt));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public User getUser(String username) throws DataAccessException {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("alias", username);
        try {
            Item item = userTable.getItem(spec);
            String firstName = item.getString("first_name");
            String lastName = item.getString("last_name");
            String imageUrlString = item.getString("image_url");
            return new User(firstName, lastName, username, imageUrlString);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void deleteUser(User user) {

    }

    private String getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return "FAILED TO GET SALT";
    }

    private String getSecurePassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH PASSWORD";
    }

    public User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    public FakeData getFakeData() {
        return new FakeData();
    }
}
