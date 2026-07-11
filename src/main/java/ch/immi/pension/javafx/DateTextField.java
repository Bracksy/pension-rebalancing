package ch.immi.pension.javafx;

import javafx.scene.control.TextField;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTextField extends TextField {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public DateTextField() {
        super();
        setEditable(false);
        setFocusTraversable(true);
        setStyle("-fx-control-inner-background: #F2F2F2");
    }

    public LocalDate getLocalDate() {
        LocalDate value = null;
        if (getText() != null && !getText().isEmpty()) {
            value = LocalDate.parse(getText(), FORMATTER);
        }
        return value;
    }

    public void setLocalDate(LocalDate date) {
        if (date != null) {
            setText(FORMATTER.format(date));
        }
    }
}