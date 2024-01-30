package production.threads;

import production.model.Book;
import production.model.Library;
import production.model.Movie;

import java.util.List;

public class GetMoviesInLibraryThread extends DatabaseThread implements Runnable{

    private List<Movie> movieList;
    private final Library library;

    public GetMoviesInLibraryThread(Library library) {
        this.library = library;
    }
    @Override
    public void run() {
        this.movieList = super.getItemsInChosenLibraryFromDB(library, "Movie");
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

}