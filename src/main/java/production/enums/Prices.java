package production.enums;

import java.math.BigDecimal;

public enum Prices {

    DAY_LATE_FEE_BOOK(BigDecimal.valueOf(0.2)),
    DAY_LATE_FEE_MOVIE(BigDecimal.valueOf(0.5));

    final BigDecimal amount;

    Prices(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
