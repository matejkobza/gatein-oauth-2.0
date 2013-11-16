package cz.muni.fi.sdipr.core;

import com.google.api.client.auth.oauth2.Credential;
import cz.muni.fi.sdipr.core.GoogleLoginService;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * This bean is default Access CDI Bean for the application. You will most probably use its functionality to login
 * in google account and providing access to services used in this application.
 *
 * !! Be advised, this bean is used only in JSF pages. Its purpose is not to be in any backing bean, but purely in JSF.
 *
 * Facade for GoogleLoginService.
 *
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 15.11.2013
 * Time: 17:46
 */
@Named(value = "googleLoginBean")
@ViewScoped
public class LoginBean {

    @Inject
    private GoogleLoginService googleLoginService;

    @PostConstruct
    public void init() {

    }

    /**
     * Login to google services
     * @throws GoogleOAuthLoginException
     */
    public void login() throws GoogleOAuthLoginException {
        googleLoginService.login();
    }

    /**
     * Redirect for access request
     * @throws GoogleOAuthLoginException
     */
    public void doRedirect() throws GoogleOAuthLoginException {
        googleLoginService.doRedirect();
    }

    /**
     * When you need check if user is authenticated with google in the application from your JSF page use this method
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return googleLoginService.isAuthenticated();
    }
}
