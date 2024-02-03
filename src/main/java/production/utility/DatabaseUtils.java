package production.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.enums.Genre;
import production.exception.CheckOptional;
import production.model.*;
import production.threads.ConnectThread;
import production.threads.GetBooksInLibraryThread;
import tvz.hr.booktrackr.App;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static production.utility.FileUtils.deleteUserFromFile;

public class DatabaseUtils {

    private static final String DATABASE_FILE = "database.properties";

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static List<User> getAllUsersFromDatabase()
    {
        List<User> userList = new ArrayList<>();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection =  connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet usersResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM ALL_USERS");
                while (usersResultSet.next()) {
                    User newUser = getUserFromResultSet(usersResultSet);
                    userList.add(newUser);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        } catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return userList;
    }

    public static Optional<User> getUserByIdFromDatabase(Long id)
    {
        Optional<User> userOptional = Optional.empty();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet userResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM ALL_USERS WHERE ID = " + id);
                if (userResultSet.next()) {
                    userOptional = Optional.of(getUserFromResultSet(userResultSet));
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return userOptional;
    }

    public static void addUserToDatabase(String username, String hashedPassword, String name, String lastName, Long libraryId, Boolean isWorker) {
        String query = "INSERT INTO ALL_USERS (USERNAME, PASSWORD, NAME, LASTNAME, LIBRARY_ID, IS_WORKER) VALUES (?, ?, ?, ?, ?, ?)";

        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
        try (Connection connection = connectThread.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lastName);
            preparedStatement.setLong(5, libraryId);
            preparedStatement.setBoolean(6, isWorker);

            preparedStatement.executeUpdate();
            }
        } catch (SQLException | InterruptedException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
    }

    private static User getUserFromResultSet(ResultSet usersResultSet) throws SQLException {
        Long id = usersResultSet.getLong("ID");
        String username = usersResultSet.getString("USERNAME");
        String hashedPassword = usersResultSet.getString("PASSWORD");
        String name = usersResultSet.getString("NAME");
        String lastName = usersResultSet.getString("LASTNAME");
        Long libraryId = usersResultSet.getLong("LIBRARY_ID");
        Boolean isWorker = usersResultSet.getBoolean("IS_WORKER");

        Library library = getLibraryByIdFromDatabase(libraryId).get();

        if (isWorker) return new Worker.Builder(username).withId(id).withHashedPassword(hashedPassword).withName(name).withLastName(lastName).withLibraryName(library.getName()).withIsWorker(isWorker).build();
        else return new User.Builder(username).withId(id).withHashedPassword(hashedPassword).withName(name).withLastName(lastName).withLibraryName(library.getName()).build();
    }

