package production.utility;

import production.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataWrapper implements Serializable {

    private Library library;
    private List<Book> bookList;
    private List<Movie> movieList;
    private List<User> userList;
    private List<Worker> workerList;

    public DataWrapper(Library library) {
        this.library = library;
        bookList = new ArrayList<>();
        movieList = new ArrayList<>();
        userList = new ArrayList<>();
        workerList = new ArrayList<>();
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(List<Worker> workerList) {
        this.workerList = workerList;
    }
}
