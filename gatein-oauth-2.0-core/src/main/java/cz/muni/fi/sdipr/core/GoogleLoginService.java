package cz.muni.fi.sdipr.core;

import com.google.api.client.auth.oauth2.Credential;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 15.11.2013
 * Time: 17:47
 */
/**
 * {@link GoogleLoginService} is a CDI backing bean for the application, holding
 * credentials for currently signed in user.
 */
public interface GoogleLoginService {

    /**
     * Login to google services
     * @throws GoogleOAuthLoginException
     */
    public void login() throws GoogleOAuthLoginException;

    /**
     * Redirect for access request
     * @throws GoogleOAuthLoginException
     */
    public void doRedirect() throws GoogleOAuthLoginException;

    /**
     * When you need check if user is authenticated with google in the application from your JSF page use this method
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated();

    public void addScope(String scope);

    public void addScopes(List<String> scopes);

    public Credential getCredential();

    void logout() throws IOException;
}
