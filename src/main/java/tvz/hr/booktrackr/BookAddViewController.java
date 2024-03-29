package tvz.hr.booktrackr;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.enums.Genre;
import production.generics.DataChange;
import production.model.Book;
import production.model.ReservedInfo;
import production.model.User;
import production.model.Worker;
import production.threads.AddBookThread;
import production.threads.GetBooksInLibraryThread;
import production.utility.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BookAddViewController {

    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private ComboBox<String> bookGenreCombobox;
    @FXML
    private TextField publisherField;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book,String> bookIdColumn;
    @FXML
    private TableColumn<Book,String> bookTitleColumn;
    @FXML
    private TableColumn<Book,String> bookAuthorColumn;
    @FXML
    private TableColumn<Book,String> bookGenreColumn;

    List<Book> bookList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void initialize() {
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
        bookGenreColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getGenre());
            }
        });

        List<String> genreList = new ArrayList<>();
        genreList = Arrays.stream(Genre.values()).toList().stream().map(Genre::name).collect(Collectors.toList());
        ObservableList observableGenreList =
                FXCollections.observableArrayList(genreList);
        bookGenreCombobox.setItems(observableGenreList);
        bookGenreCombobox.setValue("");

        ScheduledExecutorService executorService;

        executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(this::updateBookList, 0, 5, TimeUnit.SECONDS);

    }

    public void search() {
        bookList = DatabaseUtils.getItemsInChosenLibrary(SessionManager.getCurrentLibrary(), "Book");
        String bookNameInput = bookTitleField.getText();
        List<Book> filteredBookList;
        filteredBookList = bookList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(bookNameInput.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableBookList =
                FXCollections.observableArrayList(filteredBookList);

        tableView.setItems(observableBookList);
    }

    public void addBook() {
        String bookTitle = bookTitleField.getText();
        String bookAuthor = bookAuthorField.getText();
        String bookGenre = bookGenreCombobox.getValue();
        String bookPublisher = publisherField.getText();

        if (!bookTitle.isBlank() && !bookAuthor.isBlank() && !bookGenre.isBlank() && !bookPublisher.isBlank()) {
            bookTitleField.setText("");
            bookAuthorField.setText("");
            bookGenreCombobox.setValue("");
            publisherField.setText("");
            AddBookThread bookThread = new AddBookThread(bookTitle, bookGenre, bookPublisher, bookAuthor, 0f);
            Thread thread = new Thread(bookThread);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Book book = new Book.Builder(bookTitle).withPublisher(bookPublisher).withGenre(bookGenre).withAuthor(bookAuthor).withRating(0f).build();
            DataChangeWrapper dataChangeWrapper = FileUtils.readDataChangeFromFile();
            DataChange<Worker, Book> dc = new DataChange<>((Worker)SessionManager.getCurrentUser(), book);
            dataChangeWrapper.addDataChange(dc);
            System.out.println("Knjiga dodana");
        }
        else AlertWindow.showNotificationDialog("Nepotpun unos", "Molimo unesite sav info za knjigu");
        search();
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
}
