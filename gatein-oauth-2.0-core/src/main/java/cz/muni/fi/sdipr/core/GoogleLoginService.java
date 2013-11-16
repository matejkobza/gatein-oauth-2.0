package cz.muni.fi.sdipr.core;

import com.google.api.client.auth.oauth2.Credential;

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

    public void login() throws GoogleOAuthLoginException;

    public void doRedirect() throws GoogleOAuthLoginException;

    public boolean isAuthenticated();

    public void addScope(String scope);

    public void addScopes(List<String> scopes);

    public Credential getCredential();

}
