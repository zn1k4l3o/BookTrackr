package production.model;

import java.sql.Date;

public record BorrowInfo(Long id, Long itemId, Long userId, Date borrowDate, Date returnDate) {
}
