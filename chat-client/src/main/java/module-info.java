module com.eutech.chatclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

//    requires org.kordamp.bootstrapfx.core;

    opens com.eutech.chatclient to javafx.fxml;
    exports com.eutech.chatclient;

    exports com.eutech.chatclient.controller;  // Add this line to export the controller package

    opens com.eutech.chatclient.controller to javafx.fxml;
}

