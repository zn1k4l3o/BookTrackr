package production.utility;

import production.model.Book;
import production.model.Movie;
import production.model.User;
import production.model.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static production.utility.DatabaseUtils.getAllUsersFromDatabase;
import static production.utility.DatabaseUtils.getItemsInChosenLibrary;

public abstract class DataCompare {

    public static DataWrapper compareDataWrapperToCurrent(DataWrapper dataWrapper) {
        DataWrapper newDataWrapper = new DataWrapper(dataWrapper.getLibrary());
        if (dataWrapper.getHasLibraryUsers()) {
            List<User> allUserList = getAllUsersFromDatabase();
            allUserList = allUserList.stream().filter(u -> u.getLibraryName().equals(dataWrapper.getLibrary().getName())).collect(Collectors.toList());
            List<User> userList = new ArrayList<>();
            List<Worker> workerList = new ArrayList<>();
            for (User user : allUserList) {
                if (user instanceof Worker) workerList.add((Worker) user);
                else userList.add(user);
            }
            newDataWrapper.setHasLibraryUsers(true);
            newDataWrapper.setUserList(userList);
            newDataWrapper.setWorkerList(workerList);

            for (int i = 0; i < userList.size(); i++) {
                String username = userList.get(i).getUsername();
                boolean found = false;
                for (User pastUser : dataWrapper.getUserList()) {
                    if (pastUser.getUsername().equals(username)) {
                        found = true;
                    }
                }
                if (found == false) {
                    newDataWrapper.getUserList().get(i).addNewToUsername();
                }
            }
            for (int i = 0; i < workerList.size(); i++) {
                String username = workerList.get(i).getUsername();
                boolean found = false;
                for (Worker pastWorker : dataWrapper.getWorkerList()) {
                    if (pastWorker.getUsername().equals(username)) {
                        found = true;
                    }
                }
                if (found == false) {
                    newDataWrapper.getWorkerList().get(i).addNewToUsername();
                }
            }
        }
        if (dataWrapper.getHasLibraryItems()) {
            List<Book> bookList = getItemsInChosenLibrary(dataWrapper.getLibrary(), "Book");
            List<Movie> movieList = getItemsInChosenLibrary(dataWrapper.getLibrary(), "Movie");
            newDataWrapper.setHasLibraryItems(true);
            newDataWrapper.setBookList(bookList);
            newDataWrapper.setMovieList(movieList);

            for (int i = 0; i < bookList.size(); i++) {
                String title = bookList.get(i).getTitle();
                boolean found = false;
                for (Book pastBook : dataWrapper.getBookList()) {
                    if (pastBook.getTitle().equals(title)) {
                        found = true;
                    }
                }
                if (found == false) {
                    newDataWrapper.getBookList().get(i).addNewToTitle();
                }
            }
            for (int i = 0; i < movieList.size(); i++) {
                String title = movieList.get(i).getTitle();
                boolean found = false;
                for (Movie pastMovie : dataWrapper.getMovieList()) {
                    if (pastMovie.getTitle().equals(title)) {
                        found = true;
                    }
                }
                if (found == false) {
                    newDataWrapper.getMovieList().get(i).addNewToTitle();
                }
            }
        }
        return newDataWrapper;
    }
}
