package production.model;

import java.sql.Date;

public record ReservedInfo(Long id, Long itemId, Long userId, Date reservedDate) {

    @Override
    public String toString() {
        return "UserId " + userId.toString() + " rezervirao ItemId " + itemId.toString();
    }
}
