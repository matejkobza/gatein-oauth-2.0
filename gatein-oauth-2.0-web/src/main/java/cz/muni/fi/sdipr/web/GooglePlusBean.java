package cz.muni.fi.sdipr.web;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.plus.model.Person;
import cz.muni.fi.sdipr.core.GoogleLoginBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author matejkobza
 */
@ManagedBean
@SessionScoped
public class GooglePlusBean implements Serializable, OAuthAbstractBean {

    private static final long serialVersionUID = 4317642879328909566L;
    private static Log log = LogFactory.getLog(GooglePlusBean.class);
    Person profile = null;

    private GoogleCredential credential;

    @Inject
    private GoogleLoginBean loginBean;

    public GooglePlusBean() throws Exception {
        System.out.println("@PostConstruct");
        System.out.println("GooglePlusBean");
        if (loginBean == null) {
            throw new Exception("CDI is not working");
        } else {
//            loginBean.addScopes(Arrays.asList(PlusScopes.PLUS_LOGIN, PlusScopes.PLUS_ME));
        }
    }

    public void loadProfile() {
        if (credential == null) {
//            credential = loginBean.getCredential();
        }

        if (credential == null) {
//            loginBean.requestAccessToken();
        }

        if (credential != null) {
//            HttpTransport httpTransport = new NetHttpTransport();
//            JacksonFactory jsonFactory = new JacksonFactory();

//            Plus plus = new Plus.Builder(httpTransport, jsonFactory, credential)
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
//            try {
//                try {
//                    profile = plus.people().get("me").execute();
//                } catch (GoogleJsonResponseException ex) {
//                    log.info("User is not authenticated to Google Plus", ex);
//                }
//            } catch (IOException ex) {
//                log.error("loadProfile", ex);
//            }
        }
    }

    public Person getProfile() {
        return profile;
    }

    @Override
    public Boolean getIsAuthenticated() {
        if (profile == null) {
            this.loadProfile();
        }
        return profile != null;
    }
}
