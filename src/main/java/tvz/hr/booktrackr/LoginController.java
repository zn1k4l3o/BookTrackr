package tvz.hr.booktrackr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> libraryComboBox;

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
        System.out.println("Prebacuje na registerView user");
    }

    public void loginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        //heširanje
        System.out.println("Login: " + username + ", " + password);
    }


}