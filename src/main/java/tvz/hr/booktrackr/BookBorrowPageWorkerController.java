package tvz.hr.booktrackr;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import production.model.Book;
import production.model.User;
import production.model.Worker;
import production.utility.DatabaseUtils;
import production.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static production.utility.BorrowActions.borrowItem;
import static production.utility.BorrowActions.returnItem;
import static production.utility.ReserveActions.cancelReservationForItem;
import static production.utility.ReserveActions.reserveItem;

public class BookBorrowPageWorkerController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField titleField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button reserveButton;
    @FXML
    private Button borrowButton;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> userIdColumn;
    @FXML
    private TableColumn<User, String> userUsernameColumn;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> bookIdColumn;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, String> bookAuthorColumn;

    private User user;
    private Book book;

    public void initialize() {
        userIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId()));
            }
        });
        userUsernameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getUsername());
            }
        });
        bookIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId()));
            }
        });
        bookTitleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTitle());
            }
        });
        bookAuthorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getAuthor());
            }
        });

        searchUsers();
        searchBooks();
    }

    public void searchBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList = DatabaseUtils.getItemsInChosenLibrary(SessionManager.getCurrentLibrary(), "Book");

        String bookNameInput = titleField.getText();
        List<Book> filteredBookList;
        filteredBookList = bookList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(bookNameInput.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableBookList =
                FXCollections.observableArrayList(filteredBookList);

        bookTable.setItems(observableBookList);
    }

    public void searchUsers() {
        List<User> userList = new ArrayList<>();
        userList = DatabaseUtils.getAllUsersFromDatabase();
        userList = userList.stream().filter(lib -> lib.getLibraryName()
                .equals(SessionManager.getCurrentLibrary().getName()))
                .collect(Collectors.toList());

        String userUsernameInput = usernameField.getText();
        List<User> filteredUserList = new ArrayList<>();
        for (User u : userList) {
            if (!(u instanceof Worker)) filteredUserList.add(u);
        }
        filteredUserList = filteredUserList.stream()
                .filter(c -> c.getUsername().toLowerCase().contains(userUsernameInput.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableUserList =
                FXCollections.observableArrayList(filteredUserList);

        userTable.setItems(observableUserList);
    }


    public void reserveBook() {
        updateChoice();
        if (reserveButton.getText().equals("REZERVIRAJ")) {
            reserveItem(book.getId(), user.getId());
        }
        else if (reserveButton.getText().equals("OTKAŽI")) {
            cancelReservationForItem(book.getId(), user.getId());
        }
        statusLabel.setText(book.getStatus());
        checkActionButton();
    }

    public void borrowBook() {
        updateChoice();
        user = userTable.getSelectionModel().getSelectedItem();
        book = bookTable.getSelectionModel().getSelectedItem();
        if (borrowButton.getText().equals("VRATI")) {
            returnItem(book.getId());
        }
        else if (borrowButton.getText().equals("POSUDI")) {
            borrowItem(book.getId(), user.getId());
        }
        checkActionButton();
    }


    private void checkActionButton() {
        reserveButton.setDisable(false);
        borrowButton.setDisable(false);
        statusLabel.setText(book.getStatusWithUserId(user.getId()));
        if (user == null) {
            System.out.println("Nije odabran user");
        }
        else if (book.getStatusWithUserId(user.getId()).equals("DOSTUPNO")) {
            reserveButton.setText("REZERVIRAJ");
            borrowButton.setText("POSUDI");
        }
        else if (book.getStatusWithUserId(user.getId()).equals("REZERVIRANO")) {
            reserveButton.setText("OTKAŽI");
            borrowButton.setText("POSUDI");
        }
        else if (book.getStatusWithUserId(user.getId()).equals("NEDOSTUPNO")) {
            reserveButton.setText("REZERVIRAJ");
            borrowButton.setText("POSUDI");
            reserveButton.setDisable(true);
            borrowButton.setDisable(true);
        }
        else if (book.getStatusWithUserId(user.getId()).equals("POSUĐENO")) {
            reserveButton.setText("REZERVIRAJ");
            borrowButton.setText("VRATI");
            reserveButton.setDisable(true);
        }
    }

    public void updateChoice() {
        user = userTable.getSelectionModel().getSelectedItem();
        book = bookTable.getSelectionModel().getSelectedItem();
        if (book != null && user != null) {
            checkActionButton();
        }
    }

}
