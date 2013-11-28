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
package cz.muni.fi.sdipr.web.google;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.Person;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;
import cz.muni.fi.sdipr.core.interceptor.GoogleLogin;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class ProfileController extends AbstractController {

    private static final long serialVersionUID = 8707726379300120095L;

    private Person profile;

    public ProfileController() throws GoogleOAuthLoginException {
        super();
        System.out.println("@ProfileController#Construct");
    }

    /**
     * Active and valid Credential from {@link cz.muni.fi.sdipr.core.GoogleLoginService} is required to run this method.
     */
    @GoogleLogin
    @Override
    public void process() throws IOException {
        Plus plus = new Plus.Builder(new NetHttpTransport(), new JacksonFactory(), googleLoginService.getCredential())
                .setApplicationName(applicationName)
                .build();
        profile = plus.people().get("me").execute();
    }

    public Person getProfile() {
        return profile;
    }

    public String getHome() {
        for (Person.PlacesLived pl : profile.getPlacesLived()) {
            if (pl.getPrimary()) {
                return pl.getValue();
            }
        }
        return null;
    }

    public List getOrganizations() {
        List<Person.Organizations> organizations = new ArrayList<Person.Organizations>();
        if (profile != null) {
            for (Person.Organizations organization : profile.getOrganizations()) {
                if (organization.getPrimary()) {
                    organizations.add(organization);
                }
            }
        }
        return organizations;
    }


    public void setProfile(Person profile) {
        this.profile = profile;
    }

    public void setScopes() {
        googleLoginService.addScope(PlusScopes.PLUS_LOGIN);
    }
}