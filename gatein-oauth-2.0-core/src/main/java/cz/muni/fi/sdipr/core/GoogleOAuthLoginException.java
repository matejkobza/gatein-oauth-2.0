package cz.muni.fi.sdipr.core;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 7.11.2013
 * Time: 11:42
 */
public class GoogleOAuthLoginException extends Exception {

    public GoogleOAuthLoginException() {
    }

    public GoogleOAuthLoginException(String message) {
        super(message);
    }

    public GoogleOAuthLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleOAuthLoginException(Throwable cause) {
        super(cause);
    }

}
