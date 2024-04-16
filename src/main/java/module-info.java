module com.example.javafxcw {
    requires javafx.controls;
    requires javafx.fxml;
//    requires de.jensd.fx.glyphs.fontawesome;
//    requires jfxrt;
//    requires rt; // Corrected module name

    opens com.example.javafxcw to javafx.fxml;
    exports com.example.javafxcw;
}
