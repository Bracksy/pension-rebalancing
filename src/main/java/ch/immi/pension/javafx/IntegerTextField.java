package ch.immi.pension.javafx;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class IntegerTextField extends AbstractTextField {

    public IntegerTextField(int width, int length, boolean readonly) {
        super(width, length, null, readonly, !readonly);
    }

    public IntegerTextField(int width, int length, String hint, boolean showZero) {
        super(width, length, hint, false, showZero);
    }

    public IntegerTextField(int width, int length, String hint) {
        super(width, length, hint, false);
    }

    public int getInt() {
        int value = 0;
        if (getText() != null && !getText().isEmpty()) {
            value = Integer.parseInt(getText().replace("'", ""));
        }
        return value;
    }

    public void setInt(Integer value) {
        if (value != null) {
            setText(Integer.toString(value));
            applyFormatting();
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

    protected void updateText(String input) {
        try {
            int intValue = Integer.parseInt(input);
            if (intValue == 0) {
                if (isShowZero()) {
                    setText(getDecimalFormatter().format(intValue));
                } else {
                    setText("");
                }
            } else {
                setText(getDecimalFormatter().format(intValue));
            }
        } catch (NumberFormatException e) {
            // Falls die Zahl zu gross für 'long' ist
            clear();
        }
    }
}