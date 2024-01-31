package production.utility;

import production.model.LibraryItem;
import production.model.User;
import production.model.Worker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataChange<TFirst,TSecond> implements Serializable {

    TFirst data1;
    TSecond data2;
    LocalDateTime time;

    public DataChange(TFirst data1, TSecond data2) {
        this.data1 = data1;
        this.data2 = data2;
        time = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = time.format(dateTimeFormat);
        if (data2 instanceof String) {
            return data1.toString() + " " + data2 + " " + formattedDateTime;
        }
        else if (data1 instanceof Worker && data2 instanceof LibraryItem) {
            return data1.toString() + " je dodao " + data2 + " " + formattedDateTime;
        }
        else return data1.toString() + " - " + data2.toString() + " " + formattedDateTime;
    }
}
