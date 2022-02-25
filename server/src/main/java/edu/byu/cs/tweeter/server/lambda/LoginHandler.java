package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {

    @Override
    public LoginResponse handleRequest(LoginRequest input, Context context) {
//        UserService userService = new UserService();
//        return userService.login(loginRequest);
        return null;
    }
}
