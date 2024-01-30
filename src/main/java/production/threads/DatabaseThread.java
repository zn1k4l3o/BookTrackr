package production.threads;

import production.model.*;
import production.utility.DatabaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DatabaseThread {

    public boolean activeConnectionWithDatabase = false;

    public synchronized Connection connectToDatabase() throws SQLException, IOException {
        Connection veza;
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        System.out.println("Usli smo");
        veza = DatabaseUtils.connectToDatabase();
        activeConnectionWithDatabase = false;
        notifyAll();
        return veza;
    }

    public synchronized List<User> getAllUsersFromDB(){
        List<User> userList;
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        userList = DatabaseUtils.getAllUsersFromDatabase();
        System.out.println("Users: " + userList);
        activeConnectionWithDatabase = false;

        notifyAll();
        return userList;
    }

    public synchronized Optional<User> getUserByIdFromDB(Long userId){
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        Optional<User> userOptional = DatabaseUtils.getUserByIdFromDatabase(userId);
        System.out.println("User by id: " + userOptional);
        activeConnectionWithDatabase = false;

        notifyAll();
        return userOptional;
    }

    public synchronized void addUserToDB(String username, String hashedPassword, String name, String lastName, Long libraryId, Boolean isWorker){
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        DatabaseUtils.addUserToDatabase(username, hashedPassword, name, lastName, libraryId, isWorker);
        System.out.println("user dodan");
        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized Optional<Library> getLibraryByIdFromDB(Long libraryId){
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        Optional<Library> libraryOptional = DatabaseUtils.getLibraryByIdFromDatabase(libraryId);
        System.out.println("library by id: " + libraryOptional);
        activeConnectionWithDatabase = false;

        notifyAll();
        return libraryOptional;
    }

    public synchronized Optional<Library> getLibraryByNameFromDB(String name) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        Optional<Library> libraryOptional = DatabaseUtils.getLibraryByNameFromDatabase(name);
        System.out.println("library by name: " + libraryOptional);
        activeConnectionWithDatabase = false;

        notifyAll();
        return libraryOptional;
    }

    public synchronized List<Library> getAllLibrariesFromDB() {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        List<Library> libraryList = DatabaseUtils.getAllLibrariesFromDatabase();
        System.out.println("libraries: " + libraryList);
        activeConnectionWithDatabase = false;

        notifyAll();
        return libraryList;
    }

    public synchronized void addLibraryToDatabase(String name, String webAddress, String hashedPassword) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        DatabaseUtils.addLibraryToDatabase(name, webAddress, hashedPassword);
        System.out.println("Dodan library, valjda");
        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized  <T extends LibraryItem> List<T> getItemsInChosenLibraryFromDB(Library library, String category) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        List<T> itemList = DatabaseUtils.getItemsInChosenLibrary(library, category);
        System.out.println("povuceni itemi, valjda");
        activeConnectionWithDatabase = false;

        notifyAll();
        return itemList;
    }

    public synchronized void addBookToDB(String title, String genre, String publisher, String author, Float rating) {
        while (activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        DatabaseUtils.addBookToDatabase(title, genre, publisher, author, rating);
        System.out.println("Dodan book, valjda");
        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized List<Book> getAllBooksFromDB() {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        List<Book> bookList = DatabaseUtils.getAllBooksFromDatabase();
        System.out.println("books: " + bookList);
        activeConnectionWithDatabase = false;

        notifyAll();
        return bookList;
    }

    public synchronized String getItemCategoryFromDB(Long itemId) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        String category = DatabaseUtils.getItemCategory(itemId);
        System.out.println("categorija: " + category);
        activeConnectionWithDatabase = false;

        notifyAll();
        return category;
    }

    public synchronized Optional<Book> getBookByIdFromDB(Long itemId) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        Optional<Book> bookOptional = DatabaseUtils.getBookByIdFromDatabase(itemId);
        System.out.println("book: " + bookOptional);
        activeConnectionWithDatabase = false;

        notifyAll();
        return bookOptional;
    }

    public synchronized Optional<Movie> getMovieByIdFromDB(Long itemId) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        Optional<Movie> movieOptional = DatabaseUtils.getMovieByIdFromDatabase(itemId);
        System.out.println("movie: " + movieOptional);
        activeConnectionWithDatabase = false;

        notifyAll();
        return movieOptional;
    }

    public synchronized void addMovieToDB(String title, String description, String director, String filmRatingSystem, Float rating, Integer releaseYear) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        DatabaseUtils.addMovieToDatabase(title, description, director, filmRatingSystem, rating, releaseYear);
        System.out.println("dodan movie: ");
        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized List<Movie> getAllMoviesFromDB() {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        List<Movie> movieList = DatabaseUtils.getAllMoviesFromDatabase();
        System.out.println("all movies: " + movieList);
        activeConnectionWithDatabase = false;

        notifyAll();
        return movieList;
    }

    public synchronized Optional<BorrowInfo> getBorrowingInfoForItemIdFromDB(Long itemId) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        Optional<BorrowInfo> borrowInfoOptional = DatabaseUtils.getBorrowingInfoForItemIdFromDatabase(itemId);
        System.out.println("borrow info: " + borrowInfoOptional);
        activeConnectionWithDatabase = false;

        notifyAll();
        return borrowInfoOptional;
    }

    public synchronized Optional<ReservedInfo> getReservedInfoForItemIdFromDB(Long itemId) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        Optional<ReservedInfo> reservedInfoOptional = DatabaseUtils.getReservedInfoForItemIdFromDatabase(itemId);
        System.out.println("reserved info: " + reservedInfoOptional);
        activeConnectionWithDatabase = false;

        notifyAll();
        return reservedInfoOptional;
    }

    public synchronized List<BorrowInfo> getBorrowingInfoForUserIdFromDB(Long userId) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        List<BorrowInfo> borrowInfoList = DatabaseUtils.getBorrowingInfoForUserIdFromDatabase(userId);
        System.out.println("all borrowedInfo: " + borrowInfoList);
        activeConnectionWithDatabase = false;

        notifyAll();
        return borrowInfoList;
    }

    public synchronized void addReservedItemToDB(ReservedInfo reservedInfo, Boolean isHistory) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        DatabaseUtils.addReservedItemToDatabase(reservedInfo, isHistory);
        System.out.println("dodana rezervacija: ");
        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized void addBorrowedItemToDB(BorrowInfo borrowInfo, Boolean isHistory) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        DatabaseUtils.addBorrowedItemToDatabase(borrowInfo, isHistory);
        System.out.println("dodana posudba: ");
        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized void deleteBorrowedInfoFromDB(Long itemId) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        DatabaseUtils.deleteBorrowedInfoFromDatabase(itemId);
        System.out.println("obrisana posudba: ");
        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized void deleteReservedInfoFromDB(Long itemId) {
        while(activeConnectionWithDatabase) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;
        DatabaseUtils.deleteReservedInfoFromDatabase(itemId);
        System.out.println("obrisana REZERVACIJA: ");
        activeConnectionWithDatabase = false;

        notifyAll();
    }

}
