package cz.muni.fi.sdipr.core;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

//import org.exoplatform.portal.webui.util.Util;

@SessionScoped
public class GoogleLoginServiceImpl implements Serializable, GoogleLoginService {

    private static final long serialVersionUID = 1632587149151042714L;

    //google specific settings.
    //you can specify them at https://cloud.google.com/console
    private static String REDIRECT_URI;//enable this in portal context + Util.getPortalRequestContext().getInitialURI();
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;

    private Set<String> scopes = new HashSet<String>();
    private Credential credential;
//    private PortletSession session;

    public GoogleLoginServiceImpl() throws GoogleOAuthLoginException {
        System.out.println("@GoogleLoginService#Construct");
    }

    @PostConstruct
    public void init() throws GoogleOAuthLoginException {
        System.out.println("<-- PostConstruct");
        System.out.println("@GoogleLoginService#init");
        scopes.add("https://www.googleapis.com/auth/userinfo.profile");
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("google.properties"));
            REDIRECT_URI = properties.getProperty("default.redirect.uri");
            CLIENT_ID = properties.getProperty("default.client.id");
            CLIENT_SECRET = properties.getProperty("default.client.secret");
        } catch (Exception e) {
            throw new GoogleOAuthLoginException();
        }
    }

    @Override
    public void login() throws GoogleOAuthLoginException {
        System.out.println("@GoogleLoginService#login");
        FacesContext ctx = FacesContext.getCurrentInstance();
        // todo - this will need change in portlet
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        if (request != null) {
            StringBuffer fullUrlBuf = request.getRequestURL();
            if (request.getQueryString() != null) {
                fullUrlBuf.append('?').append(request.getQueryString());
                AuthorizationCodeResponseUrl authResponse =
                        new AuthorizationCodeResponseUrl(fullUrlBuf.toString());
                // check for user-denied error
                if (authResponse.getState().contains("authorization")) {
                    if (authResponse.getError() != null) {
                        throw new GoogleOAuthLoginException(authResponse.getError());
                    } else {
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
                                    .setScopes(scopes)
                                    .execute();
                            credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                                    .setFromTokenResponse(response);

                            // user is successfully authenticated redirect to the origin page
                            if (isAuthenticated()) {
                                String state = authResponse.getState();
                                String url = state.substring(state.indexOf("?") + 1);
                                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
                            }
                        } catch (IOException e) {
                            throw new GoogleOAuthLoginException("Unable to acquire token response", e);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doRedirect() throws GoogleOAuthLoginException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest)ctx.getExternalContext().getRequest();
        String pointOfOrigin = req.getRequestURL().toString();
        String url = new AuthorizationCodeRequestUrl("https://accounts.google.com/o/oauth2/auth", CLIENT_ID)
                .setState("authorization?" + pointOfOrigin).setRedirectUri(REDIRECT_URI)
                .setScopes(scopes)
                .build();
        try {
            ctx.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new GoogleOAuthLoginException("Unable to redirect", e);
        }
    }

    @Override
    public boolean isAuthenticated() {
        return credential != null;
    }

    @Override
    public void addScopes(List<String> scopes) {
        this.scopes.addAll(scopes);
    }

    @Override
    public void addScope(String scope) {
        this.scopes.add(scope);
    }

    @Override
    public Credential getCredential() {
        return credential;
    }
}
