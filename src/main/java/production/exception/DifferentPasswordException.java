package production.exception;

public class DifferentPasswordException extends Exception {

    public DifferentPasswordException(String message) {
        super(message);
    }

    public DifferentPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public DifferentPasswordException(Throwable cause) {
        super(cause);
    }
}
