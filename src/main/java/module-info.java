module tvz.hr.booktrackr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens tvz.hr.booktrackr to javafx.fxml;
    exports tvz.hr.booktrackr;
}