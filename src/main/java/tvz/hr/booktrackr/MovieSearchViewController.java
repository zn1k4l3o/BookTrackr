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
import production.model.Book;
import production.model.Movie;
import production.threads.GetBooksInLibraryThread;
import production.threads.GetMoviesInLibraryThread;
import production.utility.ItemMemory;
import production.utility.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static production.utility.DatabaseUtils.getAllBooksFromDatabase;
import static production.utility.DatabaseUtils.getAllMoviesFromDatabase;

public class MovieSearchViewController {

    @FXML
    TextField movieNameField;
    @FXML
    TableView<Movie> movieTable;
    @FXML
    TableColumn<Movie, String> movieNameColumn;
    @FXML
    TableColumn<Movie, String> movieDescriptionColumn;
    @FXML
    TableColumn<Movie, String> movieStatusColumn;

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void initialize() {
        movieNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTitle());
            }
        });
        movieDescriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getDescription());
            }
        });
        movieStatusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getStatus());
            }
        });

        search();
    }

    public void search() {
        String movieName = movieNameField.getText();
        List<Movie> movieList;
        GetMoviesInLibraryThread moviesThread = new GetMoviesInLibraryThread(SessionManager.getCurrentLibrary());
        Thread thread = new Thread(moviesThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        movieList = moviesThread.getMovieList();
        List<Movie> filteredMovieList;
        filteredMovieList = movieList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(movieName.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableMovieList =
                FXCollections.observableArrayList(filteredMovieList);

        movieTable.setItems(observableMovieList);
    }

    public void bookReserveInfo() {
        Movie chosenMovie = movieTable.getSelectionModel().getSelectedItem();
        if (chosenMovie != null) {
            ItemMemory.setRememberedMovie(chosenMovie);
            switchToMovieReservePage();
        }

    }

    public void switchToMovieReservePage() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("movieReservePage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na movie reserve view user");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

}
