package production.utility;

import production.enums.Prices;
import production.model.*;
import production.threads.GetBorrowinInfoThread;
import production.threads.GetLibraryByNameThread;
import production.threads.GetReservedInfoThread;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static production.utility.DatabaseUtils.*;
import static production.utility.ReserveActions.cancelReservationForItem;

public interface BorrowActions {

    static Long checkBorrowedStatus(Long itemId) {
        GetBorrowinInfoThread borrowingInfoThread = new GetBorrowinInfoThread(itemId);
        Thread thread = new Thread(borrowingInfoThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        GetReservedInfoThread reservedInfoThread = new GetReservedInfoThread(itemId);
        Thread thread2 = new Thread(reservedInfoThread);
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<BorrowInfo> borrowInfoOptional = borrowingInfoThread.getBorrowInfo();
        Optional<ReservedInfo> reservedInfoOptional = reservedInfoThread.getReservedInfo();
        //Optional<BorrowInfo> borrowInfoOptional = getBorrowingInfoForItemIdFromDatabase(itemId);
        //Optional<ReservedInfo> reservedInfoOptional = getReservedInfoForItemIdFromDatabase(itemId);
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
        //Optional<BorrowInfo> borrowInfoOptional = getBorrowingInfoForItemIdFromDatabase(itemId);
        //Optional<ReservedInfo> reservedInfoOptional = getReservedInfoForItemIdFromDatabase(itemId);
        GetBorrowinInfoThread borrowingInfoThread = new GetBorrowinInfoThread(itemId);
        Thread thread = new Thread(borrowingInfoThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        GetReservedInfoThread reservedInfoThread = new GetReservedInfoThread(itemId);
        Thread thread2 = new Thread(reservedInfoThread);
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<BorrowInfo> borrowInfoOptional = borrowingInfoThread.getBorrowInfo();
        Optional<ReservedInfo> reservedInfoOptional = reservedInfoThread.getReservedInfo();
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
        //Optional<Library> libraryOptional = getLibraryByNameFromDatabase(user.getLibraryName());
        GetLibraryByNameThread libraryThread = new GetLibraryByNameThread(user.getLibraryName());
        Thread thread = new Thread(libraryThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<Library> libraryOptional = libraryThread.getLibrary();
        if (libraryOptional.isEmpty()) {
            System.out.println("Problemi s knjiznicom kod usera " + user.getUsername());
            return itemsInCurrentLibrary;
        }
        List<BorrowInfo> borrowInfoList = getBorrowingInfoForUserIdFromDatabase(user.getId(), Boolean.FALSE);
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

    static BigDecimal returnItem(Long itemId) {
        //Optional<BorrowInfo> borrowInfoOptional = getBorrowingInfoForItemIdFromDatabase(itemId);
        GetBorrowinInfoThread borrowingInfoThread = new GetBorrowinInfoThread(itemId);
        Thread thread = new Thread(borrowingInfoThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<BorrowInfo> borrowInfoOptional = borrowingInfoThread.getBorrowInfo();
        BigDecimal lateReturnPenalty = BigDecimal.ZERO;
        if (borrowInfoOptional.isPresent()) {
            long millis=System.currentTimeMillis();
            Date dateCurrent = new java.sql.Date(millis);
            BorrowInfo b = borrowInfoOptional.get();
            lateReturnPenalty = getBorrowDifference(b);
            addBorrowedItemToDatabase(new BorrowInfo(-1L, b.itemId(), b.userId(), b.borrowDate(), dateCurrent), Boolean.TRUE);
            deleteBorrowedInfoFromDatabase(itemId, Boolean.FALSE);
        }
        return lateReturnPenalty;
    }

    static BigDecimal getBorrowDifference(BorrowInfo borrowInfo) {
        String category = getItemCategory(borrowInfo.itemId());
        long millis=System.currentTimeMillis();
        Date dateCurrent = new java.sql.Date(millis);
        Date supposedReturnDate = borrowInfo.returnDate();
        LocalDate localDate1 = dateCurrent.toLocalDate();
        LocalDate localDate2 = supposedReturnDate.toLocalDate();
        long daysDifference = ChronoUnit.DAYS.between(localDate2, localDate1);
        System.out.println(daysDifference);
        if (daysDifference <= 0) return BigDecimal.ZERO;
        else  {
            if (category.equals("Book")) return (Prices.DAY_LATE_FEE_BOOK.getAmount().multiply(BigDecimal.valueOf(daysDifference)));
            else if (category.equals("Movie")) return (Prices.DAY_LATE_FEE_MOVIE.getAmount().multiply(BigDecimal.valueOf(daysDifference)));
        }
        return BigDecimal.ZERO;
    }
}
