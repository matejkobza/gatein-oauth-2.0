/**
 * http://javadoc.google-oauth-java-client.googlecode.com/hg/1.17.0-rc/com/google/api/client/auth/oauth2/package-summary.html
 *
 * These are the typical steps of the web server flow based on an authorization code, as specified in Authorization Code Grant:
 *
 * Redirect the end user in the browser to the authorization page using @AuthorizationCodeRequestUrl to grant your application access to the end user's protected data.
 * Process the authorization response using @AuthorizationCodeResponseUrl to parse the authorization code.
 * Request an access token and possibly a refresh token using @AuthorizationCodeTokenRequest.
 * Access protected resources using @Credential. Expired access tokens will automatically be refreshed using the refresh token (if applicable).
 *
 */
package cz.muni.fi.sdipr.web.controller;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Person;
import cz.muni.fi.sdipr.core.GoogleLoginBean;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;

@ViewScoped
@ManagedBean
public class ProfileController extends AbstractController {

    private Person profile;



    public ProfileController() throws GoogleOAuthLoginException {
        super();
        System.out.println("@ProfileController#Construct");
    }

    /**
     * Active and valid Credential from {@link GoogleLoginBean} is required to run this method.
     */
    @Override
    public void process() {
        Plus plus = new Plus.Builder(new NetHttpTransport(), new JacksonFactory(), googleLoginBean.getCredential())
                .setApplicationName(applicationName)
                .build();
        try {
            profile = plus.people().get("me").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Person getProfile() {
        return profile;
    }

    public void setProfile(Person profile) {
        this.profile = profile;
    }

    public void setScopes() {
        googleLoginBean.addScope(PlusScopes.PLUS_LOGIN);
    }

}