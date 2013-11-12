/**
 * http://javadoc.google-oauth-java-client.googlecode.com/hg/1.17.0-rc/com/google/api/client/auth/oauth2/package-summary.html
 *
 * These are the typical steps of the web server flow based on an authorization code, as specified in Authorization Code Grant:

 Redirect the end user in the browser to the authorization page using @AuthorizationCodeRequestUrl to grant your application access to the end user's protected data.
 Process the authorization response using @AuthorizationCodeResponseUrl to parse the authorization code.
 Request an access token and possibly a refresh token using @AuthorizationCodeTokenRequest.
 Access protected resources using @Credential. Expired access tokens will automatically be refreshed using the refresh token (if applicable).

 */
package cz.muni.fi.sdipr.web;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Person;
import cz.muni.fi.sdipr.core.GoogleLoginBean;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@ViewScoped
@ManagedBean
public class ProfileController implements Serializable {

    private boolean authenticated = false;
    private Credential credential;
    private Person profile;
    private String applicationName;

    /**
     *inject {@link GoogleLoginBean}
     */
    @Inject
    private GoogleLoginBean googleLoginBean;


    public ProfileController() throws GoogleOAuthLoginException {
        System.out.println("@ProfileController#Construct");
//        super();
        Properties props = new Properties();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            applicationName = props.getProperty("google.application.name");
        } catch (IOException e) {
            throw new GoogleOAuthLoginException("Unable to initialize", e);
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("@ProfileController#PostConstruct");
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        googleLoginBean.setRedirectUri(request.getRequestURL().toString());
        googleLoginBean.addScope(PlusScopes.PLUS_LOGIN);
    }

    public boolean isAuthenticated() {
        if (!authenticated) {
//            fetchProfile();
        }
        return authenticated;
    }

    public void fetchProfile() {
        Plus plus = new Plus.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
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

    public void doRedirect() {
        System.out.println("redirect to google page");
    }

}