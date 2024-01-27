package production.utility;

import production.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static production.utility.DatabaseUtils.*;

public interface BorrowActions {

    static Long checkBorrowedStatus(Long itemId) {
        Optional<BorrowInfo> borrowInfoOptional = getBorrowingInfoForItemIdFromDatabase(itemId);
        Optional<ReservedInfo> reservedInfoOptional = getReservedInfoForItemIdFromDatabase(itemId);
        //ako je rezervirano vraća -2L
        if (borrowInfoOptional.isPresent()) {
            BorrowInfo borrowInfo = borrowInfoOptional.get();
            return borrowInfo.userId();
        }
        else if (reservedInfoOptional.isPresent()) {
            ReservedInfo reservedInfo = reservedInfoOptional.get();
            if (reservedInfo.userId().equals(SessionManager.getCurrentUser().getId())) return -2L;
            else return reservedInfo.userId();
        }
        else return -1L;
    }

    static List<LibraryItem> getAllBorrowedItemsByUser(User user) {
        List<LibraryItem> itemsInCurrentLibrary = new ArrayList<>();
        Optional<Library> libraryOptional = getLibraryByNameFromDatabase(user.getLibraryName());
        if (libraryOptional.isEmpty()) {
            System.out.println("Problemi s knjiznicom kod usera " + user.getUsername());
            return itemsInCurrentLibrary;
        }
        List<BorrowInfo> borrowInfoList = getBorrowingInfoForUserIdFromDatabase(user.getId());
        String category;
        for (BorrowInfo borrowInfo : borrowInfoList) {
            category = getItemCategory(borrowInfo.itemId());
            if (category.equals("Book")) getBookByIdFromDatabase(borrowInfo.itemId()).ifPresent(itemsInCurrentLibrary::add);
            else if (category.equals("Movie")) getMovieByIdFromDatabase(borrowInfo.itemId()).ifPresent(itemsInCurrentLibrary::add);
        }
        return itemsInCurrentLibrary;
    }
}
