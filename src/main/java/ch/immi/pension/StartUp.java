package ch.immi.pension;

import ch.immi.pension.persistence.DatabaseManager;
import ch.immi.pension.javafx.*;
import javafx.stage.Stage;

import java.util.Locale;

public class StartUp extends javafx.application.Application {

    @Override
    public void stop() {
        System.out.println("Schliesse Datenbankverbindungen...");
        DatabaseManager.getCurrent().close();
    }

    @Override
    public void start(Stage primaryStage) {
        Locale swissGermanLocale = Locale.of("de", "ch"); //$NON-NLS-1$ //$NON-NLS-2$
        Locale.setDefault(swissGermanLocale);

        if (!DatabaseManager.getCurrent().isConnectionOk()) {
            MessageDialog.showError(primaryStage, "Datenbankfehler", //$NON-NLS-1$
                    "Keine Verbindung zur Datenbank"); //$NON-NLS-1$
            return;
        }

        MainDialog mainDialog = new MainDialog();
        mainDialog.show(primaryStage, 1000, 770);
    }
}
