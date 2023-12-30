module tvz.hr.booktrackr {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;

    requires org.controlsfx.controls;

    opens tvz.hr.booktrackr to javafx.fxml;
    exports tvz.hr.booktrackr;
}