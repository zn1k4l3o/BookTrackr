package tvz.hr.booktrackr;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import production.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static production.utility.DatabaseUtils.getAllBooksFromDatabase;

public class BookSearchWorkerViewController {

    @FXML
    TextField bookNameField;
    @FXML
    TextField bookAuthorField;
    @FXML
    TextField bookGenreField;
    @FXML
    TableView<Book> bookTable;
    @FXML
    TableColumn<Book, String> bookNameColumn;
    @FXML
    TableColumn<Book, String> bookAuthorColumn;
    @FXML
    TableColumn<Book, String> bookGenreColumn;
    @FXML
    TableColumn<Book, String> bookStatusColumn;

    public void initialize() {
        bookNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTitle());
            }
        });
        bookAuthorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getAuthor());
            }
        });
        bookGenreColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getGenre());
            }
        });
        bookStatusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getStatus());
            }
        });

        search();
    }

    public void search() {
        List<Book> bookList = new ArrayList<>();
        bookList = getAllBooksFromDatabase();
        String bookNameInput = bookNameField.getText();
        List<Book> filteredBookList;
        filteredBookList = bookList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(bookNameInput.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableBookList =
                FXCollections.observableArrayList(filteredBookList);

        bookTable.setItems(observableBookList);
    }

}
