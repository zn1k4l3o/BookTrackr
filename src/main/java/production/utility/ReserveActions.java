package production.utility;

import production.model.BorrowInfo;
import production.model.ReservedInfo;

import java.sql.Date;
import java.util.Optional;

import static production.utility.DatabaseUtils.*;

public interface ReserveActions {

    static void reserveItem(Long itemId, Long userId) {
        Optional<BorrowInfo> borrowInfoOptional = DatabaseUtils.getBorrowingInfoForItemIdFromDatabase(itemId);
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
            addReservedItemToDatabase(new ReservedInfo(-1L, itemId, userId, dateCurrent), Boolean.FALSE);
        }
    }

    static void cancelReservationForItem(Long itemId, Long userId) {
        Optional<ReservedInfo> reservedInfoOptional = getReservedInfoForItemIdFromDatabase(itemId);
        if (reservedInfoOptional.isPresent()) {
            System.out.println("Bilo je rezervirano");
            ReservedInfo reservedInfo = reservedInfoOptional.get();
            if (reservedInfo.userId().equals(userId)) {
                deleteReservedInfoFromDatabase(itemId);
                addReservedItemToDatabase( reservedInfo, Boolean.TRUE);
            }

        }
    }
}
