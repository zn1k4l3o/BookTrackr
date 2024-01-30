package production.threads;

import production.model.Library;

import java.util.List;

public class GetAllLibrariesThread extends DatabaseThread implements Runnable{

    private List<Library> libraryList;

    @Override
    public void run() {
        this.libraryList = super.getAllLibrariesFromDB();
    }

    public List<Library> getLibraryList() {
        return libraryList;
    }

}
