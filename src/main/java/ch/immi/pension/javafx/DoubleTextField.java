package ch.immi.pension.javafx;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DoubleTextField extends AbstractTextField {

    public DoubleTextField(int width, int length, String hint) {
        super(width, length, hint, false);
    }

    public double getDouble() {
        double value = 0;
        if (getText() != null && !getText().isEmpty()) {
            value = Double.parseDouble(getText().replace("'", ""));
        }
        return value;
    }

    public void setDouble(Double value) {
        if (value != null) {
            setText(Double.toString(value));
            applyFormatting();
        }
    }

    protected boolean matches(String newText) {
        return newText.matches("\\d+\\.?\\d?\\d?");
    }

    protected DecimalFormat getDecimalFormatter() {
        // Schweizer Hochkomma-Format definieren
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        return new DecimalFormat("#,###.00", symbols);
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
