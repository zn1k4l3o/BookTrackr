package production.model;

import java.io.Serializable;
import java.sql.Date;

public record BorrowInfo(Long id, Long itemId, Long userId, Date borrowDate, Date returnDate)  implements Serializable {

    @Override
    public String toString() {
        return "UserId " + userId.toString() + " posudio ItemId " + itemId.toString();
    }
}
