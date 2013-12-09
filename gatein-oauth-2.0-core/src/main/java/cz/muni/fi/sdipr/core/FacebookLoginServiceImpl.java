package cz.muni.fi.sdipr.core;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.portal.application.PortalRequestContext;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 25.11.2013
 * Time: 23:52
 */
@Named(value = "facebookLoginBean")
@SessionScoped
public class FacebookLoginServiceImpl implements Serializable, FacebookLoginService {

    private static final long serialVersionUID = 8340747654024181065L;

    private static boolean debug = false;
    private static String APP_ID;
    private static String APP_SECRET;
    private static String REDIRECT_URI;
    private Facebook facebook;
    private String pointOfOrigin;
    private AccessToken accessToken;

    private Set<String> scopes = new HashSet<String>();

    public FacebookLoginServiceImpl() {
        System.out.println("@FacebookLoginService#Construct");
    }

    @PostConstruct
    public void init() throws FacebookOAuthLoginException {
        System.out.println("<-- PostConstruct");
        System.out.println("@FacebookLoginService#init");
        scopes.add(FacebookScope.EMAIL.getValue());

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("facebook.properties"));
            REDIRECT_URI = properties.getProperty("default.redirect.uri");
            APP_ID = properties.getProperty("default.app.id");
            APP_SECRET = properties.getProperty("default.app.secret");
            debug = Boolean.valueOf(properties.getProperty("default.debug"));
        } catch (IOException e) {
            throw new FacebookOAuthLoginException(e);
        }
    }

    /**
     * WEB Application
     *
     * @throws FacebookOAuthLoginException
     */
//    @Override
//    public void login() throws FacebookOAuthLoginException {
//        FacesContext ctx = FacesContext.getCurrentInstance();
//        HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
//        String oauthCode = req.getParameter("code");
//
//        try {
//            accessToken = facebook.getOAuthAccessToken(oauthCode);
//        } catch (FacebookException e) {
//            throw new FacebookOAuthLoginException("Unable to acquire access token.", e);
//        }
//
//        try {
//            ctx.getExternalContext().redirect(pointOfOrigin);
//        } catch (IOException e) {
//            throw new FacebookOAuthLoginException("Unable to return for point of origin.", e);
//        }
//
//    }
    @Override
    public void login() throws FacebookOAuthLoginException {
        if (accessToken != null) {
            return;
        }
        System.out.println("@FacebookLoginService#login");
        PortalRequestContext prc = PortalRequestContext.getCurrentInstance();
        HttpServletRequest request = prc.getRequest();

        String oauthCode = request.getParameter("code");

        try {
            accessToken = facebook.getOAuthAccessToken(oauthCode);
        } catch (FacebookException e) {
            throw new FacebookOAuthLoginException("Unable to acquire access token.", e);
        }

        try {
            prc.sendRedirect(pointOfOrigin);
        } catch (IOException e) {
            throw new FacebookOAuthLoginException("Unable to return for point of origin.", e);
        }
    }

    @Override
    public void doRedirect() throws FacebookOAuthLoginException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
        pointOfOrigin = req.getRequestURL().toString();

        ConfigurationBuilder cf = new ConfigurationBuilder();
        cf.setOAuthAppId(APP_ID);
        cf.setOAuthAppSecret(APP_SECRET);
        cf.setDebugEnabled(debug);
//        rem this in portal
        StringBuilder strScopes = new StringBuilder();
        for (String scope : scopes) {
            strScopes.append(scope).append(",");
        }
        cf.setOAuthPermissions(strScopes.substring(0, strScopes.lastIndexOf(",")));
        System.out.println(strScopes.substring(0, strScopes.lastIndexOf(",")));
//        unrem this in a portal environment
//        cf.setOAuthPermissions(StringUtils.join(scopes, ","));

        facebook = new FacebookFactory(cf.build()).getInstance();

        String url = facebook.getOAuthAuthorizationURL(REDIRECT_URI);
        try {
            ctx.getExternalContext().redirect(url);
        } catch (IOException ex) {
            throw new FacebookOAuthLoginException("Unable to redirect", ex);
        }
    }

    /**
     * WEB Application
     * @return
     */
//    @Override
//    public boolean isAuthenticated() {
//        return accessToken != null;
//    }

    @Override
    public boolean isAuthenticated() throws FacebookOAuthLoginException {
        PortalRequestContext prc = PortalRequestContext.getCurrentInstance();
        if (prc.getRequestParameter("code") != null) {
            this.login();
        }
        return accessToken != null;
    }

    @Override
    public void addScope(String scope) {
        this.scopes.add(scope);
    }

    @Override
    public void addScopes(List<String> scopes) {
        this.scopes.addAll(scopes);
    }

    @Override
    public Facebook getFacebook() {
        return facebook;
    }

    @Override
    public void logout() throws FacebookOAuthLoginException {
        accessToken = null;
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        try {
            ctx.getExternalContext().redirect(request.getRequestURL().toString());
        } catch (IOException e) {
            throw new FacebookOAuthLoginException("Unable to logout.", e);
        }

    }
}
