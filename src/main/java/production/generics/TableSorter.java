package production.generics;

import production.model.LibraryItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableSorter<T extends Comparable<T>> {

    public void sort(List<T> list) {
        Collections.sort(list);
    }


}
