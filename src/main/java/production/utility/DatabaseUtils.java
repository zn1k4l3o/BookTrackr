package production.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.enums.Genre;
import production.enums.LibraryEnum;
import production.model.*;
import tvz.hr.booktrackr.App;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DatabaseUtils {

    private static final String DATABASE_FILE = "database.properties";

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static List<User> getAllUsersFromDatabase()
    {
        List<User> userList = new ArrayList<>();
        try {
            Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet usersResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM ALL_USERS");
            while(usersResultSet.next()) {
                User newUser = getUserFromResultSet(usersResultSet);
                userList.add(newUser);
            }
            connection.close();
        }
        catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
        }
        return userList;
    }

    public static Optional<User> getUserByIdFromDatabase(Long id)
    {
        Optional<User> userOptional = Optional.empty();
        try {
            Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet userResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM ALL_USERS WHERE ID = " + id);
            if (userResultSet.next()) {
                userOptional = Optional.of(getUserFromResultSet(userResultSet));
            }
            connection.close();
        }
        catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
        }
        return userOptional;
    }

    public static void addUserToDatabase(String username, String hashedPassword, String name, String lastName, Long libraryId, Boolean isWorker) {
        String query = "INSERT INTO ALL_USERS (USERNAME, PASSWORD, NAME, LASTNAME, LIBRARY_ID, IS_WORKER) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lastName);
            preparedStatement.setLong(5, libraryId);
            preparedStatement.setBoolean(6, isWorker);

            preparedStatement.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
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
        try {
            Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet libraryResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM LIBRARY WHERE ID = " + id);
            if (libraryResultSet.next()) {
                libraryOptional = Optional.of(getLibraryFromResultSet(libraryResultSet));
            }
            connection.close();
        }
        catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
        }
        return libraryOptional;
    }

    public static List<Library> getAllLibrariesFromDatabase()
    {
        List<Library> libraryList = new ArrayList<>();
        try {
            Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet libraryResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM LIBRARY");
            while(libraryResultSet.next()) {
                Library newLibrary = getLibraryFromResultSet(libraryResultSet);
                libraryList.add(newLibrary);
            }
            connection.close();
        }
        catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
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

        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, webAddress);
            preparedStatement.setString(3, hashedPassword);

            preparedStatement.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void addBookToDatabase(String title, String genre, String publisher, String author, Float rating) {
        String query = "INSERT INTO BOOK (AUTHOR, GENRE_ID, PUBLISHER) VALUES (?, ?, ?)";

        Long genreId = findGenreId(genre);
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
                        System.out.println("Generated Item ID: " + childId);
                    } else {
                        System.out.println("Failed to get generated keys");
                    }
                }
            }

        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }

        query = "INSERT INTO ITEM (TITLE, RATING, CATEGORY, CHILD_ID) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement2 = connection.prepareStatement(query)) {

            preparedStatement2.setString(1, title);
            preparedStatement2.setFloat(2, rating);
            preparedStatement2.setString(3, "Book");
            preparedStatement2.setLong(4, childId);

            preparedStatement2.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static List<Book> getAllBooksFromDatabase()
    {
        List<Book> bookList = new ArrayList<>();
        try {
            Connection connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet bookResultSet = sqlStatement.executeQuery(
                    "SELECT * FROM BOOK");
            Statement sqlStatement2 = connection.createStatement();
            ResultSet itemResultSet = sqlStatement2.executeQuery(
                    "SELECT * FROM ITEM WHERE CATEGORY='Book'");
            while(itemResultSet.next() && bookResultSet.next()) {
                Book newBook = getBookFromResultSet(bookResultSet, itemResultSet);
                bookList.add(newBook);
            }
            connection.close();
        }
        catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
            logger.info(e.getMessage());
        }
        return bookList;
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
