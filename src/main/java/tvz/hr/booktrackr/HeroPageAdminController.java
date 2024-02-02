package tvz.hr.booktrackr;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.exception.CheckOptional;
import production.model.*;
import production.threads.GetAllLibrariesThread;
import production.threads.GetLibraryByNameThread;
import production.utility.DataCompare;
import production.utility.DataWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static production.utility.DatabaseUtils.*;
import static production.utility.FileUtils.readDataFromFile;
import static production.utility.FileUtils.writeDataToFile;
import static production.utility.UserChecking.checkPasswords;

public class HeroPageAdminController {

    //Dodavanje knjižnice
    @FXML
    TextField newLibraryNameField;
    @FXML
    TextField newLibraryWebAddressField;
    @FXML
    PasswordField newLibraryPasswordField;
    @FXML
    PasswordField newLibraryRepeatPasswordField;
    @FXML
    TableView<Library> libraryTable;
    @FXML
    TableColumn<Library, String> libraryIdColumn;
    @FXML
    TableColumn<Library, String> libraryNameColumn;
    @FXML
    TableColumn<Library, String> libraryWebAddressColumn;
    //Izvoz bin datoteke
    @FXML
    ComboBox<String> libraryComboBox;
    @FXML
    CheckBox includeUsers;
    @FXML
    CheckBox includeLibraryItems;
    //Usporedba bin data
    @FXML
    ComboBox<String> compareLibrary;
    @FXML
    TableView<User> compareUserTable;
    @FXML
    TableColumn<User,String> compareUserIdColumn;
    @FXML
    TableColumn<User,String> compareUserUsernameColumn;
    @FXML
    TableView<Book> compareBookTable;
    @FXML
    TableColumn<Book, String> compareBookIdColumn;
    @FXML
    TableColumn<Book, String> compareBookTitleColumn;
    @FXML
    TableColumn<Book, String> compareBookAuthorColumn;
    @FXML
    TableView<Movie> compareMovieTable;
    @FXML
    TableColumn<Movie, String> compareMovieIdColumn;
    @FXML
    TableColumn<Movie, String> compareMovieTitleColumn;
    @FXML
    TableColumn<Movie, String> compareMovieDirectorColumn;

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void initialize() {
        libraryIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Library, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Library, String> param) {
                return new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId()));
            }
        });
        libraryNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Library, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Library, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getName());
            }
        });
        libraryWebAddressColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Library, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Library, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getWebAddress());
            }
        });
        //compare
        compareUserIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId()));
            }
        });
        compareUserUsernameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getUsername());
            }
        });
        compareBookIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId()));
            }
        });
        compareBookTitleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTitle());
            }
        });
        compareBookAuthorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getAuthor());
            }
        });
        compareMovieIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId()));
            }
        });
        compareMovieTitleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTitle());
            }
        });
        compareMovieDirectorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getDirector());
            }
        });

        refreshLibraryTable();

        //List<Library> libraryList = DatabaseUtils.getAllLibrariesFromDatabase();
        GetAllLibrariesThread librariesThread = new GetAllLibrariesThread();
        Thread thread = new Thread(librariesThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Library> libraryList = librariesThread.getLibraryList();
        ObservableList<String> observableLibraryList = FXCollections.observableArrayList(libraryList.stream().map(Library::getName).collect(Collectors.toList()));
        libraryComboBox.setItems(observableLibraryList);
        libraryComboBox.setValue(libraryList.get(0).getName());

        //ObservableList<String> observableLibraryNames = FXCollections.observableArrayList(getAllLibrariesFromDatabase().stream().map(Library::getName).collect(Collectors.toList()));
        compareLibrary.setItems(observableLibraryList);
    }

    public void refreshLibraryTable() {
        //List<Library> libraryList = getAllLibrariesFromDatabase();
        GetAllLibrariesThread librariesThread = new GetAllLibrariesThread();
        Thread thread = new Thread(librariesThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Library> libraryList = librariesThread.getLibraryList();
        ObservableList observableLibraryList = FXCollections.observableArrayList(libraryList);
        libraryTable.setItems(observableLibraryList);
    }

    public void addLibrary() {
        System.out.println("sad dodajemo library");
        String newLibraryName = newLibraryNameField.getText();
        String newLibraryWebAddress = newLibraryWebAddressField.getText();
        String newLibraryPassword = newLibraryPasswordField.getText();
        String newLibraryRepeatPassword = newLibraryRepeatPasswordField.getText();
        try {
            checkPasswords(newLibraryPassword, newLibraryRepeatPassword);
            if (!newLibraryName.isBlank() && !newLibraryPassword.isBlank() && !newLibraryWebAddress.isBlank()) {
                String hashedPassword = BCrypt.hashpw(newLibraryPassword, BCrypt.gensalt());
                addLibraryToDatabase(newLibraryName, newLibraryWebAddress,hashedPassword);
            }
        } catch (CheckOptional e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
        refreshLibraryTable();
    }

    public void exportLibraryInfo() {
        String libraryName = libraryComboBox.getValue();
        Boolean includeUsersOption = includeUsers.isSelected();
        Boolean includeLibraryItemsOption = includeLibraryItems.isSelected();
        GetLibraryByNameThread libraryThread = new GetLibraryByNameThread(libraryName);
        Thread thread = new Thread(libraryThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<Library> libraryOptional = libraryThread.getLibrary();
        //Optional<Library> libraryOptional = getLibraryByNameFromDatabase(libraryName);
        if (libraryOptional.isPresent()) {
            Library library = libraryOptional.get();
            DataWrapper dataWrapper = new DataWrapper(library);
            if (includeUsersOption) {
                List<User> allUserList = getAllUsersFromDatabase();
                allUserList = allUserList.stream().filter(u -> u.getLibraryName().equals(libraryName)).collect(Collectors.toList());
                List<User> userList = new ArrayList<>();
                List<Worker> workerList = new ArrayList<>();
                for (User user : allUserList) {
                    if (user instanceof Worker) workerList.add((Worker) user);
                    else userList.add(user);
                }
                dataWrapper.setHasLibraryUsers(true);
                dataWrapper.setUserList(userList);
                dataWrapper.setWorkerList(workerList);
            }
            if (includeLibraryItemsOption) {
                List<Book> bookList = getItemsInChosenLibrary(library, "Book");
                List<Movie> movieList = getItemsInChosenLibrary(library, "Movie");
                dataWrapper.setHasLibraryItems(true);
                dataWrapper.setBookList(bookList);
                dataWrapper.setMovieList(movieList);
            }
            writeDataToFile(dataWrapper);
        }

    }

    public void compareData() {
        Optional<DataWrapper> dataWrapperOptional = readDataFromFile();
        if (dataWrapperOptional.isPresent()) {
            DataWrapper dataWrapper = dataWrapperOptional.get();
            dataWrapper = DataCompare.compareDataWrapperToCurrent(dataWrapper);
            ObservableList<User> observableUserList = FXCollections.observableArrayList(dataWrapper.getUserList());
            compareUserTable.setItems(observableUserList);
            ObservableList<Book> observableBookList = FXCollections.observableArrayList(dataWrapper.getBookList());
            compareBookTable.setItems(observableBookList);
            ObservableList<Movie> observableMovieList = FXCollections.observableArrayList(dataWrapper.getMovieList());
            compareMovieTable.setItems(observableMovieList);
        }
    }

    public void switchToLogin() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("loginView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na login");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

    public void switchToLog() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("logView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na log");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }
}
