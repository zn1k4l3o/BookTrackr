package production.model;

import java.util.Date;

public class Movie extends LibraryItem{

    String title;
    String description;

    public static class Builder {

        String title;
        String description;
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

        public Builder withDescription(String description) {
            this.description = description;
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

        public Movie build() {
            Movie movie = new Movie();
            movie.title = title;
            movie.description = description;
            movie.id = id;
            movie.borrowedById = borrowedById;
            movie.reservedById = reservedById;
            movie.returnDate = returnDate;
            movie.canBeBorrowed = canBeBorrowed;
            return movie;
        }

    }

    private Movie() {
        super(Long.valueOf(-1), Long.valueOf(-1), Long.valueOf(-1), null, true);
    }
    public Movie(String title, String description, Long id, Long borrowedById, Long reservedById, Date returnDate, Boolean canBeBorrowed) {
        super(id, borrowedById, reservedById, returnDate, canBeBorrowed);
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setAuthor(String description) {
        this.description = description;
    }
}
