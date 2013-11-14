package cz.muni.fi.sdipr.web.interceptor;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import cz.muni.fi.sdipr.core.GoogleLoginBean;

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
@Log
public class LogInterceptor implements Serializable {

    @Inject
    private GoogleLoginBean googleLoginBean;

    @AroundInvoke
    public Object process(InvocationContext context) throws Exception {
        System.out.println("interceptor running");
        try {
            return context.proceed();
        } catch (GoogleJsonResponseException ex) {
            googleLoginBean.doRedirect();
        } catch (Exception ex) {
            throw ex;
        }
        return null;
    }
}
