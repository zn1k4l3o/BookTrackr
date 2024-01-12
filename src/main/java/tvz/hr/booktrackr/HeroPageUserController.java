package tvz.hr.booktrackr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.utility.SessionManager;

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
    public void switchToInventorySearch() {
       logger.info("Prebačeno na inventorySearch");
    }
}
