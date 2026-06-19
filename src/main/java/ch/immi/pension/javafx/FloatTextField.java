package ch.immi.pension.javafx;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FloatTextField extends AbstractTextField {

    public FloatTextField(int width, int length) {
        super(width, length);
    }

    public float getFloat() {
        float value = 0;
        if (getText() != null && !getText().isEmpty()) {
            value = Float.parseFloat(getText().replace("'", ""));
        }
        return value;
    }

    protected boolean matches(String newText) {
        return newText.matches("\\d+\\.?\\d?\\d?");
    }

    protected DecimalFormat getDecimalFormatter() {
        // Schweizer Hochkomma-Format definieren
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        return new DecimalFormat("#,###.##", symbols);
    }

    protected void updateText(String input) {
        try {
            float floatValue = Float.parseFloat(input);
            setText(getDecimalFormatter().format(floatValue));
        } catch (NumberFormatException e) {
            // Falls die Zahl zu gross für 'long' ist
            clear();
        }
    }
}
