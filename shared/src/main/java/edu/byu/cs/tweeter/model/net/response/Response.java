package edu.byu.cs.tweeter.model.net.response;

import java.io.Serializable;

public class Response implements Serializable {
    private final boolean success;
    private final String message;

    public Response(boolean success) {
        this(success, null);
    }

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
