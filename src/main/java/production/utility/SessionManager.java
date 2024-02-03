package production.utility;

import production.model.Library;
import production.model.User;
import production.threads.GetAllLibrariesThread;

import java.util.List;


public abstract class SessionManager {

    private static User currentUser;

    private static Library currentLibrary;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static Library getCurrentLibrary() {
        return currentLibrary;
    }

    public static void setCurrentLibrary(Library library) {
        currentLibrary = library;
    }

    public static void setCurrentLibraryByName(String libraryName) {
        GetAllLibrariesThread librariesThread = new GetAllLibrariesThread();
        Thread thread = new Thread(librariesThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Library> libraryList = librariesThread.getLibraryList();
        currentLibrary = libraryList.stream()
                .filter(lib -> lib.getName().equals(libraryName))
                .findFirst().get();
    }

}
