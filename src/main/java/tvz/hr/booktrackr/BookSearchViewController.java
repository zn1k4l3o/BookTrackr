package tvz.hr.booktrackr;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.generics.TableSorter;
import production.model.Book;
import production.model.LibraryItem;
import production.threads.GetBooksInLibraryThread;
import production.threads.GetBooksThread;
import production.utility.DatabaseUtils;
import production.utility.ItemMemory;
import production.utility.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BookSearchViewController {

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

    private static final Logger logger = LoggerFactory.getLogger(App.class);

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

        GetBooksInLibraryThread booksThread = new GetBooksInLibraryThread(SessionManager.getCurrentLibrary());
        Thread thread = new Thread(booksThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        bookList = booksThread.getBookList();
        System.out.println(bookList);

        //bookList.sort(new TableSorter<>());
        System.out.println(bookList);

        //bookList = DatabaseUtils.getItemsInChosenLibrary(SessionManager.getCurrentLibrary(), "Book");

        String bookNameInput = bookNameField.getText();
        List<Book> filteredBookList;
        filteredBookList = bookList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(bookNameInput.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableBookList =
                FXCollections.observableArrayList(filteredBookList);

        bookTable.setItems(observableBookList);
        bookTable.sort();
    }

    public void bookReserveInfo() {
        Book chosenBook = bookTable.getSelectionModel().getSelectedItem();
        if (chosenBook != null) {
            ItemMemory.setRememberedBook(chosenBook);
            switchToBookReservePage();
        }

    }

    public void switchToBookReservePage() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookReservePage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na book reserve view user");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }
}
