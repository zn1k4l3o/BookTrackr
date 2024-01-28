package production.model;

import java.sql.Date;

public record ReservedInfo(Long id, Long itemId, Long userId, Date reservedDate) {
}
