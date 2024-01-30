package production.threads;

import production.model.BorrowInfo;

import java.util.Optional;

public class GetBorrowinInfoThread extends DatabaseThread implements Runnable {
    Long itemId;

    public GetBorrowinInfoThread(Long itemId) {
        this.itemId = itemId;
    }
    Optional<BorrowInfo> optionalBorrowInfo = Optional.empty();
    @Override
    public void run() {
        optionalBorrowInfo = super.getBorrowingInfoForItemIdFromDB(itemId);
    }

    public Optional<BorrowInfo> getBorrowInfo() {
        return optionalBorrowInfo;
    }
}
