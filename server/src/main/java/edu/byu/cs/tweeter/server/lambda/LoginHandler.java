package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class LoginHandler implements RequestHandler<LoginRequest, AuthenticationResponse> {

    @Override
    public AuthenticationResponse handleRequest(LoginRequest request, Context context) {
        UserService userService = new UserService();
        return userService.login(request);
    }
}
