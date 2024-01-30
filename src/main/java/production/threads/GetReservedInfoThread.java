package production.threads;

import production.model.ReservedInfo;

import java.util.Optional;

public class GetReservedInfoThread extends DatabaseThread implements Runnable {

    Long itemId;

    public GetReservedInfoThread(Long itemId) {
        this.itemId = itemId;
    }
    Optional<ReservedInfo> optionalReservedInfo = Optional.empty();
    @Override
    public void run() {
        optionalReservedInfo = super.getReservedInfoForItemIdFromDB(itemId);
    }

    public Optional<ReservedInfo> getReservedInfo() {
        return optionalReservedInfo;
    }
}
