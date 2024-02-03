package tvz.hr.booktrackr;

import javafx.application.Platform;
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
import production.model.Book;
import production.threads.GetBooksInLibraryThread;
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

    List<Book> bookList = new ArrayList<>();

    private ScheduledExecutorService executorService;
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

        executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(this::updateBookList, 0, 5, TimeUnit.SECONDS);

    }

    private void updateBookList() {
        GetBooksInLibraryThread booksThread = new GetBooksInLibraryThread(SessionManager.getCurrentLibrary());
        Thread thread = new Thread(booksThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        bookList = booksThread.getBookList();

        this.search();
    }

    public void search() {

        String bookNameInput = bookNameField.getText();
        String bookGenreInput = bookGenreField.getText();
        String bookAuthorInput = bookAuthorField.getText();

        List<Book> filteredBookList;
        filteredBookList = bookList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(bookNameInput.toLowerCase()))
                .filter(c -> c.getGenre().toLowerCase().contains(bookGenreInput.toLowerCase()))
                .filter(c -> c.getAuthor().toLowerCase().contains(bookAuthorInput.toLowerCase()))
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
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na book reserve view user");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }
}
