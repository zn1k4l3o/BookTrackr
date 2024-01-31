package production.model;

import java.util.Date;

public non-sealed class Book extends LibraryItem {

    Integer pageCount;
    String genre;
    String author;
    String publisher;

    public static class Builder {

        Integer pageCount;
        String genre;
        String author;
        String publisher;
        Float rating;
        String title;
        Long id;

        public Builder(String title) {
            this.title = title;
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

        public Builder withPageCount(Integer pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder withPublisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder withRating(Float rating) {
            this.rating = rating;
            return this;
        }

        public Book build() {
            Book book = new Book();
            book.title = title;
            book.author = author;
            book.genre = genre;
            book.id = id;
            book.rating = rating;
            book.pageCount = pageCount;
            book.publisher = publisher;
            return book;
        }

    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Book(Long id, String title, Float rating, String publisher, Integer pageCount, String genre, String author) {
        super(id, title, rating);
        this.publisher = publisher;
        this.pageCount = pageCount;
        this.genre = genre;
        this.author = author;
    }

    public Book() {
        super(0L, "", 0.0f);
    }

    public Book(String state) {
        super(0L, state, 0.0f);
        this.publisher = state;
        this.pageCount = 0;
        this.genre = state;
        this.author = state;
    }

    @Override
    public String toString() {
        return "Book - " + this.title;
    }

}
