package production.threads;

import production.model.Library;
import production.model.Movie;

import java.util.List;
import java.util.Optional;

public class GetLibraryByNameThread extends DatabaseThread implements Runnable{

    private Optional<Library> library;
    private String libraryName;

    public GetLibraryByNameThread(String libraryName) {
        this.libraryName = libraryName;
    }
    @Override
    public void run() {
        this.library = super.getLibraryByNameFromDB(libraryName);
    }

    public Optional<Library> getLibrary() {
        return library;
    }

}