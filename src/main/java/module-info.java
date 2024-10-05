module org.tyby.javaapacheexp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires log4j;
    requires java.datatransfer;
    requires java.desktop;
    requires fastjson;
    requires java.naming;
    requires com.oracle.weblogic.deployment;

    opens org.tyby.javaapacheexp to javafx.fxml;
    exports org.tyby.javaapacheexp;
    exports org.tyby.javaapacheexp.controller;
    opens org.tyby.javaapacheexp.controller to javafx.fxml;
}