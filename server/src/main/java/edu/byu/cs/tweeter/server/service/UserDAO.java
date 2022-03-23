package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DataAccessException;

public interface UserDAO {
    public void createUser(User user, String password) throws DataAccessException;
    public User getUser(String username) throws DataAccessException;
    public User getUser(String username, String password) throws DataAccessException;
    public void deleteUser(User user);
}
