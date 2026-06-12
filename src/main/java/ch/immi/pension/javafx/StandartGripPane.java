package ch.immi.pension.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class StandartGripPane extends GridPane {

    public StandartGripPane() {
        super();
        setAlignment(Pos.TOP_LEFT);
        setHgap(5);
        setVgap(5);
        setPadding(new Insets(5));
    }

    private void addThinBorder() {
        setStyle("-fx-border-color: #dcdcdc; " +
                "-fx-border-width: 1; " +
                "-fx-border-style: solid; " +
                "-fx-border-radius: 4; " +
                "-fx-background-color: rgba(255, 255, 255, 0.3);");
    }
}
