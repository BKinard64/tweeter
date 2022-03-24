package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.StatusRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.DataAccessException;
import edu.byu.cs.tweeter.util.Pair;
import edu.byu.cs.tweeter.util.Triple;

public class StatusService extends Service {

    private FeedDAO feedDAO;
    private StoryDAO storyDAO;
    private FollowDAO followDAO;
    private UserDAO userDAO;

    public StatusService(DAOFactory daoFactory) {
        super(daoFactory);
    }

    public FeedResponse getFeed(PagedRequest<Status> request) {
        verifyPagedRequest(request);

        Triple<List<String>, List<Status>, Boolean> triple;
        try {
            triple = getFeedDAO().getFeed(request);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to get User's feed");
        }
        List<String> posterAliases = triple.getFirst();
        List<Status> feedPage = triple.getSecond();
        boolean hasMorePages = triple.getThird();

        addUserInfo(posterAliases, feedPage);
        parsePost(feedPage);

        return new FeedResponse(feedPage, hasMorePages);
    }

    public StoryResponse getStory(PagedRequest<Status> request) {
        verifyPagedRequest(request);

        User targetUser = getTargetUser(request.getUserAlias());

        Pair<List<Status>, Boolean> pair = null;
        try {
            pair = getStoryDAO().getStory(request, targetUser);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unalbe to get User's story");
        }
        List<Status> feedPage = pair.getFirst();
        boolean hasMorePages = pair.getSecond();

        parsePost(feedPage);

        return new StoryResponse(feedPage, hasMorePages);
    }

    public Response postStatus(StatusRequest request) {
        verifyAuthenticatedRequest(request);

        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }

        try {
            getStoryDAO().addStatus(request.getStatus());
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to post status to user's story");
        }

        List<String> followerAliases = getFollowerAliases(request.getStatus().getUser().getAlias());
        for (String followerAlias : followerAliases) {
            try {
                getFeedDAO().addStatus(followerAlias, request.getStatus());
            } catch (DataAccessException e) {
                throw new RuntimeException("[Server Error] Unable to post status to followers' feeds");
            }
        }

        return new Response(true);
    }

    private void addUserInfo(List<String> posterAliases, List<Status> feedPage) {
        assert posterAliases.size() == feedPage.size();

        for (int i = 0; i < posterAliases.size(); i++) {
            String alias = posterAliases.get(i);
            if (alias != null) {
                try {
                    User user = getUserDAO().getUser(alias);
                    feedPage.get(i).setUser(user);
                } catch (DataAccessException e) {
                    throw new RuntimeException("[Server Error] Unable to get poster info");
                }
            }
        }
    }

    private User getTargetUser(String userAlias) {
        try {
            return getUserDAO().getUser(userAlias);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to get user info");
        }
    }

    private void parsePost(List<Status> feedPage) {
        for (Status status : feedPage) {
            List<String> urls = parseURLs(status.getPost());
            List<String> mentions = parseMentions(status.getPost());

            status.setUrls(urls);
            status.setMentions(mentions);
        }
    }

    private List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    private int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    private List<String> getFollowerAliases(String posterAlias) {
        try {
            return getFollowDAO().getAllFollowers(posterAlias);
        } catch (DataAccessException e) {
            throw new RuntimeException("[Server Error] Unable to get followers aliases");
        }
    }

    public FeedDAO getFeedDAO() {
        if (feedDAO == null) {
            feedDAO = getDaoFactory().getFeedDAO();
        }
        return feedDAO;
    }

    public StoryDAO getStoryDAO() {
        if (storyDAO == null) {
            storyDAO = getDaoFactory().getStoryDAO();
        }
        return storyDAO;
    }

    public FollowDAO getFollowDAO() {
        if (followDAO == null) {
            followDAO = getDaoFactory().getFollowDAO();
        }
        return followDAO;
    }

    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = getDaoFactory().getUserDAO();
        }
        return userDAO;
    }
}
