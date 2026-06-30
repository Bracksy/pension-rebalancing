package ch.immi.pension.javafx;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

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

        if (!readonly) {
            setTextFormatter(new TextFormatter<>(change -> {
                // Holt den Text, wie er NACH der Änderung aussehen würde
                String newText = change.getControlNewText();

                // Wenn das Feld geleert wird, erlauben wir das immer
                if (newText.isEmpty()) {
                    setTransferred(false); // Logik aus Ihrem alten Listener
                    return change;
                }

                // VALIDIERUNG: Passt das Regex-Match oder ist der Text zu lang?
                if (!matches(newText) || newText.length() > maxLength) {
                    // BLOCKIEREN: Die Änderung (z.B. ein falsches Ctrl+V) wird einfach ignoriert!
                    // Ctrl+C/V stürzen dadurch nicht ab und behalten ihre Funktion.
                    return null;
                }

                // LOGIK FÜR FORMATIERUNGS-ÄNDERUNGEN
                String oldText = change.getControlText();
                if (oldText.isEmpty()) {
                    // Da wir im TextFormatter sind, können wir applyFormatting() hier oft nicht direkt aufrufen,
                    // da es den Text erneut ändern würde. Nutzen Sie dafür besser Platform.runLater:
                    Platform.runLater(() -> applyFormatting());
                } else if (!removeFormatting(oldText).equals(removeFormatting(newText))) {
                    setTransferred(false);
                }

                return change; // Änderung ist valide und wird ausgeführt
            }));
        }

    }

    public void setTransferred(boolean transferred) {
        if (transferred) {
            setStyle("-fx-control-inner-background: #d7f5fa");
        } else {
            setStyle("-fx-control-inner-background: #FFFFFF");
        }
    }

    protected abstract boolean matches(String newText);

    protected abstract DecimalFormat getDecimalFormatter();

    protected abstract void updateText(String input);

    protected void removeFormatting() {
        String input = getText();
        input = removeFormatting(input);
        setText(input);
    }

    protected void applyFormatting() {
        String input = getText();

        // Alte Trennzeichen entfernen
        input = removeFormatting(input);

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
            setStyle("-fx-control-inner-background: #FFFFFF");
        }
    }

    private String removeFormatting(String text) {
        return text.replace("'", "");
    }
}
