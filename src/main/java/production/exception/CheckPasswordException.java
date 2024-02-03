package production.exception;

public class CheckPasswordException extends Exception {

    public CheckPasswordException(String message) {
        super(message);
    }

    public CheckPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckPasswordException(Throwable cause) {
        super(cause);
    }
}
