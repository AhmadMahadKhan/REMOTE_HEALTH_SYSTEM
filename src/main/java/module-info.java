module com.structure.remote_health_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires java.desktop;
    requires jakarta.mail;
    requires java.naming;
    requires itext;
    requires org.jfree.jfreechart;
    requires mysql.connector.j;

    opens com.structure.Logins to javafx.fxml;
    opens com.structure.project to javafx.fxml;
    opens com.structure.UserManagment to javafx.fxml;
    opens com.structure.AppointmentScheduling to javafx.base;
    opens com.structure.Model to javafx.fxml;

    exports com.structure.Logins;
    exports com.structure.UserManagment;
    exports com.structure.Model;
}