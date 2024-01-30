package production.threads;

import production.model.Book;

import java.util.List;

public class GetBooksThread extends DatabaseThread implements Runnable{
    private List<Book> bookList;

    @Override
    public void run() {
        this.bookList = super.getAllBooksFromDB();
        System.out.println("Ažurirano");
    }

    public List<Book> getBooks() {
        return bookList;
    }
}