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
import production.model.Movie;

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
        movieList = getAllMoviesFromDatabase();

        List<Movie> filteredMovieList;
        filteredMovieList = movieList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(movieName.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableMovieList =
                FXCollections.observableArrayList(filteredMovieList);

        movieTable.setItems(observableMovieList);
    }

}
