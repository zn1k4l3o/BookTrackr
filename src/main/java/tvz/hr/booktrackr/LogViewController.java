package tvz.hr.booktrackr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.utility.DataChangeWrapper;
import production.utility.FileUtils;

import java.io.IOException;
import java.util.List;

public class LogViewController {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @FXML
    ListView<String> logListView;
    private DataChangeWrapper dataChangeWrapper;

    public void initialize() {
        dataChangeWrapper = FileUtils.readDataChangeFromFile();
        List<String> changeStringList = dataChangeWrapper.getStringifiedChanges();
        ObservableList<String> observableChangeStringList = FXCollections.observableArrayList(changeStringList);
        logListView.setItems(observableChangeStringList);
    }

    public void switchToHeroPageAdmin() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("heroPageAdmin.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Prebačeno na heroPageAdmin");
        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

}
