package cz.muni.fi.sdipr.core.interceptor;

import cz.muni.fi.sdipr.core.FacebookLoginService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 26.11.2013
 * Time: 12:12
 */
public class FacebookLoginInterceptor implements Serializable {

    private static final long serialVersionUID = -1395275720271101546L;

    @Inject
    private FacebookLoginService facebookLoginService;

    @AroundInvoke
    public Object process(InvocationContext context) throws Exception {
        return context.proceed();
    }
}
