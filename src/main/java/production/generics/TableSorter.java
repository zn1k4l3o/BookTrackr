package production.generics;

import production.model.LibraryItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableSorter<T extends Comparable<T>> {

    public void sort(List<T> list) {
        Collections.sort(list);
    }
    /*
    public int compare(T i1, T i2) {
        if (i1.getTitle().compareTo(i2.getTitle()) > 0) return 1;
        else {
            if (i1.getId().compareTo(i2.getId()) > 0) return 1;
            else return i1.getId().compareTo(i2.getId());
        }
    }

     */

}

/*
class GenericSorter<T extends Comparable<T>> {
    public void sort(List<T> list) {
        Collections.sort(list);
    }
}
 */