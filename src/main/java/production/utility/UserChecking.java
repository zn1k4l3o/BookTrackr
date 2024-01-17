package production.utility;

import production.exception.DifferentPasswordException;
import production.exception.ExistingUserException;
import production.model.User;

import java.util.List;

import static production.utility.FileUtils.getUsersFromFile;

public abstract class UserChecking {

    public static void checkExistingUser(String username) throws ExistingUserException {
        if (doesUsernameExist(username)) {
            throw new ExistingUserException("Korisnicko ime vec postoji u bazi.");
        }
    }

    public static void checkPasswords(String password, String checkPassword) throws DifferentPasswordException {
        if (!password.equals(checkPassword)) {
            throw new DifferentPasswordException("Lozinka se ne podudara");
        }
    }
    
    public static Boolean doesUsernameExist(String username) {
        List<User> usersShort = getUsersFromFile();
        for (User user : usersShort) {
            if (user.getUsername().equals(username)) return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
