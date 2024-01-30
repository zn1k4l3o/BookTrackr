package production.threads;

import production.model.Book;
import production.model.Library;
import production.utility.DatabaseUtils;

import java.util.List;

public class GetBooksInLibraryThread extends DatabaseThread implements Runnable{

    private List<Book> bookList;
    private final Library library;

    public GetBooksInLibraryThread(Library library) {
        this.library = library;
    }
    @Override
    public void run() {
        this.bookList = super.getItemsInChosenLibraryFromDB(library, "Book");
    }

    public List<Book> getBookList() {
        return bookList;
    }

}
