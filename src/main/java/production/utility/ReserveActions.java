package production.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.exception.CheckOptionalException;
import production.exception.DifferentIdException;
import production.generics.DataChange;
import production.model.*;
import production.threads.GetBorrowinInfoThread;
import production.threads.GetLibraryByNameThread;
import tvz.hr.booktrackr.App;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static production.utility.DatabaseUtils.*;
import static production.utility.UserChecking.checkIds;
import static production.utility.UserChecking.checkOptional;

public interface ReserveActions {

    static final Logger logger = LoggerFactory.getLogger(App.class);
    static void reserveItem(Long itemId, Long userId) {
        GetBorrowinInfoThread borrowingInfoThread = new GetBorrowinInfoThread(itemId);
        Thread thread = new Thread(borrowingInfoThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<BorrowInfo> borrowInfoOptional = borrowingInfoThread.getBorrowInfo();
        //Optional<BorrowInfo> borrowInfoOptional = DatabaseUtils.getBorrowingInfoForItemIdFromDatabase(itemId);
        Optional<ReservedInfo> reservedInfoOptional = DatabaseUtils.getReservedInfoForItemIdFromDatabase(itemId);
        if (borrowInfoOptional.isPresent()) {
            System.out.println("Nemože se rezervirati jer je posuđeno");
        }
        else if (reservedInfoOptional.isPresent()) {
            System.out.println("Nemože se rezervirati jer je rezervirano");
        }
        else {
            long millis=System.currentTimeMillis();
            Date dateCurrent = new java.sql.Date(millis);
            ReservedInfo reservedInfo = new ReservedInfo(-1L, itemId, userId, dateCurrent);
            addReservedItemToDatabase(reservedInfo, Boolean.FALSE);
            DataChangeWrapper dataChangeWrapper = FileUtils.readDataChangeFromFile();
            DataChange<ReservedInfo,String> dc = new DataChange<>(reservedInfo, "rezervirano");
            dataChangeWrapper.addDataChange(dc);
        }
    }

    static void cancelReservationForItem(Long itemId, Long userId) {
        Optional<ReservedInfo> reservedInfoOptional = getReservedInfoForItemIdFromDatabase(itemId);
        try {
            checkOptional(reservedInfoOptional);
            System.out.println("Bilo je rezervirano");
            ReservedInfo reservedInfo = reservedInfoOptional.get();
            try {
                //reservedInfo.userId().equals(userId)
                checkIds(reservedInfo.userId(), userId);
                deleteReservedInfoFromDatabase(itemId, Boolean.FALSE);
                addReservedItemToDatabase( reservedInfo, Boolean.TRUE);
                DataChangeWrapper dataChangeWrapper = FileUtils.readDataChangeFromFile();
                DataChange<ReservedInfo,String> dc = new DataChange<>(reservedInfo, "maknuta rezervacija");
                dataChangeWrapper.addDataChange(dc);
            } catch (DifferentIdException e) {
                logger.info(e.getMessage());
            }

        } catch (CheckOptionalException e) {
            logger.info(e.getMessage());
        }
    }

    static List<LibraryItem> getAllReservedItemsByUser(User user) {
        List<LibraryItem> itemsInCurrentLibrary = new ArrayList<>();
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
        List<ReservedInfo> reservedInfoList = getReservedInfoForUserIdFromDatabase(user.getId(), Boolean.FALSE);
        String category;
        for (ReservedInfo reservedInfo : reservedInfoList) {
            category = getItemCategory(reservedInfo.itemId());
            if (category.equals("Book")) getBookByIdFromDatabase(reservedInfo.itemId()).ifPresent(itemsInCurrentLibrary::add);
            else if (category.equals("Movie")) getMovieByIdFromDatabase(reservedInfo.itemId()).ifPresent(itemsInCurrentLibrary::add);
        }
        return itemsInCurrentLibrary;
    }
}
