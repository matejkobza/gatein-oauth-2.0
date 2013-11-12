package cz.muni.fi.sdipr.core;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

//import org.exoplatform.portal.webui.util.Util;

/**
 * {@link GoogleLoginBean} is the JSF backing bean for the application, holding
 * the input data to be re-displayed.
 */
@Named
@ManagedBean(eager = true)
@ApplicationScoped
public class GoogleLoginBean implements Serializable {

    private static final long serialVersionUID = -6239437588285327644L;

    //google specific settings.
    //you can specify them at https://cloud.google.com/console
    private static String REDIRECT_URI;//enable this in portal context + Util.getPortalRequestContext().getInitialURI();
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;

//    private PortletSession session;
    private List<String> scopes = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile");
    private Credential credential;
//    GoogleCredential credential = null;

    public GoogleLoginBean() {
        System.out.println("GoogleLoginBean#Construct");
    }

    @PostConstruct
    public void init() throws GoogleOAuthLoginException {
        System.out.println("<-- PostConstruct");
        System.out.println("GoogleLoginBean#init");
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("google.properties"));
            REDIRECT_URI = properties.getProperty("default.redirect.uri");
            CLIENT_ID = properties.getProperty("default.client.id");
            CLIENT_SECRET = properties.getProperty("default.client.secret");
        } catch (Exception e) {
            throw new GoogleOAuthLoginException();
        }
//        login();
    }

//    @PreDestroy
//    public void destroy() {
//
//    }

    public void login() throws GoogleOAuthLoginException {
        System.out.println("@GoogleLoginBean#login");
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        StringBuffer fullUrlBuf = request.getRequestURL();
        if (request.getQueryString() != null) {
            fullUrlBuf.append('?').append(request.getQueryString());
            AuthorizationCodeResponseUrl authResponse =
                    new AuthorizationCodeResponseUrl(fullUrlBuf.toString());
            // check for user-denied error
            if (authResponse.getState().equals("authorization")) {
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
                    } catch (IOException e) {
                        throw new GoogleOAuthLoginException("Unable to acquire token response" ,e);
                    }
                }
            }
        }
    }

    public void doRedirect() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        String url = new AuthorizationCodeRequestUrl("https://accounts.google.com/o/oauth2/auth", CLIENT_ID)
                .setState("authorization").setRedirectUri(REDIRECT_URI)
                .setScopes(scopes)
                .build();
        try {
            ctx.getExternalContext().redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAuthenticated() {
        return false;
    }

    public void addScopes(List<String> scopes) {
        this.scopes.addAll(scopes);
    }

    public void addScope(String scope) {
        this.scopes.add(scope);
    }

    public void setRedirectUri(String uri) {
        REDIRECT_URI = uri;
    }
}
