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
import production.exception.DifferentPasswordException;
import production.model.Library;
import production.utility.DatabaseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static production.utility.DatabaseUtils.*;
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

        refreshLibraryTable();

        List<Library> libraryList = DatabaseUtils.getAllLibrariesFromDatabase();
        ObservableList<String> observableLibraryList = FXCollections.observableArrayList(libraryList.stream().map(Library::getName).collect(Collectors.toList()));
        libraryComboBox.setItems(observableLibraryList);
        libraryComboBox.setValue(libraryList.get(0).getName());
    }

    public void refreshLibraryTable() {
        List<Library> libraryList = getAllLibrariesFromDatabase();
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
        } catch (DifferentPasswordException e) {
            logger.info(e.getMessage());
            System.out.println(e.getMessage());
        }
        refreshLibraryTable();
    }

    public void exportLibraryInfo() {
        String libraryName = libraryComboBox.getValue();
        Optional<Library> libraryOptional = getLibraryByNameFromDatabase(libraryName);
        if (libraryOptional.isPresent()) {
            Library library = libraryOptional.get();

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
}
