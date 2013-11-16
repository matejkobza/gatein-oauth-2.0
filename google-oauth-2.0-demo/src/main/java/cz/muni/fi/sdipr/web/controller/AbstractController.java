package cz.muni.fi.sdipr.web.controller;

import cz.muni.fi.sdipr.core.GoogleLoginService;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;
import cz.muni.fi.sdipr.core.interceptor.Login;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 7.11.2013
 * Time: 12:02
 */
public abstract class AbstractController implements Serializable {

    protected String applicationName;

    /**
     * inject {@link cz.muni.fi.sdipr.core.GoogleLoginService}
     */
    @Inject
    protected GoogleLoginService googleLoginService;


    protected AbstractController() throws GoogleOAuthLoginException {
        Properties props = new Properties();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("cz/muni/fi/sdipr/web/application.properties"));
            applicationName = props.getProperty("google.application.name");
        } catch (IOException e) {
            throw new GoogleOAuthLoginException("Unable to initialize", e);
        }
    }

    @Login
    public abstract void  process() throws Exception;

}
