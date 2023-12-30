package tvz.hr.booktrackr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("registerView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

    public void loginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        //heširanje
        System.out.println("Login: " + username + ", " + password);
    }


}