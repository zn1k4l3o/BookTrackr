package tvz.hr.booktrackr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import production.utility.SessionManager;

import java.io.IOException;

public class MenuBarWorkerController {

    public void switchToBookInventoryScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookSearchWorkerView.fxml"));
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

    public void addNewBookScreen () {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookAddView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();

    }

    public void borrowBookPage () {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookBorrowPageWorker.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();

    }

    public void borrowMoviePage () {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("movieBorrowPageWorker.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();

    }

    public void addNewMovieScreen () {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("movieAddView.fxml"));
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
