package production.threads;

import production.model.Library;

import java.util.ArrayList;
import java.util.List;

public class GetAllLibrariesThread extends DatabaseThread implements Runnable{

    private List<Library> libraryList;

    @Override
    public void run() {
        libraryList = new ArrayList<>();
        this.libraryList = super.getAllLibrariesFromDB();
    }

    public List<Library> getLibraryList() {
        return libraryList;
    }

}
