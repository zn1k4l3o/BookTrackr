package production.exception;

public class CheckOptional extends Exception {

    public CheckOptional(String message) {
        super(message);
    }

    public CheckOptional(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckOptional(Throwable cause) {
        super(cause);
    }
}
