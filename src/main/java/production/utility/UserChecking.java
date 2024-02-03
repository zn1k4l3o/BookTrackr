package production.utility;

import production.exception.CheckPasswordException;
import production.exception.DifferentIdException;
import production.exception.ExistingUserException;
import production.model.User;

import java.util.List;
import java.util.Optional;

import static production.utility.FileUtils.getUsersFromFile;

public interface UserChecking {

    static void checkExistingUser(String username) throws ExistingUserException {
        if (doesUsernameExist(username)) {
            throw new ExistingUserException("Korisnicko ime vec postoji u bazi.");
        }
    }

    static void checkPasswords(String password, String checkPassword) throws CheckPasswordException {
        if (!password.equals(checkPassword)) {
            throw new CheckPasswordException("Lozinka se ne podudara");
        }
    }
    
    static Boolean doesUsernameExist(String username) {
        List<User> usersShort = getUsersFromFile();
        for (User user : usersShort) {
            if (user.getUsername().equals(username)) return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    static <T> void checkOptional(Optional<T> optional) throws CheckPasswordException {
        if (optional.isEmpty()) {
            throw new CheckPasswordException("Optional prazan");
        }
    }

    static void checkIds(Long id1, Long id2) {
        if (id1.compareTo(id2) != 0) throw new DifferentIdException("Nisu isti IDevi");
    }
}
