package ch.immi.pension.javafx;

import javafx.scene.control.TextField;

import java.text.DecimalFormat;

public abstract class AbstractTextField extends TextField {
    private final int maxLength;

    public AbstractTextField(int width, int length, String hint, boolean readonly) {
        maxLength = length;
        if (hint != null) {
            setPromptText(hint);
        }
        setPrefWidth(width);
        setReadonly(readonly);

        // Event-Listener für den Fokusverlust hinzufügen
        focusedProperty().addListener((observable, oldVal, newVal) -> {
            if (!readonly) {
                if (!newVal) {
                    // Wenn newVal false ist, hat das Feld den Fokus verloren
                    applyFormatting();
                } else {
                    removeFormatting();
                }
            }
        });

        textProperty().addListener((observable, oldText, newText) -> {
            if (!readonly) {
                if (newText != null && !newText.isEmpty()) {
                    if (!matches(newText) || newText.length() > maxLength) {
                        setText(oldText);
                    }
                    // Format initially
                    if ((oldText == null || oldText.isEmpty())) {
                        applyFormatting();
                    }
                }
            }

        });
    }

    protected abstract boolean matches(String newText);

    protected abstract DecimalFormat getDecimalFormatter();

    protected abstract void updateText(String input);

    protected void removeFormatting() {
        String input = getText();
        input = input.replace("'", "");
        setText(input);
    }

    protected void applyFormatting() {
        String input = getText();

        // Alte Trennzeichen entfernen
        input = input.replace("'", "");

        if (matches(input)) {
            updateText(input);
        } else if (!input.isEmpty()) {
            // Ungültigen Text (z.B. Buchstaben) löschen
            clear();
        }
    }

    protected void setReadonly(boolean readonly) {
        setEditable(!readonly);
        setFocusTraversable(!readonly);
        if (readonly) {
            setStyle("-fx-control-inner-background: #F2F2F2");
        } else {
            setStyle("-fx-control-inner-background: #ffffff");
        }
    }
}
