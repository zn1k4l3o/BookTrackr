package tvz.hr.booktrackr;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.model.Book;
import production.model.Movie;
import production.utility.ItemMemory;
import production.utility.SessionManager;

import java.io.IOException;

import static production.utility.ReserveActions.cancelReservationForItem;
import static production.utility.ReserveActions.reserveItem;

public class MovieReservePageController {

    @FXML
    private Label movieTitle;
    @FXML
    private Label movieDirector;
    @FXML
    private Label movieDescription;
    @FXML
    private Label movieReleaseYear;
    @FXML
    private Label movieFilmRatingSystem;
    @FXML
    private Label movieStatus;
    @FXML
    private Button actionButton;

    private Movie movie;
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void initialize() {
        movie = ItemMemory.getRememberedMovie();

        movieTitle.setText(movie.getTitle());
        movieDirector.setText(movie.getDirector());
        movieDescription.setText(movie.getDescription());
        movieReleaseYear.setText(String.valueOf(movie.getReleaseYear()));
        movieFilmRatingSystem.setText(movie.getFilmRatingSystem());
        movieStatus.setText(movie.getStatus());

        checkActionButton();
    }

    public void reserveMovie() {
        if (actionButton.getText().equals("REZERVIRAJ")) {
            reserveItem(movie.getId(), SessionManager.getCurrentUser().getId());
        }
        else if (actionButton.getText().equals("OTKAŽI")) {
            cancelReservationForItem(movie.getId(), SessionManager.getCurrentUser().getId());
        }
        movieStatus.setText(movie.getStatus());
        checkActionButton();
    }

    public void switchToMovieSearchView() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("movieSearchView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na book search view user");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

    private void checkActionButton() {
        if (movie.getStatus().equals("DOSTUPNO")) {
            actionButton.setText("REZERVIRAJ");
        }
        else if (movie.getStatus().equals("REZERVIRANO")) {
            actionButton.setText("OTKAŽI");
        }
        else if (movie.getStatus().equals("NEDOSTUPNO") || movie.getStatus().equals("POSUĐENO")) {
            actionButton.setText("REZERVIRAJ");
            actionButton.setDisable(true);
        }
    }

}
