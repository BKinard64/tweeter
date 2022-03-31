package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticationResponse;
import edu.byu.cs.tweeter.server.lambda.RegisterHandler;

public class Test {
    public static void main(String[] args) {
        RegisterRequest request = new RegisterRequest("Bob", "Sanders", "@bs21", "secretPassword", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        RegisterHandler handler = new RegisterHandler();

        AuthenticationResponse response = handler.handleRequest(request, null);
    }
}
