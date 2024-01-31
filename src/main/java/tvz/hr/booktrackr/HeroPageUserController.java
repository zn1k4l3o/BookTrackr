package tvz.hr.booktrackr;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.model.Book;
import production.model.LibraryItem;
import production.model.Movie;
import production.utility.BorrowActions;
import production.utility.DatabaseUtils;
import production.utility.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeroPageUserController {

    @FXML
    private Label libraryNameLabel;
    @FXML
    private Label item1Name;
    @FXML
    private Label item1Date;
    @FXML
    private Label item2Name;
    @FXML
    private Label item2Date;
    @FXML
    private Label item3Name;
    @FXML
    private Label item3Date;

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public void initialize() {
        libraryNameLabel.setText(SessionManager.getCurrentLibrary().getName());

        if (SessionManager.getCurrentUser() != null) {
            List<LibraryItem> itemList = BorrowActions.getAllBorrowedItemsByUser(SessionManager.getCurrentUser());
            System.out.println(itemList);
            //Sortiranje po returnDateu

            if (itemList.size() > 0) {
                item1Name.setText(itemList.get(0).getTitle());
                item1Date.setText(itemList.get(0).getReturnDate());
            }
            if (itemList.size() > 1) {
                item2Name.setText(itemList.get(1).getTitle());
                item2Date.setText(itemList.get(1).getReturnDate());
            }
            if (itemList.size() > 2) {
                item3Name.setText(itemList.get(2).getTitle());
                item3Date.setText(itemList.get(2).getReturnDate());
            }
        }


    }

    public void switchToInventorySearch() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookSearchView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na inventorySearch");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }
}
