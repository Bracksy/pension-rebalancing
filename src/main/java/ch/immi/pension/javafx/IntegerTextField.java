package ch.immi.pension.javafx;

import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class IntegerTextField extends TextField {
    private final int maxLength;

    private boolean readonly = false;

    public IntegerTextField(int width, int length) {
        maxLength = length;
        setPromptText("Enter number..");
        setPrefWidth(width);

        // Event-Listener für den Fokusverlust hinzufügen
        focusedProperty().addListener((observable, oldVal, newVal) -> {

            if (!newVal) {
                // Wenn newVal false ist, hat das Feld den Fokus verloren
                applyFormatting();
            } else {
                removeFormatting();
            }
        });

        textProperty().addListener((observable, oldText, newText) -> {
            if (readonly) {
                applyFormatting();
            } else if (newText != null && !newText.isEmpty()) {
                if (!matches(newText) || newText.length() > maxLength) {
                    setText(oldText);
                }
                // Format initially
                if ((oldText == null || oldText.isEmpty())) {
                    applyFormatting();
                }
            }

        });
    }

    public void setTextReformat(String text) {
        setText(text);
        applyFormatting();
    }

    public void setReadonly(boolean readonly) {
        if (readonly) {
            setStyle("-fx-control-inner-background: #F5F5F5");
        } else {
            setStyle("-fx-control-inner-background: #fffff");
        }
    }

    public int getInt() {
        int value = 0;
        if (getText() != null && !getText().isEmpty()) {
            value = Integer.parseInt(getText().replace("'", ""));
        }
        return value;
    }

    public void updateText(String input) {
        try {
            int intValue = Integer.parseInt(input);
            setText(getDecimalFormatter().format(intValue));
        } catch (NumberFormatException e) {
            // Falls die Zahl zu gross für 'long' ist
            clear();
        }
    }

    protected boolean matches(String newText) {
        return newText.matches("[\\d']+");
    }

    protected DecimalFormat getDecimalFormatter() {
        // Schweizer Hochkomma-Format definieren
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        return new DecimalFormat("#,###", symbols);
    }

    private void removeFormatting() {
        String input = getText();
        input = input.replace("'", "");
        setText(input);
    }

    private void applyFormatting() {
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
}