package cz.muni.fi.sdipr.web.facebook.controller;

import cz.muni.fi.sdipr.core.FacebookLoginService;
import cz.muni.fi.sdipr.core.FacebookScope;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.InboxResponseList;
import facebook4j.Message;

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
public class InboxController implements Serializable {

    private static final long serialVersionUID = 4118229345827162153L;
    private Facebook facebook;
    private InboxResponseList<Message> messages;

    @Inject
    private FacebookLoginService facebookLoginService;

    public void process() throws FacebookException {
        facebook = facebookLoginService.getFacebook();
        fetchMessages();
    }

    public void setScopes() {
        facebookLoginService.addScope(FacebookScope.EMAIL.getValue());
        facebookLoginService.addScope(FacebookScope.READ_STREAM.getValue());
        facebookLoginService.addScope(FacebookScope.READ_MAILBOX.getValue());
    }

    public void fetchMessages() throws FacebookException {
        messages = facebook.getInbox();
    }

    public InboxResponseList<Message> getMessages() {
        return messages;
    }

    public void setMessages(InboxResponseList<Message> messages) {
        this.messages = messages;
    }

}
