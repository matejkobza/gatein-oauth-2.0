package cz.muni.fi.sdipr.core;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 7.11.2013
 * Time: 11:43
 */
public class FaceBookOAuthLoginException extends Exception {

    public FaceBookOAuthLoginException() {
    }

    public FaceBookOAuthLoginException(String message) {
        super(message);
    }

    public FaceBookOAuthLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaceBookOAuthLoginException(Throwable cause) {
        super(cause);
    }
}
