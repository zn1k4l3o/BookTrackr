package production.model;

import java.util.Date;

public non-sealed class Movie extends LibraryItem{

    String director;
    Integer releaseYear;
    String filmRatingSystem;
    String description;

    public static class Builder {

        String title;
        String description;
        Long id;
        Float rating;
        String director;
        Integer releaseYear;
        String filmRatingSystem;

        public Builder(String title) {
            this.title = title;

        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withRating(Float rating) {
            this.rating = rating;
            return this;
        }

        public Builder withDirector(String director) {
            this.director = director;
            return this;
        }

        public Builder withReleaseYear(Integer releaseYear) {
            this.releaseYear = releaseYear;
            return this;
        }

        public Builder withFilmRatingSystem(String filmRatingSystem) {
            this.filmRatingSystem = filmRatingSystem;
            return this;
        }

        public Movie build() {
            Movie movie = new Movie();
            movie.title = title;
            movie.id = id;
            movie.description = description;
            movie.rating = rating;
            movie.filmRatingSystem = filmRatingSystem;
            movie.director = director;
            movie.releaseYear = releaseYear;
            return movie;
        }

    }
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getFilmRatingSystem() {
        return filmRatingSystem;
    }

    public void setFilmRatingSystem(String filmRatingSystem) {
        this.filmRatingSystem = filmRatingSystem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Movie(Long id, String title, Float rating, String director, Integer releaseYear, String filmRatingSystem) {
        super(id, title, rating);
        this.director = director;
        this.releaseYear = releaseYear;
        this.filmRatingSystem = filmRatingSystem;
    }

    public Movie() {
        super(0L, "", 0.0f);
    }

    public Movie(String state) {
        super(0L, state, 0.0f);
        this.director = state;
        this.releaseYear = 0;
        this.filmRatingSystem = state;
    }

    /*
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

     */
}
