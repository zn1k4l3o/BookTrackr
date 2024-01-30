package production.utility;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataChange<T> implements Serializable {

    T data;
    String message;
    LocalDateTime time;

    public void DataChange(T data, String message) {
        this.data = data;
        this.message = message;
        time = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = time.format(dateTimeFormat);
        return data.toString() + message + formattedDateTime;
    }
}
