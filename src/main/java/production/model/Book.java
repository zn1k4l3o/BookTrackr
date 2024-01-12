package production.model;

import java.util.Date;

public class Book extends LibraryItem{

    String title;
    String author;
    String genre;

    public static class Builder {

        String title;
        String author;
        String genre;
        Long id;
        Long borrowedById;
        Long reservedById;
        Date returnDate;
        Boolean canBeBorrowed;

        public Builder(String title) {
            this.title = title;
            this.canBeBorrowed = true;
            this.borrowedById = Long.valueOf(-1);
            this.reservedById = Long.valueOf(-1);
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }
        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withBorrowedById(Long borrowedById) {
            this.borrowedById = borrowedById;
            return this;
        }

        public Builder withReservedById(Long reservedById) {
            this.reservedById = reservedById;
            return this;
        }

        public Builder withDate(Date returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public Builder withCanBeBorrowed(Boolean canBeBorrowed) {
            this.canBeBorrowed = canBeBorrowed;
            return this;
        }

        public Book build() {
            Book book = new Book();
            book.title = title;
            book.author = author;
            book.genre = genre;
            book.id = id;
            book.borrowedById = borrowedById;
            book.reservedById = reservedById;
            book.returnDate = returnDate;
            book.canBeBorrowed = canBeBorrowed;
            return book;
        }

    }

    private Book() {
        super(Long.valueOf(-1), Long.valueOf(-1), Long.valueOf(-1), null, true);
    }
    public Book(String title, String author, String genre, Long id, Long borrowedById, Long reservedById, Date returnDate, Boolean canBeBorrowed) {
        super(id, borrowedById, reservedById, returnDate, canBeBorrowed);
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
