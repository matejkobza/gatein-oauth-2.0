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

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

@ManagedBean
@ViewScoped
public class GoogleLoginBean implements Serializable {

    private static final String CLIENT_ID = "832209082909.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "NVM0u1pJNGvT_HlATU2WfRWw";
    private static final String REDIRECT_URI = "http://localhost:8080/google-oauth-2-demo/faces/index.xhtml";
    private static final String APPLICATION_NAME = "OAuth-2-demo";


    private static final long serialVersionUID = 1L;
    private boolean authenticated = false;
    private FacesContext ctx;
    private Credential credential;
    private Person profile;

    @PostConstruct
    public void init() {
        System.out.println("@GoogleLoginBean#PostConstruct");
        ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        StringBuffer fullUrlBuf = request.getRequestURL();
        if (request.getQueryString() != null) {
            fullUrlBuf.append('?').append(request.getQueryString());
            AuthorizationCodeResponseUrl authResponse =
                    new AuthorizationCodeResponseUrl(fullUrlBuf.toString());
            // check for user-denied error
            if (authResponse.getState().equals("authorization")) {
                if (authResponse.getError() != null) {
                    ctx.addMessage("Error", new FacesMessage("Access not granted!"));
                } else {
                    authenticated = true;
                    String authorizationCode = authResponse.getCode();
                    // request access token using authResponse.getCode()...
                    try {
                        TokenResponse response = new AuthorizationCodeTokenRequest(new NetHttpTransport(),
                                new JacksonFactory(),
                                new GenericUrl("https://accounts.google.com/o/oauth2/token"),
                                authorizationCode)
                                .setRedirectUri(REDIRECT_URI)
                                .setGrantType("authorization_code")
                                .setClientAuthentication(new ClientParametersAuthentication(CLIENT_ID, CLIENT_SECRET))
                                .setScopes(Arrays.asList(PlusScopes.PLUS_LOGIN))
                                .execute();
                        credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                                .setFromTokenResponse(response);

                        fetchProfile();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


//        if (authenticated) {
        // load credential
//            new TokenResponse();
//        }
    }


    public boolean isAuthenticated() {
        if (!authenticated) {
            init();
        }
        return authenticated;
    }

    public void doRedirect() {
        ctx = FacesContext.getCurrentInstance();
        String url = new AuthorizationCodeRequestUrl("https://accounts.google.com/o/oauth2/auth", CLIENT_ID)
                .setState("authorization").setRedirectUri(REDIRECT_URI)
                .setScopes(Arrays.asList(PlusScopes.PLUS_LOGIN))
                .build();
        try {
            ctx.getExternalContext().redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchProfile() {
        Plus plus = new Plus.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        try {
            PeopleFeed feed = plus.people().list("me", "visible").execute();
            profile = feed.getItems().get(0);
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
}