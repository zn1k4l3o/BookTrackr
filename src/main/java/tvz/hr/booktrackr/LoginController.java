package tvz.hr.booktrackr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.generics.DataChange;
import production.model.Library;
import production.model.User;
import production.model.Worker;
import production.threads.GetAllLibrariesThread;
import production.utility.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static production.utility.DatabaseUtils.getUserByIdFromDatabase;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> libraryComboBox;

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void initialize() {
        List<String> libraryNames = new ArrayList<>();
        GetAllLibrariesThread librariesThread = new GetAllLibrariesThread();
        Thread thread = new Thread(librariesThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Library> libraryList = librariesThread.getLibraryList();
        if (libraryList.isEmpty()) AlertWindow.showNotificationDialog("Problemi s bazom", "Nismo u mogućnosti doći do podataka");
        else {
            libraryNames = libraryList.stream().map(Library::getName).collect(Collectors.toList());
            ObservableList observableLibraryList =
                    FXCollections.observableArrayList(libraryNames);
            libraryComboBox.setItems(observableLibraryList);
            libraryComboBox.setValue("");
            DataChangeWrapper dataChangeWrapper = FileUtils.readDataChangeFromFile();
            DataChange<String,String> dc = new DataChange<>("Aplikacija", "otvoreno login");
            dataChangeWrapper.addDataChange(dc);
        }

    }

    public void switchToRegister() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("registerView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na register");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

    public void loginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.equals("admin")) {
            User user;
            Optional<User> userOptional = FileUtils.getUserByUsernameFromFile(username);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                if (BCrypt.checkpw(password, user.getHashedPassword())) {
                    SessionManager.setCurrentUser(user);
                    switchToHeroPageAdmin();
                }
            }
        }
        else if (UserChecking.doesUsernameExist(username)) {
            String libraryName = libraryComboBox.getValue();
            User user;
            Optional<User> userOptional = FileUtils.getUserByUsernameFromFile(username);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                user = getUserByIdFromDatabase(user.getId()).get();
                if (libraryName.equals(user.getLibraryName())) {
                    if (BCrypt.checkpw(password, user.getHashedPassword())) {
                        SessionManager.setCurrentUser(user);
                        SessionManager.setCurrentLibraryByName(libraryName);
                        if (user instanceof Worker) switchToHeroPageWorker();
                        else switchToHeroPageUser();
                    }
                    else AlertWindow.showNotificationDialog("Kriva lozinka", "Unijeli ste krivu lozinku!");
                }
            }
        }
        else {
            AlertWindow.showNotificationDialog("Nepostojeći korisnik", "Korisnik ne postoji u fileu!");
        }
    }

    public void switchToHeroPageUser() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("heroPageUser.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na hero page user");
        DataChangeWrapper dataChangeWrapper = FileUtils.readDataChangeFromFile();
        DataChange<String,String> dc = new DataChange<>("Aplikacija", "otvoreno hero page user");
        dataChangeWrapper.addDataChange(dc);
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

    public void switchToHeroPageWorker() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("heroPageWorker.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na hero page worker");
        DataChangeWrapper dataChangeWrapper = FileUtils.readDataChangeFromFile();
        DataChange<String,String> dc = new DataChange<>("Aplikacija", "otvoreno hero page worker");
        dataChangeWrapper.addDataChange(dc);
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

    public void switchToHeroPageAdmin() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("heroPageAdmin.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na hero page admin");
        DataChangeWrapper dataChangeWrapper = FileUtils.readDataChangeFromFile();
        DataChange<String,String> dc = new DataChange<>("Aplikacija", "otvoreno hero page admin");
        dataChangeWrapper.addDataChange(dc);
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

}