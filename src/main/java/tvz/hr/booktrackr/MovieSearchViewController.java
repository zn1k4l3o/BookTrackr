package tvz.hr.booktrackr;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import production.model.Book;
import production.model.Movie;

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

    }
}
