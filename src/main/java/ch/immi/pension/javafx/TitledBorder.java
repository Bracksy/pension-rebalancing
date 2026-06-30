package ch.immi.pension.javafx;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TitledBorder extends VBox {

    Label lblTitle = new Label();

    public TitledBorder(String title) {
        // 1. Die Hauptbox für den Rahmen
        super(5);
        setStyle("-fx-border-color: #ababab; " +
                "-fx-border-width: 1; " +
                "-fx-border-style: solid; " +
                "-fx-border-radius: 5; " +
                "-fx-padding: 10 5 5 5;");

        // 2. Das Label für den Titel
        lblTitle.setText(title);
        // Wichtig: Eine feste Hintergrundfarbe (z.B. weiß) eingeben, damit die Linie darunter verschwindet
        lblTitle.setStyle("-fx-font-weight: bold; -fx-background-color: #F2F2F2; -fx-padding: 0 5 0 5;");

        // Wir positionieren das Label relativ zur Box
        lblTitle.setTranslateY(-20); // Schiebt es hoch auf den Rahmen
        lblTitle.setTranslateX(10);  // Schiebt es leicht nach rechts

        // 3. Der Trick: Ein unsichtbarer Platzhalter
        // Weil das Label durch -25px nach oben verschoben wurde, klafft darunter eine Lücke.
        // Wir setzen einen negativen Abstand (Margin) für das NÄCHSTE Element, das in die Box kommt.
        // Dadurch rutscht dein GridPane magisch ganz nach oben an den Rand!
        VBox.setMargin(lblTitle, new Insets(0, 0, -18, 0));

        getChildren().add(lblTitle);
    }

    public void setTitle(String title) {
        this.lblTitle.setText(title);
    }
}
