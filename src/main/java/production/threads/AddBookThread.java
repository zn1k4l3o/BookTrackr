package production.threads;

public class AddBookThread extends DatabaseThread implements Runnable {

    String bookTitle;
    String bookGenre;
    String bookPublisher;
    String bookAuthor;
    Float rating;

    public AddBookThread(String bookTitle, String bookGenre, String bookPublisher, String bookAuthor, Float rating) {
        this.bookTitle = bookTitle;
        this.bookGenre = bookGenre;
        this.bookPublisher = bookPublisher;
        this.bookAuthor = bookAuthor;
        this.rating = rating;
    }
    @Override
    public void run() {
        super.addBookToDB(bookTitle, bookGenre, bookPublisher, bookAuthor, rating);
    }
}
