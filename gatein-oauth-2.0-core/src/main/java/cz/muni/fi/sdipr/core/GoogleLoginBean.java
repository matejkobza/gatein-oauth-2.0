package cz.muni.fi.sdipr.core;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.exoplatform.portal.webui.util.Util;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.portlet.PortletSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link GoogleLoginBean} is the JSF backing bean for the application, holding
 * the input data to be re-displayed.
 */
@Named
@ApplicationScoped
public class GoogleLoginBean implements Serializable {

    private static final long serialVersionUID = -6239437588285327644L;
    //    private static Log log = LogFactory.getLog(GoogleLoginBean.class);
    private static String REDIRECT_URI = "http://localhost:8080" + Util.getPortalRequestContext().getInitialURI();
    private static final String CLIENT_ID = "220691316194.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "8iSht29f0P1XykVchC-nNk0c";
    private PortletSession session;
    private List<String> scopes;
    GoogleCredential credential = null;

    public GoogleLoginBean() {
        System.out.println("LoginBean");
        //context
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        session = (PortletSession) context.getSession(false);
        scopes = new ArrayList<String>();
    }

    //
    public void requestAccessToken(String redirectUri, Collection<String> scopes) {
        System.out.println("requestAccessToken");
        System.out.println(REDIRECT_URI);

        String authorizationUrl = new GoogleBrowserClientRequestUrl(CLIENT_ID, redirectUri, scopes).build();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(authorizationUrl);
        } catch (IOException ex) {
            ex.printStackTrace();
//            log.error(ex);
        }
    }

    //
//  public void requestAccessToken(List<String> scopes) {
//    this.scopes = scopes;
//    System.out.println(scopes);
//    requestAccessToken(REDIRECT_URI, scopes);
//  }
//
    public void requestAccessToken() {
        System.out.println(scopes);
        requestAccessToken(REDIRECT_URI, scopes);
    }

    // @todo this reimplement! should be less complex!
    public GoogleCredential getCredential() {
        System.out.println("getCredential");

        if (credential != null) return credential;

        String code;
        HttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        if (Util.getPortalRequestContext().getRequestParameter("code") != null) {

            code = Util.getPortalRequestContext().getRequestParameter("code");
            System.out.println("Code: " + code);

            System.out.println("Response from google");
            GoogleTokenResponse response;
            try {
                response = new GoogleAuthorizationCodeTokenRequest(transport,
                        jsonFactory, CLIENT_ID, CLIENT_SECRET, code, REDIRECT_URI)
                        .execute();
                session.setAttribute("accessToken", response.getAccessToken());
                session.setAttribute("refreshToken", response.getRefreshToken());
                session.setAttribute("expiresInSeconds", response.getExpiresInSeconds());
            } catch (Exception ex) {
                ex.printStackTrace();
//                log.error("getCredential", ex);
            }
        }

        System.out.println(session.getAttribute("accessToken"));
        if (session.getAttribute("accessToken") != null) {
            // Build a new GoogleCredential instance and return it.
            String accessToken = (String) session.getAttribute("accessToken");
            String refreshToken = (String) session.getAttribute("refreshToken");
            Long expiresInSeconds = (Long) session.getAttribute("expiresInSeconds");

            if (accessToken != null) {
                credential = new GoogleCredential.Builder()
                        .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                        .setJsonFactory(jsonFactory)
                        .setTransport(transport).build()
                        .setAccessToken(accessToken)
                        .setRefreshToken(refreshToken)
                        .setExpirationTimeMilliseconds(expiresInSeconds);
            }

            System.out.println(credential);

        }
        return credential;
    }

    //    public void setScopes(List<String> scopes) {
//        this.scopes = scopes;
//    }
//
//    public List<String> getScopes() {
//        return this.scopes;
//    }
//
    public void addScopes(List<String> scopes) {
        this.scopes.addAll(scopes);
    }

//    public void addScope(String scope) {
//        this.scopes.add(scope);
//    }
}
