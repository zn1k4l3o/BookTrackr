package tvz.hr.booktrackr;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import production.model.Movie;
import production.model.User;
import production.model.Worker;
import production.utility.AlertWindow;
import production.utility.DatabaseUtils;
import production.utility.SessionManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static production.utility.BorrowActions.borrowItem;
import static production.utility.BorrowActions.returnItem;
import static production.utility.ReserveActions.cancelReservationForItem;
import static production.utility.ReserveActions.reserveItem;

public class MovieBorrowPageWorkerController {

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
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> movieIdColumn;
    @FXML
    private TableColumn<Movie, String> movieTitleColumn;
    @FXML
    private TableColumn<Movie, String> movieDirectorColumn;

    private User user;
    private Movie movie;

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
        movieDirectorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Movie,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Movie, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getDirector());
            }
        });

        searchUsers();
        searchMovies();

        borrowButton.setDisable(true);
        reserveButton.setDisable(true);
    }

    public void searchMovies() {
        List<Movie> movieList = new ArrayList<>();
        movieList = DatabaseUtils.getItemsInChosenLibrary(SessionManager.getCurrentLibrary(), "Movie");

        String movieNameInput = titleField.getText();
        List<Movie> filteredMovieList;
        filteredMovieList = movieList.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(movieNameInput.toLowerCase()))
                .collect(Collectors.toList());

        ObservableList observableMovieList =
                FXCollections.observableArrayList(filteredMovieList);

        movieTable.setItems(observableMovieList);
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


    public void reserveMovie() {
        updateChoice();
        if (reserveButton.getText().equals("REZERVIRAJ")) {
            reserveItem(movie.getId(), user.getId());
        }
        else if (reserveButton.getText().equals("OTKAŽI")) {
            cancelReservationForItem(movie.getId(), user.getId());
        }
        statusLabel.setText(movie.getStatus());
        checkActionButton();
    }

    public void borrowMovie() {
        updateChoice();
        user = userTable.getSelectionModel().getSelectedItem();
        movie = movieTable.getSelectionModel().getSelectedItem();
        if (borrowButton.getText().equals("VRATI")) {
            BigDecimal returnPenalty = returnItem(movie.getId());
            if (returnPenalty.compareTo(BigDecimal.ZERO) > 0) AlertWindow.showNotificationDialog("Zakašnjelo vraćanje","Korisnik duguje " + String.valueOf(returnPenalty) + "eur. ");
            else AlertWindow.showNotificationDialog("Uspješno vraćeno","Film je uspješno vraćen!");
        }
        else if (borrowButton.getText().equals("POSUDI")) {
            borrowItem(movie.getId(), user.getId());
        }
        checkActionButton();
    }


    private void checkActionButton() {
        reserveButton.setDisable(false);
        borrowButton.setDisable(false);
        statusLabel.setText(movie.getStatusWithUserId(user.getId()));
        if (user == null) {
            System.out.println("Nije odabran user");
        }
        else if (movie.getStatusWithUserId(user.getId()).equals("DOSTUPNO")) {
            reserveButton.setText("REZERVIRAJ");
            borrowButton.setText("POSUDI");
        }
        else if (movie.getStatusWithUserId(user.getId()).equals("REZERVIRANO")) {
            reserveButton.setText("OTKAŽI");
            borrowButton.setText("POSUDI");
        }
        else if (movie.getStatusWithUserId(user.getId()).equals("NEDOSTUPNO")) {
            reserveButton.setText("REZERVIRAJ");
            borrowButton.setText("POSUDI");
            reserveButton.setDisable(true);
            borrowButton.setDisable(true);
        }
        else if (movie.getStatusWithUserId(user.getId()).equals("POSUĐENO")) {
            reserveButton.setText("REZERVIRAJ");
            borrowButton.setText("VRATI");
            reserveButton.setDisable(true);
        }
    }

    public void updateChoice() {
        user = userTable.getSelectionModel().getSelectedItem();
        movie = movieTable.getSelectionModel().getSelectedItem();
        if (movie != null && user != null) {
            checkActionButton();
        }
    }

}
