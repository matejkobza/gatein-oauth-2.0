package cz.muni.fi.sdipr.web.controller;

import cz.muni.fi.sdipr.core.FacebookLoginService;
import cz.muni.fi.sdipr.core.FacebookScope;
import facebook4j.*;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 28.11.2013
 * Time: 16:06
 */
@Named
@SessionScoped
public class ProfileController implements Serializable {

    private static final long serialVersionUID = 4118229345827162153L;
    private Facebook facebook;
    private String statusMessage;
    private ResponseList<Post> posts;

    @Inject
    private FacebookLoginService facebookLoginService;

    public void process() throws FacebookException {
        facebook = facebookLoginService.getFacebook();

//        User user = facebook.getMe();

//        InboxResponseList<Message> messages = facebook.getInbox();

        fetchFeed();
    }

    public void setScopes() {
        facebookLoginService.addScope(FacebookScope.EMAIL.getValue());
        facebookLoginService.addScope(FacebookScope.READ_STREAM.getValue());
        facebookLoginService.addScope(FacebookScope.READ_MAILBOX.getValue());
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void updateStatus() throws FacebookException {
        facebook.postStatusMessage(statusMessage);
        statusMessage = null;
    }

    public void fetchFeed() throws FacebookException {
        posts = facebook.getHome();
    }

    public ResponseList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ResponseList<Post> posts) {
        this.posts = posts;
    }
}
