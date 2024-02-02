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
import production.model.LibraryItem;
import production.model.Movie;
import production.threads.DeleteUserThread;
import production.threads.GetBooksInLibraryThread;
import production.utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static production.utility.FileUtils.deleteUserFromFile;

public class DeleteItemViewController {

    @FXML
    TextField itemTitleField;
    @FXML
    TableView<LibraryItem> itemTable;
    @FXML
    TableColumn<LibraryItem, String> itemIdColumn;
    @FXML
    TableColumn<LibraryItem, String> itemTitleColumn;
    @FXML
    TableColumn<LibraryItem, String> itemStatusColumn;

    public void initialize() {
        itemIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LibraryItem,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LibraryItem, String> param) {
                return new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId()));
            }
        });
        itemTitleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LibraryItem,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LibraryItem, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTitle());
            }
        });
        itemStatusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LibraryItem,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LibraryItem, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getStatus());
            }
        });

        search();
    }

    public void search() {
        List<LibraryItem> itemList = new ArrayList<>();
        System.out.println(itemList);

        //itemList.sort(new TableSorter<>());
        System.out.println(itemList);

        List<Book> bookList = DatabaseUtils.getItemsInChosenLibrary(SessionManager.getCurrentLibrary(), "Book");
        List<Movie> movieList = DatabaseUtils.getItemsInChosenLibrary(SessionManager.getCurrentLibrary(), "Movie");

        itemList.addAll(bookList);
        itemList.addAll(movieList);

        String itemTitleInput = itemTitleField.getText();
        List<LibraryItem> filtereditemList;
        filtereditemList = itemList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(itemTitleInput.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableBookList =
                FXCollections.observableArrayList(filtereditemList);

        itemTable.setItems(observableBookList);
        itemTable.sort();
    }

    public void deleteItem() {
        LibraryItem item = itemTable.getSelectionModel().getSelectedItem();
        if (item != null) {
            if (item.getStatus().equals("DOSTUPNO")) {
                DatabaseUtils.deleteItemFromDatabase(item.getId());
                search();
            }
            else AlertWindow.showNotificationDialog("Nemože se obrisati", "Molimo prvo vratite sve i maknite sve rezervacije");
        }
        else AlertWindow.showNotificationDialog("Prazna tablica", "Nema Itema za izbrisati!:-(");
    }
}
