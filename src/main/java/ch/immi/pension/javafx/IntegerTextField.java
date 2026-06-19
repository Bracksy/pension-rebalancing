package ch.immi.pension.javafx;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class IntegerTextField extends AbstractTextField {

    public IntegerTextField(int width, int length) {
        super(width, length);
    }

    public int getInt() {
        int value = 0;
        if (getText() != null && !getText().isEmpty()) {
            value = Integer.parseInt(getText().replace("'", ""));
        }
        return value;
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

    protected void updateText(String input) {
        try {
            int intValue = Integer.parseInt(input);
            setText(getDecimalFormatter().format(intValue));
        } catch (NumberFormatException e) {
            // Falls die Zahl zu gross für 'long' ist
            clear();
        }
    }
}