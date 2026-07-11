package ch.immi.pension.javafx;

import ch.immi.pension.util.HistoryUtil;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HtmlPopup extends Stage {

    private final WebView webView;
    private final WebEngine webEngine;

    /**
     * Constructor for the reusable HTML Popup.
     *
     * @param ownerWindow  The parent Window (Stage) to bind the modal to.
     * @param configKey The configKey
     */
    public HtmlPopup(Window ownerWindow, String configKey) {
        // Initialize window hierarchy and block parent window interactions
        if (ownerWindow != null) {
            this.initOwner(ownerWindow);
            this.initModality(Modality.WINDOW_MODAL);
        }

        // Initialize JavaFX browser nodes
        this.webView = new WebView();
        this.webEngine = webView.getEngine();

        String filename = HistoryUtil.getFilename(configKey);
        this.setTitle(filename);

        Path filePath = Paths.get(filename);
        if (Files.isRegularFile(filePath)) {
            try {
                // WICHTIG: Wandelt den Path in eine valide file:// URL um
                String urlString = filePath.toUri().toURL().toExternalForm();
                webEngine.load(urlString);
            } catch (Exception e) {
                webEngine.loadContent("<html><body><h3>Fehler beim Konvertieren des Pfads:</h3><p>" + e.getMessage() + "</p></body></html>");
            }
        } else {
            String targetPath = filePath != null ? filePath.toAbsolutePath().toString() : "null";
            webEngine.loadContent(
                    "<html><body style='font-family:sans-serif; text-align:center; padding-top:20px;'>" +
                            "<h3>⚠️ Fehler: Datei wurde nicht gefunden</h3>" +
                            "<p>Gesuchter Pfad: <code>" + targetPath + "</code></p>" +
                            "</body></html>"
            );
        }

        // Wrap inside a layout pane and apply to the stage
        StackPane root = new StackPane(webView);
        Scene scene = new Scene(root, 900, 600); // Set default starting size
        this.setScene(scene);
    }
}
