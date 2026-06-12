package ch.immi.pension.javafx;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TitledBorder extends VBox {

    public TitledBorder(String titel) {
        // 1. Die Hauptbox für den Rahmen
        super(10);
        setStyle("-fx-border-color: #ababab; " +
                "-fx-border-width: 1; " +
                "-fx-border-style: solid; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 15 10 10 10;");

        // 2. Das Label für den Titel
        Label lblTitel = new Label(titel);
        // Wichtig: Eine feste Hintergrundfarbe (z.B. weiß) eingeben, damit die Linie darunter verschwindet
        lblTitel.setStyle("-fx-font-weight: bold; -fx-background-color: white; -fx-padding: 0 5 0 5;");

        // Wir positionieren das Label relativ zur Box
        lblTitel.setTranslateY(-20); // Schiebt es hoch auf den Rahmen
        lblTitel.setTranslateX(10);  // Schiebt es leicht nach rechts

        // 3. Der Trick: Ein unsichtbarer Platzhalter
        // Weil das Label durch -25px nach oben verschoben wurde, klafft darunter eine Lücke.
        // Wir setzen einen negativen Abstand (Margin) für das NÄCHSTE Element, das in die Box kommt.
        // Dadurch rutscht dein GridPane magisch ganz nach oben an den Rand!
        VBox.setMargin(lblTitel, new Insets(0, 0, -20, 0));

        getChildren().add(lblTitel);
    }
}
