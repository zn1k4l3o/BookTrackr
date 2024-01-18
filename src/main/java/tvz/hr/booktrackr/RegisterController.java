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
import production.utility.UserChecking;
import tvz.hr.booktrackr.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

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
        //promijeniti
        List<String> libraryNames = new ArrayList<>();
        libraryNames.add("knjiznica1");
        libraryNames.add("knjiznica2");
        libraryNames.add("knjiznica3");
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
        String checkPassword = repeatPasswordField.getText();
        String library = libraryComboBox.getValue();
        Boolean isWorker = isWorkerCheckbox.isSelected();
        String libraryPassword = libraryPasswordField.getText();
        if (!name.isBlank() && !lastName.isBlank() && !username.isBlank() && !password.isBlank() && !checkPassword.isBlank() && !library.isBlank()) {
            try {
                UserChecking.checkPasswords(password, checkPassword);
                UserChecking.checkExistingUser(username);
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                writeUserToFile(username, hashedPassword, 0L);
                logger.info("Registriran novi korisnik");
            }
            catch (DifferentPasswordException | ExistingUserException e) {
                System.out.println(e.getMessage());
                logger.info(e.getMessage());
            }
        }
        else {

        }

    }


}
