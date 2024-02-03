package production.exception;

public class CheckOptionalException extends Exception {

    public CheckOptionalException(String message) {
        super(message);
    }

    public CheckOptionalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckOptionalException(Throwable cause) {
        super(cause);
    }
}