    public static Optional<Library> getLibraryByIdFromDatabase(Long id)
    {
        Optional<Library> libraryOptional = Optional.empty();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet libraryResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM LIBRARY WHERE ID = " + id);
                if (libraryResultSet.next()) {
                    libraryOptional = Optional.of(getLibraryFromResultSet(libraryResultSet));
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return libraryOptional;
    }

    public static Optional<Library> getLibraryByNameFromDatabase(String name)
    {
        Optional<Library> libraryOptional = Optional.empty();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet libraryResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM LIBRARY WHERE NAME = '" + name + "'");
                if (libraryResultSet.next()) {
                    libraryOptional = Optional.of(getLibraryFromResultSet(libraryResultSet));
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return libraryOptional;
    }

    public static List<Library> getAllLibrariesFromDatabase()
    {
        List<Library> libraryList = new ArrayList<>();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet libraryResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM LIBRARY");
                while (libraryResultSet.next()) {
                    Library newLibrary = getLibraryFromResultSet(libraryResultSet);
                    libraryList.add(newLibrary);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return libraryList;
    }

    private static Library getLibraryFromResultSet(ResultSet libraryResultSet) throws SQLException {
        Long id = libraryResultSet.getLong("ID");
        String name = libraryResultSet.getString("NAME");
        String hashedPassword = libraryResultSet.getString("PASSWORD");
        String webAddress = libraryResultSet.getString("WEB_ADDRESS");

        return new Library.Builder(id).withName(name).withWebAddress(webAddress).withHashedPassword(hashedPassword).build();
    }

    public static void addLibraryToDatabase(String name, String webAddress, String hashedPassword) {
        String query = "INSERT INTO LIBRARY (NAME, WEB_ADDRESS, PASSWORD) VALUES (?, ?, ?)";
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try (Connection connection = connectThread.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, webAddress);
                preparedStatement.setString(3, hashedPassword);

                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException | InterruptedException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
    }

    public static <T extends LibraryItem> List<T> getItemsInChosenLibrary(Library library, String category) {
        List<T> itemsInLibrary = new ArrayList<>();
        List<Long> itemIdList = getItemIdsInLibraryFromDatabase(library);
        if (category.equals("Book")) {
            List<Book> bookList = getAllBooksFromDatabase();
            for (Book book : bookList) {
                Long bookId = book.getId();
                for (Long itemId : itemIdList) {
                    if (itemId.equals(bookId)) itemsInLibrary.add((T) book);
                }
            }
        }
        else if(category.equals("Movie")) {
            List<Movie> movieList = getAllMoviesFromDatabase();
            for (Movie movie : movieList) {
                Long movieId = movie.getId();
                for (Long itemId : itemIdList) {
                    if (itemId.equals(movieId)) itemsInLibrary.add((T) movie);
                }
            }
        }
        else {
            System.out.println("Nez koji to category");
        }
        return itemsInLibrary;
    }

    private static List<Long> getItemIdsInLibraryFromDatabase(Library library) {
        List<Long> itemIdsInLibrary = new ArrayList<>();
        try {
            Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet libraryItemIdResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM LIBRARY_ITEM");
            while(libraryItemIdResultSet.next()) {
                Long libraryId = libraryItemIdResultSet.getLong("LIBRARY_ID");
                Long itemId = libraryItemIdResultSet.getLong("ITEM_ID");
                if (libraryId.equals(library.getId())) itemIdsInLibrary.add(itemId);
            }
            connection.close();
        }
        catch (IOException | SQLException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return itemIdsInLibrary;
    }

    public static void addBookToDatabase(String title, String genre, String publisher, String author, Float rating) {

        Long genreId = findGenreId(genre);
        Long childId = -1L;
        Long itemId = -1L;

        childId = addToBookTable(publisher, author, genreId);

        itemId = addToItemTable(title, rating, childId, "Book");

        addRelationToLibraryItemTable(itemId);
    }

    private static void addRelationToLibraryItemTable(Long itemId) {
        String query;
        query = "INSERT INTO LIBRARY_ITEM (LIBRARY_ID, ITEM_ID) VALUES (?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement3 = connection.prepareStatement(query)) {

            //String libraryName = SessionManager.getCurrentUser().getLibraryName();
            ///////////Zamijeniti sa SessionManagerom
            /*
            List<Library> libraryList = getAllLibrariesFromDatabase();
            Long libraryId = libraryList.stream()
                    .filter(lib -> lib.getName().equals(libraryName))
                    .findFirst()
                    .map(Library::getId).get();

             */
            //////////////////////////
            preparedStatement3.setLong(1, SessionManager.getCurrentLibrary().getId());
            preparedStatement3.setLong(2, itemId);

            preparedStatement3.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
    }

    private static Long addToItemTable(String title, Float rating, Long childId, String category) {
        String query;
        Long itemId = -1L;
        query = "INSERT INTO ITEM (TITLE, RATING, CATEGORY, CHILD_ID) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement2 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement2.setString(1, title);
            preparedStatement2.setFloat(2, rating);
            preparedStatement2.setString(3, category);
            preparedStatement2.setLong(4, childId);

            Integer affectedRows = preparedStatement2.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement2.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        itemId = generatedKeys.getLong(1);
                    }
                }
            }

        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
        return itemId;
    }

    private static Long addToBookTable(String publisher, String author, Long genreId) {
        String query = "INSERT INTO BOOK (AUTHOR, GENRE_ID, PUBLISHER) VALUES (?, ?, ?)";
        Long childId = -1L;
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement1 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement1.setString(1, author);
            preparedStatement1.setLong(2, genreId);
            preparedStatement1.setString(3, publisher);

            Integer affectedRows = preparedStatement1.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement1.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        childId = generatedKeys.getLong(1);
                    }
                }
            }

        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
        return childId;
    }

    public static List<Book> getAllBooksFromDatabase()
    {
        List<Book> bookList = new ArrayList<>();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectToDatabase();
                Statement sqlStatement = connection.createStatement();
                ResultSet itemResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM ITEM WHERE CATEGORY='Book'");
                Statement sqlStatement2 = connection.createStatement();
                while (itemResultSet.next()) {
                    Long childId = getItemChildIdFromResultSet(itemResultSet);
                    Book newBook = new Book("CORRUPTED");
                    ResultSet bookResultSet = sqlStatement2.executeQuery(
                            "SELECT * FROM BOOK WHERE ID=" + childId);
                    if (bookResultSet.next()) newBook = getBookFromResultSet(bookResultSet, itemResultSet);
                    bookList.add(newBook);
                }
                connection.close();
            }
            catch (SQLException | IOException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return bookList;
    }

    public static String getItemCategory(Long itemId) {
        String category = "None";
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet itemResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM ITEM WHERE ID=" + itemId);
                if (itemResultSet.next()) category = itemResultSet.getString("CATEGORY");
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return category;
    }

    public static Optional<Book> getBookByIdFromDatabase(Long itemId)
    {
        Optional<Book> book = Optional.empty();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet itemResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM ITEM WHERE ID=" + itemId);
                Statement sqlStatement2 = connection.createStatement();
                if (itemResultSet.next()) {
                    Long childId = getItemChildIdFromResultSet(itemResultSet);
                    ResultSet bookResultSet = sqlStatement2.executeQuery(
                            "SELECT * FROM BOOK WHERE ID=" + childId);
                    Book newBook = new Book("CORRUPTED");
                    if (bookResultSet.next()) newBook = getBookFromResultSet(bookResultSet, itemResultSet);
                    book = Optional.of(newBook);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return book;
    }

    public static Optional<Movie> getMovieByIdFromDatabase(Long itemId)
    {
        Optional<Movie> movie = Optional.empty();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet itemResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM ITEM WHERE ID=" + itemId);
                Statement sqlStatement2 = connection.createStatement();
                if (itemResultSet.next()) {
                    Long childId = getItemChildIdFromResultSet(itemResultSet);
                    ResultSet bookResultSet = sqlStatement2.executeQuery(
                            "SELECT * FROM MOVIE WHERE ID=" + childId);
                    Movie newMovie = new Movie("CORRUPTED");
                    if (bookResultSet.next()) newMovie = getMovieFromResultSet(bookResultSet, itemResultSet);
                    movie = Optional.of(newMovie);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return movie;
    }

    private static Book getBookFromResultSet(ResultSet bookResultSet, ResultSet itemResultSet) throws SQLException {
        Long id = itemResultSet.getLong("ID");
        String title = itemResultSet.getString("TITLE");
        Float rating = itemResultSet.getFloat("RATING");
        String author = bookResultSet.getString("AUTHOR");
        long genreId = bookResultSet.getLong("GENRE_ID");
        String publisher = bookResultSet.getString("PUBLISHER");
        String genre = Genre.values()[(int) (genreId-1)].name();

        return new Book.Builder(title).withId(id).withRating(rating).withAuthor(author).withGenre(genre).withPublisher(publisher).build();
    }

    private static Long findGenreId(String genre) {
        Long genreId = -1L;
        List<Genre> genres = Arrays.stream(Genre.values()).toList();
        for (int i = 0; i < genres.size(); i++) {
            if (genres.get(i).name().equals(genre)) genreId = Long.valueOf(i+1);
        }
        return genreId;
    }

    public static void addMovieToDatabase(String title, String description, String director, String filmRatingSystem, Float rating, Integer releaseYear) {

        Long childId = -1L;
        Long itemId = -1L;

        childId = addToMovieTable(description, director, filmRatingSystem, releaseYear);

        itemId = addToItemTable(title, rating, childId, "Movie");

        addRelationToLibraryItemTable(itemId);
    }

    private static Long addToMovieTable(String description, String director, String filmRatingSystem, Integer releaseYear) {
        Long childId = -1L;
        String query = "INSERT INTO MOVIE (DESCRIPTION, DIRECTOR, FILM_RATING_SYSTEM, RELEASE_YEAR) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement1 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement1.setString(1, description);
            preparedStatement1.setString(2, director);
            preparedStatement1.setString(3, filmRatingSystem);
            preparedStatement1.setInt(4, releaseYear);

            Integer affectedRows = preparedStatement1.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement1.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        childId = generatedKeys.getLong(1);
                    }
                }
            }

        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
        return childId;
    }

    public static List<Movie> getAllMoviesFromDatabase()
    {
        List<Movie> movieList = new ArrayList<>();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet itemResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM ITEM WHERE CATEGORY='Movie'");
                Statement sqlStatement2 = connection.createStatement();
                while (itemResultSet.next()) {
                    Long childId = getItemChildIdFromResultSet(itemResultSet);
                    Movie newMovie = new Movie("CORRUPTED");
                    ResultSet movieResultSet = sqlStatement2.executeQuery(
                            "SELECT * FROM MOVIE WHERE ID=" + childId);
                    if (movieResultSet.next()) newMovie = getMovieFromResultSet(movieResultSet, itemResultSet);
                    movieList.add(newMovie);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return movieList;
    }

    private static Long getItemChildIdFromResultSet(ResultSet itemResultSet) throws SQLException {
        return itemResultSet.getLong("CHILD_ID");
    }

    private static Movie getMovieFromResultSet(ResultSet movieResultSet, ResultSet itemResultSet) throws SQLException {
        Long id = itemResultSet.getLong("ID");
        String title = itemResultSet.getString("TITLE");
        Float rating = itemResultSet.getFloat("RATING");
        String director = movieResultSet.getString("DIRECTOR");
        String description = movieResultSet.getString("DESCRIPTION");
        String filmRatingSystem = movieResultSet.getString("FILM_RATING_SYSTEM");
        Integer releaseYear = movieResultSet.getInt("RELEASE_YEAR");

        return new Movie.Builder(title)
                .withId(id)
                .withRating(rating)
                .withDirector(director)
                .withReleaseYear(releaseYear)
                .withFilmRatingSystem(filmRatingSystem)
                .withDescription(description).build();
    }

    public static Optional<BorrowInfo> getBorrowingInfoForItemIdFromDatabase(Long itemId) {
        Optional<BorrowInfo> borrowInfoOptional = Optional.empty();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet borrowInfoResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM BORROWED WHERE ITEM_ID=" + itemId);
                if (borrowInfoResultSet.next()) {
                    BorrowInfo newBorrowInfo = getBorrowInfoFromResultSet(borrowInfoResultSet);
                    borrowInfoOptional = Optional.of(newBorrowInfo);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return borrowInfoOptional;
    }

    public static Optional<ReservedInfo> getReservedInfoForItemIdFromDatabase(Long itemId) {
        Optional<ReservedInfo> reservedInfoOptional = Optional.empty();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet reservedInfoResultSet = sqlStatement.executeQuery(
                        "SELECT * FROM RESERVED WHERE ITEM_ID=" + itemId);
                if (reservedInfoResultSet.next()) {
                    ReservedInfo newReservedInfo = getReservedInfoFromResultSet(reservedInfoResultSet);
                    reservedInfoOptional = Optional.of(newReservedInfo);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return reservedInfoOptional;
    }

    public static List<BorrowInfo> getBorrowingInfoForUserIdFromDatabase(Long userId, Boolean getHistory) {
        String query = "SELECT * FROM BORROWED WHERE USER_ID=";
        if (getHistory) query = "SELECT * FROM BORROWED_HISTORY WHERE USER_ID=";
        List<BorrowInfo> borrowInfoList = new ArrayList<>();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet borrowInfoResultSet = sqlStatement.executeQuery(
                        query + userId);
                while (borrowInfoResultSet.next()) {
                    BorrowInfo newBorrowInfo = getBorrowInfoFromResultSet(borrowInfoResultSet);
                    borrowInfoList.add(newBorrowInfo);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return borrowInfoList;
    }

    public static List<ReservedInfo> getReservedInfoForUserIdFromDatabase(Long userId, Boolean isHistory) {
        String query = "SELECT * FROM RESERVED WHERE USER_ID=";
        if (isHistory) query = "SELECT * FROM RESERVED_HISTORY WHERE USER_ID=";
        List<ReservedInfo> reservedInfoList = new ArrayList<>();
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try {
                Connection connection = connectThread.getConnection();
                Statement sqlStatement = connection.createStatement();
                ResultSet reservedInfoResultSet = sqlStatement.executeQuery(
                        query + userId);
                while (reservedInfoResultSet.next()) {
                    ReservedInfo newReservedInfo = getReservedInfoFromResultSet(reservedInfoResultSet);
                    reservedInfoList.add(newReservedInfo);
                }
                connection.close();
            } catch (SQLException e) {
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
                logger.info(e.getMessage());
            }
        }
        catch (InterruptedException e) {
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            logger.info(e.getMessage());
        }
        return reservedInfoList;
    }

    private static BorrowInfo getBorrowInfoFromResultSet(ResultSet borrowInfoResultSet) throws SQLException {
        Long id = borrowInfoResultSet.getLong("ID");
        Long itemId = borrowInfoResultSet.getLong("ITEM_ID");
        Long userId = borrowInfoResultSet.getLong("USER_ID");
        Date borrowDate = borrowInfoResultSet.getDate("BORROW_DATE");
        Date returnDate = borrowInfoResultSet.getDate("RETURN_DATE");

        return new BorrowInfo(id, itemId, userId, borrowDate, returnDate);
    }

    private static ReservedInfo getReservedInfoFromResultSet(ResultSet reservedInfoResultSet) throws SQLException {
        Long id = reservedInfoResultSet.getLong("ID");
        Long itemId = reservedInfoResultSet.getLong("ITEM_ID");
        Long userId = reservedInfoResultSet.getLong("USER_ID");
        Date reservedDate = reservedInfoResultSet.getDate("RESERVED_DATE");

        return new ReservedInfo(id, itemId, userId, reservedDate);
    }

    public static void addReservedItemToDatabase(ReservedInfo reservedInfo, Boolean isHistory) {
        String query = "INSERT INTO RESERVED (ITEM_ID, USER_ID, RESERVED_DATE) VALUES (?, ?, ?)";
        if (isHistory) query = "INSERT INTO RESERVED_HISTORY (ITEM_ID, USER_ID, RESERVED_DATE) VALUES (?, ?, ?)";
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
        try (Connection connection = connectThread.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, reservedInfo.itemId());
            preparedStatement.setLong(2, reservedInfo.userId());
            preparedStatement.setDate(3, reservedInfo.reservedDate());

            preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addBorrowedItemToDatabase(BorrowInfo borrowInfo, Boolean isHistory) {
        String query = "INSERT INTO BORROWED (ITEM_ID, USER_ID, BORROW_DATE, RETURN_DATE) VALUES (?, ?, ?, ?)";
        if (isHistory) query = "INSERT INTO BORROWED_HISTORY (ITEM_ID, USER_ID, BORROW_DATE, RETURN_DATE) VALUES (?, ?, ?, ?)";
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try (Connection connection = connectThread.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setLong(1, borrowInfo.itemId());
                preparedStatement.setLong(2, borrowInfo.userId());
                preparedStatement.setDate(3, borrowInfo.borrowDate());
                preparedStatement.setDate(4, borrowInfo.returnDate());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteBorrowedInfoFromDatabase(Long itemId, Boolean isHistory)
    {
        String query = "DELETE FROM BORROWED WHERE ITEM_ID = ?";
        if (isHistory) query = "DELETE FROM BORROWED_HISTORY WHERE ITEM_ID = ?";
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try (Connection connection = connectThread.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setLong(1, itemId);
                preparedStatement.executeUpdate();
                connection.close();
            }
        }
        catch (SQLException | InterruptedException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
    }

    public static void deleteReservedInfoFromDatabase(Long itemId, Boolean isHistory)
    {
        String query = "DELETE FROM RESERVED WHERE ITEM_ID = ?";
        if (isHistory) query = "DELETE FROM RESERVED_HISTORY WHERE ITEM_ID = ?";
        ConnectThread connectThread = new ConnectThread();
        Thread thread = new Thread(connectThread);
        thread.start();
        try {
            thread.join();
            try (Connection connection = connectThread.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setLong(1, itemId);
                preparedStatement.executeUpdate();
                connection.close();
            }
        }
        catch (SQLException e) {
        logger.info(e.getMessage());
        AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static void deleteUserFromDatabase(Long userId) {
            String query = "DELETE FROM ALL_USERS WHERE ID = ?";
            try (Connection connection = connectToDatabase();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setLong(1, userId);
                preparedStatement.executeUpdate();
                connection.close();
            } catch (SQLException | IOException e) {
                logger.info(e.getMessage());
                AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
            }
    }

    private static Long getItemByIdFromDatabase(Long itemId) {
        Long childId = -1L;
        try {Connection connection = connectToDatabase();
             Statement sqlStatement = connection.createStatement();
             ResultSet itemResultSet = sqlStatement.executeQuery(
                     "SELECT * FROM ITEM WHERE ID = " + itemId);
             if (itemResultSet.next()) {
                 childId = getItemChildIdFromResultSet(itemResultSet);
             }
             connection.close();
        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
        return childId;
    }
    public static void deleteItemFromDatabase(long itemId) {
        Long childId = getItemByIdFromDatabase(itemId);
        String genre = getItemCategory(itemId);
        String query = "DELETE FROM BOOK WHERE ID = ?";
        if (genre.equals("Movie")) query = "DELETE FROM MOVIE WHERE ID = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, childId);
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
        query = "DELETE FROM LIBRARY_ITEM WHERE ITEM_ID = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, itemId);
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
        query = "DELETE FROM ITEM WHERE ID = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, itemId);
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            AlertWindow.showNotificationDialog("Problemi s bazom", e.getMessage());
        }
    }

    public static void updateUserInDatabase(User newUser) {
        List<ReservedInfo> currentlyReserved = getReservedInfoForUserIdFromDatabase(SessionManager.getCurrentUser().getId(), Boolean.FALSE);
        List<ReservedInfo> reservedHistory = getReservedInfoForUserIdFromDatabase(SessionManager.getCurrentUser().getId(), Boolean.TRUE);
        List<BorrowInfo> currentlyBorrowed = getBorrowingInfoForUserIdFromDatabase(SessionManager.getCurrentUser().getId(), Boolean.FALSE);
        List<BorrowInfo> borrowedHistory = getBorrowingInfoForUserIdFromDatabase(SessionManager.getCurrentUser().getId(), Boolean.TRUE);
        Library library = SessionManager.getCurrentLibrary();

        for (BorrowInfo borrowInfo : currentlyBorrowed) deleteBorrowedInfoFromDatabase(borrowInfo.itemId(), Boolean.FALSE);
        for (BorrowInfo borrowInfo : borrowedHistory) deleteBorrowedInfoFromDatabase(borrowInfo.itemId(), Boolean.TRUE);
        for (ReservedInfo reservedInfo : currentlyReserved) deleteReservedInfoFromDatabase(reservedInfo.itemId(), Boolean.FALSE);
        for (ReservedInfo reservedInfo : reservedHistory) deleteReservedInfoFromDatabase(reservedInfo.itemId(), Boolean.TRUE);

        deleteUserFromFile(SessionManager.getCurrentUser());
        deleteUserFromDatabase(newUser.getId());
        if (newUser instanceof Worker) addUserToDatabase(newUser.getUsername(), newUser.getHashedPassword(), newUser.getName(), newUser.getLastName(), library.getId(), Boolean.TRUE);
        else addUserToDatabase(newUser.getUsername(), newUser.getHashedPassword(), newUser.getName(), newUser.getLastName(), library.getId(), Boolean.FALSE);
        Long id = getUserIdByUsername(newUser.getUsername());
        newUser.setId(id);
        FileUtils.writeUserToFile(newUser.getUsername(), newUser.getHashedPassword(), id, Boolean.TRUE);

        Optional<User> userOptional = FileUtils.getUserByUsernameFromFile(newUser.getUsername());
        try {
            UserChecking.checkOptional(userOptional);
            for (BorrowInfo borrowInfo : currentlyBorrowed) {
                BorrowInfo newBorrowInfo = new BorrowInfo(-1L, borrowInfo.itemId(), id, borrowInfo.borrowDate(), borrowInfo.returnDate());
                addBorrowedItemToDatabase(newBorrowInfo, Boolean.FALSE);
            }
            for (BorrowInfo borrowInfo : borrowedHistory) {
                BorrowInfo newBorrowInfo = new BorrowInfo(-1L, borrowInfo.itemId(), id, borrowInfo.borrowDate(), borrowInfo.returnDate());
                addBorrowedItemToDatabase(newBorrowInfo, Boolean.TRUE);
            }
            for (ReservedInfo reservedInfo : currentlyReserved) {
                ReservedInfo newReservedInfo = new ReservedInfo(-1L, reservedInfo.itemId(), id, reservedInfo.reservedDate());
                addReservedItemToDatabase(newReservedInfo, Boolean.FALSE);
            }
            for (ReservedInfo reservedInfo : reservedHistory) {
                ReservedInfo newReservedInfo = new ReservedInfo(-1L, reservedInfo.itemId(), id, reservedInfo.reservedDate());
                addReservedItemToDatabase(newReservedInfo, Boolean.TRUE);
            }
            SessionManager.setCurrentUser(newUser);


        }
        catch (CheckOptional e) {
            logger.info(e.getMessage());
        }

    }

    private static Long getUserIdByUsername(String username) {
        Long id = -1L;
        List<User> userList = getAllUsersFromDatabase();
        id = userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .map(User::getId)
                .orElse(-1L);
        return id;
    }


    public synchronized static Connection connectToDatabase() throws SQLException, IOException {
        Properties svojstva = new Properties();
        svojstva.load(new FileReader(DATABASE_FILE));
        String urlBazePodataka = svojstva.getProperty("databaseUrl");
        String korisnickoIme = svojstva.getProperty("username");
        String lozinka = svojstva.getProperty("password");
        Connection connection = DriverManager.getConnection(urlBazePodataka, korisnickoIme,lozinka);
        return connection;
    }
}
