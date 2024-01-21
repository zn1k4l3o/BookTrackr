package tvz.hr.booktrackr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import production.utility.SessionManager;
import tvz.hr.booktrackr.App;

import java.io.IOException;

public class MenuBarUserController {

    public void switchToBookInventoryScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookSearchView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();

    }

    public void switchToMovieInventoryScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("movieSearchView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();

    }

    public void switchToStatisticsScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("statisticsView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();

    }

    public void logOutUser() {
        SessionManager.setCurrentUser(null);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("loginView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();
    }
}
