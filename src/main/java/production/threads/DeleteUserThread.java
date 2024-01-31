package production.threads;

import production.model.User;

public class DeleteUserThread extends DatabaseThread implements Runnable {
    private User user;
    @Override
    public void run() {
        super.deleteUserFromDB(user);
    }

    public DeleteUserThread(User user) {
        this.user = user;
    }
}
