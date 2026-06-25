package ch.immi.pension.javafx;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class DeleteButton extends Button {

    public DeleteButton() {
        // Create an empty region to serve as our graphic container
        Region icon = new Region();
        icon.setPrefSize(14, 16);

        // SVG Path for a simple trash can icon shape
        String svgTrashPath = "M4 2V0h6v2h5v2H1v-2h5zM2 6h12v10H2V6zm2 2v6h2V8H4zm4 0v6h2V8H8zm4 0v6h2V8h-2z";

        // Apply styles programmatically
        icon.setStyle("-fx-background-color: #d9534f; -fx-shape: \"" + svgTrashPath + "\";");
        setGraphic(icon);
    }
}
