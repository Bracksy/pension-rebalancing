package ch.immi.pension.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtil {

    @Contract(" -> new")
    public static @NotNull DecimalFormat getDecimalFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        return new DecimalFormat("#,###", symbols);
    }


    @Contract(" -> new")
    public static @NotNull DecimalFormat getDoubleFormatter() {
        // Schweizer Hochkomma-Format definieren
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        return new DecimalFormat("#,###.00", symbols);
    }

    public static @NotNull String getDateTimeString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return formatter.format(localDateTime);
    }
}
