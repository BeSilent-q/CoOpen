module coopen.coopen {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.dustinredmond.fxtrayicon;

    opens coopen.coopen to javafx.fxml;
    exports coopen.coopen;
}