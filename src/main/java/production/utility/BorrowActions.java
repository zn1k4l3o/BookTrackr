package production.utility;

import production.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static production.utility.DatabaseUtils.*;
import static production.utility.ReserveActions.cancelReservationForItem;

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

    static Long checkBorrowedStatusWithId(Long itemId, Long userId) {
        Optional<BorrowInfo> borrowInfoOptional = getBorrowingInfoForItemIdFromDatabase(itemId);
        Optional<ReservedInfo> reservedInfoOptional = getReservedInfoForItemIdFromDatabase(itemId);
        //ako je rezervirano vraća -2L
        if (borrowInfoOptional.isPresent()) {
            BorrowInfo borrowInfo = borrowInfoOptional.get();
            return borrowInfo.userId();
        }
        else if (reservedInfoOptional.isPresent()) {
            ReservedInfo reservedInfo = reservedInfoOptional.get();
            if (reservedInfo.userId().equals(userId)) return -2L;
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

    static void borrowItem(Long itemId, Long userId) {
        Long resultId = checkBorrowedStatusWithId(itemId, userId);
        if (resultId < 0) {
            if (resultId.equals(-2L)) {
                cancelReservationForItem(itemId, userId);
            }
            long millis=System.currentTimeMillis();
            Date dateCurrent = new java.sql.Date(millis);
            LocalDate localDate = dateCurrent.toLocalDate();
            LocalDate futureDate = localDate.plusDays(15);
            java.sql.Date supposedReturnDate = java.sql.Date.valueOf(futureDate);
            addBorrowedItemToDatabase(new BorrowInfo(-1L, itemId, userId, dateCurrent, supposedReturnDate), Boolean.FALSE);
        }

    }

    static void returnItem(Long itemId) {
        Optional<BorrowInfo> borrowInfoOptional = getBorrowingInfoForItemIdFromDatabase(itemId);
        if (borrowInfoOptional.isPresent()) {
            long millis=System.currentTimeMillis();
            Date dateCurrent = new java.sql.Date(millis);
            BorrowInfo b = borrowInfoOptional.get();
            addBorrowedItemToDatabase(new BorrowInfo(-1L, b.itemId(), b.userId(), b.borrowDate(), dateCurrent), Boolean.TRUE);
            deleteBorrowedInfoFromDatabase(itemId);
        }
    }
}
