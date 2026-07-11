package ch.immi.pension.javafx;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class NewButton extends Button {

    public NewButton() {
        // Create an empty region to serve as our graphic container
        Region icon = new Region();
        icon.setPrefSize(14, 16);

        // SVG Path for a simple trash can icon shape
        String svgTrashPath = "M 9 2 L 11 2 L 11 9 L 18 9 L 18 11 L 11 11 L 11 18 L 9 18 L 9 11 L 2 11 L 2 9 L 9 9 Z";

        // Apply styles programmatically
        icon.setStyle("-fx-background-color: #d9534f; -fx-shape: \"" + svgTrashPath + "\";");
        setGraphic(icon);
    }
}
