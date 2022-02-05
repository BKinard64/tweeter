package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.handler.AuthenticationHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService {

    public void getUser(AuthToken currUserAuthToken, String userAlias, GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias, new GetUserHandler(getUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void login(String username, String password, AuthenticationObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new AuthenticationHandler(loginObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    public void register(String firstName, String lastName, String username, String password, String imageBytesBase64, AuthenticationObserver registerObserver) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, username, password,
                                                    imageBytesBase64, new AuthenticationHandler(registerObserver));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    public void logout(AuthToken currUserAuthToken, SimpleNotificationObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new SimpleNotificationHandler(logoutObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }
}
