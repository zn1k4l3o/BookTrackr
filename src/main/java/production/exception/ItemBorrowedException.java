package production.exception;

public class ItemBorrowedException extends RuntimeException{
    public ItemBorrowedException() {
    }

    public ItemBorrowedException(String message) {
        super(message);
    }

    public ItemBorrowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemBorrowedException(Throwable cause) {
        super(cause);
    }
}
