package tvz.hr.booktrackr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import production.model.User;
import production.utility.SessionManager;

import static production.utility.DatabaseUtils.updateUserInDatabase;

public class EditUserInfoViewController {

    @FXML
    Label usernameLabel;
    @FXML
    TextField nameField;
    @FXML
    TextField lastnameField;
    @FXML
    Label libraryNameLabel;

    public void initialize() {
        usernameLabel.setText(SessionManager.getCurrentUser().getUsername());
        nameField.setText(SessionManager.getCurrentUser().getName());
        lastnameField.setText(SessionManager.getCurrentUser().getLastName());
        libraryNameLabel.setText(SessionManager.getCurrentLibrary().getName());
    }

    public void changeUserInfo() {
        User newUser = SessionManager.getCurrentUser();
        String name = nameField.getText();
        String lastname = lastnameField.getText();
        if (!name.isBlank()) newUser.setName(name);
        if (!lastname.isBlank()) newUser.setLastName(lastname);
        updateUserInDatabase(newUser);
    }
}
