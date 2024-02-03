package production.exception;

public class CheckOptionalException extends RuntimeException{
    public CheckOptionalException() {
    }

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
