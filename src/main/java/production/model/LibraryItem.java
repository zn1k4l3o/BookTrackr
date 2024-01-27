package production.model;

import org.controlsfx.control.PropertySheet;
import production.utility.BorrowActions;
import production.utility.SessionManager;

import java.io.Serializable;
import java.util.Date;

import static production.utility.BorrowActions.checkBorrowedStatus;

public abstract class LibraryItem implements Serializable {

    /*
    Long id;
    Boolean canBeBorrowed = true;
    Long borrowedById;
    Long reservedById;
    Date returnDate;


    public Long getBorrowedById() {
        return borrowedById;
    }

    public void setBorrowedById(Long borrowedById) {
        this.borrowedById = borrowedById;
    }

    public Long getReservedById() {
        return reservedById;
    }

    public void setReservedById(Long reservedById) {
        this.reservedById = reservedById;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getCanBeBorrowed() {
        return canBeBorrowed;
    }

    public void setCanBeBorrowed(Boolean canBeBorrowed) {
        this.canBeBorrowed = canBeBorrowed;
    }

    public LibraryItem(Long id, Long borrowedById, Long reservedById, Date returnDate, Boolean canBeBorrowed) {
        this.id = id;
        this.borrowedById = borrowedById;
        this.reservedById = reservedById;
        this.returnDate = returnDate;
        this.canBeBorrowed = canBeBorrowed;
    }

    public String getStatus() {
        String status = "TREBA SLOŽITI";
        return status;
    }

    */


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
        else return "NEDOSTUPNO " + userId;
    }
}
