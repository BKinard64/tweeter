package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.service.UserDAO;

public class DynamoUserDAO implements UserDAO {
    @Override
    public void createUser(User user) {

    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }
}
