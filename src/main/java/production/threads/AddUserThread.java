package production.threads;

public class AddUserThread extends DatabaseThread implements Runnable{

    String username;
    String hashedPassword;
    String name;
    String lastName;
    Long libraryId;
    Boolean isWorker;

    public AddUserThread(String username, String hashedPassword, String name, String lastName, Long libraryId, Boolean isWorker) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.lastName = lastName;
        this.libraryId = libraryId;
        this.isWorker = isWorker;
    }
    @Override
    public void run() {
        super.addUserToDB(username, hashedPassword, name, lastName, libraryId, isWorker);
    }
}
