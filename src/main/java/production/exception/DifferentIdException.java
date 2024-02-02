package production.exception;

public class DifferentIdException extends RuntimeException {
    public DifferentIdException() {
    }

    public DifferentIdException(String message) {
        super(message);
    }

    public DifferentIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public DifferentIdException(Throwable cause) {
        super(cause);
    }
}
