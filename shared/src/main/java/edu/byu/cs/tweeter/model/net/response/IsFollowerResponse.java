package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {
    private boolean follower;

    public IsFollowerResponse(String message) {
        super(false, message);
    }

    public IsFollowerResponse(boolean follower) {
        super(true);
        this.follower = follower;
    }

    public void setFollower(boolean follower) {
        this.follower = follower;
    }

    public boolean isFollower() {
        return follower;
    }
}
