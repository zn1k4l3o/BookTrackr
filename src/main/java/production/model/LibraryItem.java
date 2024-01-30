package production.model;

import production.utility.DatabaseUtils;
import production.utility.SessionManager;

import java.io.Serializable;
import java.util.Optional;

import static production.utility.BorrowActions.checkBorrowedStatus;
import static production.utility.BorrowActions.checkBorrowedStatusWithId;

public abstract sealed class LibraryItem implements Serializable permits Book, Movie  {

    Long id;
    String title;
    Float rating;


    public LibraryItem(Long id, String title, Float rating) {
        this.id = id;
        this.title = title;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addNewToTitle() {
        title = "***-" + title + "-NEW";
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getStatus() {
        Long userId = checkBorrowedStatus(id);
        if (userId.equals(-1L)) {
            return "DOSTUPNO";
        }
        else if (userId.equals(-2L)) {
            return "REZERVIRANO";
        }
        else if (userId.equals(SessionManager.getCurrentUser().getId())) {
            return "POSUĐENO";
        }
        else return "NEDOSTUPNO";
    }

    public String getStatusWithUserId(Long userId) {
        Long returnedUserId = checkBorrowedStatusWithId(id, userId);
        if (returnedUserId.equals(-1L)) {
            return "DOSTUPNO";
        }
        else if (returnedUserId.equals(-2L)) {
            return "REZERVIRANO";
        }
        else if (returnedUserId.equals(userId)) {
            return "POSUĐENO";
        }
        else return "NEDOSTUPNO";
    }

    public String getReturnDate() {
        Optional<BorrowInfo> borrowInfoOptional = DatabaseUtils.getBorrowingInfoForItemIdFromDatabase(id);
        if (borrowInfoOptional.isPresent()) {
            BorrowInfo borrowInfo = borrowInfoOptional.get();
            String returnDate = borrowInfo.returnDate().toString();
            return returnDate;
        }
        return "NIJE POSUĐENO!";
    }
}
