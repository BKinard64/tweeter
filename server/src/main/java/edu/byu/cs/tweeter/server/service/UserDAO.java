package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {
    public void createUser(User user);
    public User getUser(String username);
    public void deleteUser(User user);
}
