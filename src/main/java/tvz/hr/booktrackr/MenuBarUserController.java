package tvz.hr.booktrackr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import production.model.Library;
import production.model.LibraryItem;
import production.threads.DatabaseThread;
import production.threads.DeleteUserThread;
import production.threads.GetBooksInLibraryThread;
import production.utility.AlertWindow;
import production.utility.BorrowActions;
import production.utility.ReserveActions;
import production.utility.SessionManager;
import tvz.hr.booktrackr.App;

import java.io.IOException;
import java.util.List;

import static production.utility.DatabaseUtils.deleteUserFromDatabase;
import static production.utility.FileUtils.deleteUserFromFile;

public class MenuBarUserController {

    public void switchToBookInventoryScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookSearchView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
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
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();

    }

    public void deleteUser() {
        List<LibraryItem> allBorrowedItems = BorrowActions.getAllBorrowedItemsByUser(SessionManager.getCurrentUser());
        if (allBorrowedItems.isEmpty()) {
            List<LibraryItem> allReservedItems = ReserveActions.getAllReservedItemsByUser(SessionManager.getCurrentUser());
            if (allReservedItems.isEmpty()) {
                deleteUserFromFile(SessionManager.getCurrentUser());

                DeleteUserThread deleteUserThread = new DeleteUserThread(SessionManager.getCurrentUser());
                Thread thread = new Thread(deleteUserThread);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                logOutUser();
            }
            else AlertWindow.showNotificationDialog("Nemože se obrisati user", "Molimo prvo maknite sve rezervacije");
        }
        else AlertWindow.showNotificationDialog("Nemože se obrisati user", "Molimo prvo vratite sve");
    }

    public void logOutUser() {
        SessionManager.setCurrentUser(null);
        SessionManager.setCurrentLibrary(null);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("loginView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();
    }

    public void editUserInfo() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("editUserInfoView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
            scene.getStylesheets().add("styles.css");
        } catch (IOException e) {
            e.printStackTrace();
        }

        App.mainStage.setScene(scene);
        App.mainStage.show();
    }
}
