package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class TargetUserRequest extends AuthenticatedRequest {
    private String targetUserAlias;

    public TargetUserRequest() {
        super();
    }

    public TargetUserRequest(AuthToken authToken, String targetUserAlias) {
        super(authToken);
        this.targetUserAlias = targetUserAlias;
    }

    public String getTargetUserAlias() {
        return targetUserAlias;
    }

    public void setTargetUserAlias(String targetUserAlias) {
        this.targetUserAlias = targetUserAlias;
    }
}
