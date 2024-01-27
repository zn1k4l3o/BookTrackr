package production.model;

import java.util.Date;

public record ReservedInfo(Long id, Long itemId, Long userId, Date reservedDate) {
}
