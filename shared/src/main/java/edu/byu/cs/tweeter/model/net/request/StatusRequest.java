package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class StatusRequest extends AuthenticatedRequest {
    private Status status;

    public StatusRequest() {
        super();
    }

    public StatusRequest(AuthToken authToken, Status status) {
        super(authToken);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
