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
@Login
public class LoginInterceptor implements Serializable {

    private static final long serialVersionUID = 3400564842493501051L;

    @Inject
    private GoogleLoginService googleLoginService;

    @AroundInvoke
    public Object process(InvocationContext context) {
        try {
            return context.proceed();
            // todo this exception we must examine!!! to be the 403 permission denied
        } catch (GoogleJsonResponseException ex) {
            try {
                googleLoginService.doRedirect();
            } catch (GoogleOAuthLoginException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
