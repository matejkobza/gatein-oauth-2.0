package cz.muni.fi.sdipr.core.interceptor;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import cz.muni.fi.sdipr.core.GoogleLoginService;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 14.11.2013
 * Time: 19:57
 */
@Interceptor
@GoogleLogin
public class GoogleLoginInterceptor implements Serializable {

    private static final long serialVersionUID = 3400564842493501051L;

    @Inject
    private GoogleLoginService googleLoginService;

    /**
     * This interceptor is monitoring the login process and if permission denied because of insufficient scope is thrown
     * than it proceed to request a new accessToken with this scope
     * @param context InvocationContext from the application
     * @return
     * @throws Exception if any unexpected exception is thrown
     */
    @AroundInvoke
    public Object process(InvocationContext context) throws Exception {
        try {
            return context.proceed(); // run the context
        } catch (GoogleJsonResponseException ex) {
            // json exception something went wrong on google side (bad request, permission denied etc.)
            if (ex.getDetails().getCode() == 403) {
                try {
                    // redirect to google and request new accessToken for added scope
                    googleLoginService.doRedirect();
                } catch (GoogleOAuthLoginException e) {
                    e.printStackTrace();
                }
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return null;
    }
}
