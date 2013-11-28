package cz.muni.fi.sdipr.core;

import facebook4j.Facebook;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 25.11.2013
 * Time: 23:51
 */
public interface FacebookLoginService {

    public void doRedirect() throws FacebookOAuthLoginException;

    public boolean isAuthenticated();

    public void addScope(String permission);

    public void addScopes(List<String> permissions);

    public Facebook getFacebook();

    public void logout() throws FacebookOAuthLoginException;

    public void login() throws FacebookOAuthLoginException;
}
