package tvz.hr.booktrackr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    public void switchToInventorySearch() {
        System.out.println("prebačeno na inventar");
    }
}
