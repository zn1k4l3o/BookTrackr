package production.utility;

import production.model.Book;
import production.model.Movie;

public abstract class ItemMemory {

    private static Book rememberedBook;
    private static Movie rememberedMovie;

    public static Book getRememberedBook() {
        return rememberedBook;
    }

    public static void setRememberedBook(Book newRememberedBook) {
        rememberedBook = newRememberedBook;
    }

    public static Movie getRememberedMovie() {
        return rememberedMovie;
    }

    public static void setRememberedMovie(Movie newRememberedMovie) {
        rememberedMovie = newRememberedMovie;
    }

}
