package tvz.hr.booktrackr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.exception.DifferentPasswordException;
import production.exception.ExistingUserException;
import production.model.Library;
import production.model.User;
import production.utility.DatabaseUtils;
import production.utility.UserChecking;
import tvz.hr.booktrackr.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import static production.utility.DatabaseUtils.*;
import static production.utility.FileUtils.writeUserToFile;


public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private ComboBox<String> libraryComboBox;
    @FXML
    private CheckBox isWorkerCheckbox;
    @FXML
    private PasswordField libraryPasswordField;

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public void initialize() {
        List<String> libraryNames = new ArrayList<>();
        libraryNames = DatabaseUtils.getAllLibrariesFromDatabase().stream().map(Library::getName).collect(Collectors.toList());
        ObservableList observableLibraryList =
                FXCollections.observableArrayList(libraryNames);
        libraryComboBox.setItems(observableLibraryList);
        libraryComboBox.setValue("");
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

    public void registerAction() {
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();
        String libraryName = libraryComboBox.getValue();
        Boolean isWorker = isWorkerCheckbox.isSelected();
        String libraryPassword = libraryPasswordField.getText();
        Library library = findLibraryByName(libraryName).get();

        if (!name.isBlank() && !lastName.isBlank() && !username.isBlank() && !password.isBlank() && !repeatPassword.isBlank() && !libraryName.isBlank()) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            if (!isWorker) {
                try {
                    UserChecking.checkPasswords(password, repeatPassword);
                    UserChecking.checkExistingUser(username);
                    addUserToDatabase(username, hashedPassword, name, lastName, library.getId(), Boolean.FALSE);
                    Long id = getUserIdByUsername(username);
                    writeUserToFile(username, hashedPassword, id);
                    logger.info("Registriran novi korisnik - " + username);
                    switchToLogin();
                }
                catch (DifferentPasswordException | ExistingUserException e) {
                    System.out.println(e.getMessage());
                    logger.info(e.getMessage());
                }
            }
            else if (!libraryPassword.isBlank()) {
                try {
                    if (BCrypt.checkpw(libraryPassword, library.getHashedPassword())) {
                        UserChecking.checkPasswords(password, repeatPassword);
                        UserChecking.checkExistingUser(username);
                        addUserToDatabase(username, hashedPassword, name, lastName, library.getId(), Boolean.TRUE);
                        Long id = getUserIdByUsername(username);
                        writeUserToFile(username, hashedPassword, id);
                        logger.info("Registriran novi radnik - " + username);
                        switchToLogin();
                    }
                    else System.out.println("Unesi ispravnu šifru knjižnice!");
                }
                catch (DifferentPasswordException | ExistingUserException e) {
                    logger.info(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
            else {
                System.out.println("Unesi šifru knjiznice");
            }
        }
        else {
            System.out.println("Nije sve ispunjeno");
        }

    }

    private Long getUserIdByUsername(String username) {
        Long id = -1L;
        List<User> userList = getAllUsersFromDatabase();
        id = userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .map(User::getId)
                .orElse(-1L);
        return id;
    }

    private Optional<Library> findLibraryByName(String libraryName) {
        Optional<Library> optionalLibrary = Optional.empty();
        List<Library> libraryList = DatabaseUtils.getAllLibrariesFromDatabase();
        for (Library lib : libraryList) {
            if (lib.getName().equals(libraryName)) {
                optionalLibrary = Optional.of(lib);
            }
        }
        return optionalLibrary;
    }


}
