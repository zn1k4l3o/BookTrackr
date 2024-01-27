package tvz.hr.booktrackr;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.model.Book;
import production.utility.ItemMemory;

import java.io.IOException;

public class BookReservePageController {

    @FXML
    private Label bookTitle;
    @FXML
    private Label bookAuthor;
    @FXML
    private Label bookGenre;
    @FXML
    private Label bookPublisher;
    @FXML
    private Label bookStatus;
    @FXML
    private Button actionButton;

    private Book book;
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void initialize() {
        book = ItemMemory.getRememberedBook();

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookGenre.setText(book.getGenre());
        bookPublisher.setText(book.getPublisher());
        bookStatus.setText(book.getStatus());

        checkActionButton();
    }

    public void reserveBook() {
        if (actionButton.getText().equals("REZERVIRAJ")) {
            //srediti rezervaciju
        }
        else if (actionButton.getText().equals("OTKAŽI")) {
            //srediti otkazivanje rezervacije
        }

        checkActionButton();
    }

    public void switchToBookSearchView() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookSearchView.fxml"));
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
        if (book.getStatus().equals("DOSTUPNO")) {
            actionButton.setText("REZERVIRAJ");
        }
        else if (book.getStatus().equals("REZERVIRANO")) {
            actionButton.setText("OTKAŽI");
        }
        else if (book.getStatus().equals("NEDOSTUPNO") || book.getStatus().equals("POSUĐENO")) {
            actionButton.setText("REZERVIRAJ");
            actionButton.setDisable(true);
        }
    }

}
