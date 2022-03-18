package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.service.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class DynamoUserDAO implements UserDAO {
    @Override
    public void createUser(User user) {

    }

    @Override
    public User getUser(String username) {
        return getDummyUser();
    }

    @Override
    public void deleteUser(User user) {

    }

    public User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    public FakeData getFakeData() {
        return new FakeData();
    }
}
