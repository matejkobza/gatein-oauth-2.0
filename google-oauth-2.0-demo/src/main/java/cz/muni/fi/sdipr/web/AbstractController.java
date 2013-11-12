package cz.muni.fi.sdipr.web;

import cz.muni.fi.sdipr.core.GoogleLoginBean;
import cz.muni.fi.sdipr.core.GoogleOAuthLoginException;

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

    protected AbstractController() throws GoogleOAuthLoginException {
        Properties props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("application.properties"));
            applicationName = props.getProperty("application.name");

        } catch (IOException e) {
            throw new GoogleOAuthLoginException("Unable to initialize" ,e);
        }
    }

//    protected void doRedirect() {
//        googleLoginBean.doRedirect();
//    }
}
