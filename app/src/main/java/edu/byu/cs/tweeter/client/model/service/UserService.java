package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.handler.AuthenticationHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.IAuthenticationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IGetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService extends Service {

    public void getUser(AuthToken currUserAuthToken, String userAlias, IGetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias, new GetUserHandler(getUserObserver));
        executeTask(getUserTask);
    }

    public void login(String username, String password, IAuthenticationObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new AuthenticationHandler(loginObserver));
        executeTask(loginTask);
    }

    public void register(String firstName, String lastName, String username, String password, String imageBytesBase64, IAuthenticationObserver registerObserver) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, username, password,
                                                    imageBytesBase64, new AuthenticationHandler(registerObserver));

        executeTask(registerTask);
    }

    public void logout(AuthToken currUserAuthToken, SimpleNotificationObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new SimpleNotificationHandler(logoutObserver));
        executeTask(logoutTask);
    }
}
