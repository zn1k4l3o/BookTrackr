package tvz.hr.booktrackr;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import production.enums.FilmRatingSystem;
import production.enums.Genre;
import production.model.Book;
import production.model.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static production.utility.DatabaseUtils.*;

public class MovieAddViewController {

    @FXML
    private TextField movieNameField;
    @FXML
    private TextField movieDirectorField;
    @FXML
    private TextArea movieDescriptionField;
    @FXML
    private Spinner<Integer> movieReleaseYearSpinner;
    @FXML
    private ComboBox<String> moviefilmRatingSystemComboBox;
    @FXML
    private TableView<Movie> tableView;
    @FXML
    private TableColumn<Movie,String> movieIdColumn;
    @FXML
    private TableColumn<Movie,String> movieTitleColumn;
    @FXML
    private TableColumn<Movie,String> movieAuthorColumn;

    public void initialize() {
        movieIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(String.valueOf(param.getValue().getId()));
            }
        });
        movieTitleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTitle());
            }
        });
        movieAuthorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getDirector());
            }
        });

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1970,2024);
        valueFactory.setValue(2000);
        movieReleaseYearSpinner.setValueFactory(valueFactory);

        List<String> filmRatinSystemList = new ArrayList<>();
        filmRatinSystemList = Arrays.stream(FilmRatingSystem.values()).map(FilmRatingSystem::name).collect(Collectors.toList());
        ObservableList observableFilmRatingSystemList =
                FXCollections.observableArrayList(filmRatinSystemList);
        moviefilmRatingSystemComboBox.setItems(observableFilmRatingSystemList);
        moviefilmRatingSystemComboBox.setValue("");

        search();
    }

    public void search() {
        List<Movie> movieList = new ArrayList<>();
        movieList = getAllMoviesFromDatabase();
        String movieNameInput = movieNameField.getText();
        List<Movie> filteredMovieList;
        filteredMovieList = movieList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(movieNameInput.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableMovieList =
                FXCollections.observableArrayList(filteredMovieList);

        tableView.setItems(observableMovieList);
    }

    public void addMovie() {
        String movieTitle = movieNameField.getText();
        String movieDescription = movieDescriptionField.getText();
        String movieDirector = movieDirectorField.getText();
        String movieFilmRatingSystem = moviefilmRatingSystemComboBox.getValue();
        Integer movieReleaseYear = movieReleaseYearSpinner.getValue();

        addMovieToDatabase(movieTitle,movieDescription, movieDirector, movieFilmRatingSystem, 0.0f, movieReleaseYear);
    }
}
