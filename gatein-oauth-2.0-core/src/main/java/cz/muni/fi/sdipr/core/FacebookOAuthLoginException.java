package cz.muni.fi.sdipr.core;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 25.11.2013
 * Time: 23:53
 */
public class FacebookOAuthLoginException extends Exception {

    public FacebookOAuthLoginException() {
    }

    public FacebookOAuthLoginException(String message) {
        super(message);
    }

    public FacebookOAuthLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacebookOAuthLoginException(Throwable cause) {
        super(cause);
    }
}
