package ch.immi.pension.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfigNamePopup extends Stage {

    private final TextField inputField;
    private final Button confirmButton;
    private final Button cancelButton;
    private String resultText = null;

    public ConfigNamePopup(Stage owner, String title, String headerText) {
        // 1. Configure Window Properties
        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(owner);
        this.setTitle(title);
        this.setResizable(false);

        // 2. Initialize UI Elements
        Label headerLabel = new Label(headerText);
        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        inputField = new TextField();
        inputField.setPrefWidth(250);

        // 3. Create Action Buttons
        confirmButton = new Button("Confirm");
        cancelButton = new Button("Cancel");

        // Simple validation: Disable confirm button if textfield is empty
        confirmButton.disableProperty().bind(inputField.textProperty().isEmpty());

        // 4. Set Button Functionality
        confirmButton.setOnAction(e -> {
            resultText = inputField.getText();
            this.close();
        });

        cancelButton.setOnAction(e -> {
            resultText = null; // Explicitly reset on cancel
            this.close();
        });

        // 5. Layout Setup
        HBox buttonLayout = new HBox(10, confirmButton, cancelButton);
        buttonLayout.setAlignment(Pos.CENTER_RIGHT);

        VBox rootLayout = new VBox(15, headerLabel, inputField, buttonLayout);
        rootLayout.setPadding(new Insets(20));
        rootLayout.setStyle("-fx-background-color: #f4f4f4;");

        // 6. Finalize Scene Setup
        Scene scene = new Scene(rootLayout);
        this.setScene(scene);
    }

    /**
     * Shows the popup, blocks execution until closed, and returns the result string.
     * @return The text entered by the user, or null if cancelled.
     */
    public String showAndGetResult() {
        this.showAndWait();
        return resultText;
    }
}

