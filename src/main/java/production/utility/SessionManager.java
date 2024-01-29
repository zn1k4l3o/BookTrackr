package production.utility;

import production.model.Library;
import production.model.User;

import java.util.List;

import static production.utility.DatabaseUtils.getAllLibrariesFromDatabase;

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
        List<Library> libraryList = getAllLibrariesFromDatabase();
        currentLibrary = libraryList.stream()
                .filter(lib -> lib.getName().equals(libraryName))
                .findFirst().get();
    }

}
