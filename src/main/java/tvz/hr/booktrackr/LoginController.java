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
import production.model.User;
import production.utility.FileUtils;
import production.utility.SessionManager;
import production.utility.UserChecking;
import tvz.hr.booktrackr.App;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> libraryComboBox;

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

    public void switchToRegister() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("registerView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
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

        if (UserChecking.doesUsernameExist(username)) {

            User user;
            Optional<User> userOptional = FileUtils.getUserByUsernameFromFile(username);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                if (BCrypt.checkpw(password, user.getHashedPassword())) {
                    System.out.println("Uspjelo");
                    SessionManager.setCurrentUser(user);
                    switchToHeroPageUser();
                }
            }
        }
        else {
            ///napisi nes
        }
    }

    public void switchToHeroPageUser() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("heroPageUser.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na hero page user");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

}