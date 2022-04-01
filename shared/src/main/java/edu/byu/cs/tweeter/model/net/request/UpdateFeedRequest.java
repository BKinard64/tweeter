package edu.byu.cs.tweeter.model.net.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class UpdateFeedRequest {
    private List<String> followerAliases;
    private Status status;

    public UpdateFeedRequest(List<String> followerAliases, Status status) {
        this.followerAliases = followerAliases;
        this.status = status;
    }

    public List<String> getFollowerAliases() {
        return followerAliases;
    }

    public void setFollowerAliases(List<String> followerAliases) {
        this.followerAliases = followerAliases;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
