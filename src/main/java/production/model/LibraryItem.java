package production.model;

import java.io.Serializable;
import java.util.Date;

public abstract class LibraryItem implements Serializable {

    Long id;
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

    public LibraryItem(Long id, Long borrowedById, Long reservedById, Date returnDate) {
        this.id = id;
        this.borrowedById = borrowedById;
        this.reservedById = reservedById;
        this.returnDate = returnDate;
    }
}
