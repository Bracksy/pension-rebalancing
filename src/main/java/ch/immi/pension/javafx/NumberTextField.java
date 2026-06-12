package ch.immi.pension.javafx;

import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NumberTextField extends TextField {
    final int maxLength;

    public NumberTextField(int width, int length) {
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
            if (newText != null && !newText.isEmpty()) {
                if (!newText.matches("[\\d']+") || newText.length() > maxLength) {
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

    private void removeFormatting() {
        String input = getText();
        input = input.replace("'", "");
        setText(input);
    }

    private void applyFormatting() {
        String input = getText();

        // Alte Trennzeichen entfernen
        input = input.replace("'", "");

        // Nur positive Ganzzahlen erlauben
        if (input.matches("\\d+")) {
            try {
                long number = Long.parseLong(input);

                // Schweizer Hochkomma-Format definieren
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator('\'');

                DecimalFormat formatter = new DecimalFormat("#,###", symbols);
                setText(formatter.format(number));
            } catch (NumberFormatException e) {
                // Falls die Zahl zu gross für 'long' ist
                clear();
            }
        } else if (!input.isEmpty()) {
            // Ungültigen Text (z.B. Buchstaben) löschen
            clear();
        }
    }

}