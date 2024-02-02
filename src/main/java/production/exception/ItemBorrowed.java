package production.exception;

public class ItemBorrowed extends RuntimeException{
    public ItemBorrowed() {
    }

    public ItemBorrowed(String message) {
        super(message);
    }

    public ItemBorrowed(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemBorrowed(Throwable cause) {
        super(cause);
    }
}
