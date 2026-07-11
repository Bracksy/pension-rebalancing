package ch.immi.pension;

import ch.immi.pension.exception.DatabaseException;
import ch.immi.pension.javafx.*;
import ch.immi.pension.persistence.*;
import ch.immi.pension.persistence.model.Account;
import ch.immi.pension.persistence.model.Configuration;
import ch.immi.pension.persistence.model.Setting;
import ch.immi.pension.util.*;
import com.sai.javafx.calendar.FXCalendar;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class MainDialog extends Pane {
    private final String ACCOUNT_HINT = "Kontostand";
    private final String PRICE_HINT = "Preis";
    private final String AMOUNT_HINT = "Anzahl";

    private final Data data = new Data();

    private final ComboBox<Configuration> cbChoose = new ComboBox<>();

    private final TitledBorder tbLastAccount = new TitledBorder("Letzter Kontostand");
    private final TitledBorder tbCurrentAccount = new TitledBorder("Aktueller Kontostand");
    private final TitledBorder tbNewAccount = new TitledBorder("Neuer Kontostand");

    private final DateTextField txtLastKontoDate = new DateTextField();
    private FXCalendar calLastKontoDate = new FXCalendar(txtLastKontoDate, Locale.GERMAN);
    private final CalendarButton btnCalendar = new CalendarButton();
    private final IntegerTextField txtLastKonto1 = new IntegerTextField(70, 7, ACCOUNT_HINT);
    private final IntegerTextField txtLastKonto2 = new IntegerTextField(70, 7, ACCOUNT_HINT);
    private final IntegerTextField txtLastNumber3a = new IntegerTextField(45, 4, AMOUNT_HINT, false);
    private final DoubleTextField txtLastPrice3a = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final IntegerTextField txtReadonlyLastKonto3a = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtLastNumber3b = new IntegerTextField(45, 4, AMOUNT_HINT, false);
    private final DoubleTextField txtLastPrice3b = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final IntegerTextField txtReadonlyLastKonto3b = new IntegerTextField(70, 7, true);
    private final Label lblLastVerteilung = new Label("40%/60%");
    private final DoubleTextField txtLastHighestPrice3a = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final DoubleTextField txtLastHighestPrice3b = new DoubleTextField(60, 6, PRICE_HINT, false);

    private final IntegerTextField txtKonto1 = new IntegerTextField(80, 7, ACCOUNT_HINT);
    private final IntegerTextField txtKonto2 = new IntegerTextField(80, 7, ACCOUNT_HINT);
    private final IntegerTextField txtNumber3a = new IntegerTextField(45, 4, AMOUNT_HINT, false);
    private final DoubleTextField txtPrice3a = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final IntegerTextField txtReadonlyKonto3a = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtNumber3b = new IntegerTextField(45, 4, AMOUNT_HINT, false);
    private final DoubleTextField txtPrice3b = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final IntegerTextField txtReadonlyKonto3b = new IntegerTextField(70, 7, true);
    private final Label lblVerteilung = new Label("40%/60%");
    private final DoubleTextField txtHighestPrice3a = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final DoubleTextField txtHighestPrice3b = new DoubleTextField(60, 6, PRICE_HINT, false);

    private final DateTextField txtNewKontoDate = new DateTextField();
    private final IntegerTextField txtNewKonto1 = new IntegerTextField(80, 7, ACCOUNT_HINT);
    private final IntegerTextField txtNewKonto2 = new IntegerTextField(80, 7, ACCOUNT_HINT);
    private final IntegerTextField txtNewNumber3a = new IntegerTextField(45, 4, AMOUNT_HINT, false);
    private final DoubleTextField txtNewPrice3a = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final IntegerTextField txtReadonlyNewKonto3a = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtNewNumber3b = new IntegerTextField(45, 4, AMOUNT_HINT, false);
    private final DoubleTextField txtNewPrice3b = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final IntegerTextField txtReadonlyNewKonto3b = new IntegerTextField(70, 7, true);
    private final Label lblNewVerteilung = new Label("40%/60%");
    private final DoubleTextField txtNewHighestPrice3a = new DoubleTextField(60, 6, PRICE_HINT, false);
    private final DoubleTextField txtNewHighestPrice3b = new DoubleTextField(60, 6, PRICE_HINT, false);

    private final Label lblProfit2 = new Label("5%");
    private final Label lblProfit3a = new Label("5%");
    private final Label lblProfit3b = new Label("5%");

    private final IntegerTextField txtBezugJahr = new IntegerTextField(80, 7, "Betrag");
    private final Label lblBezugJahrPercentage = new Label("0%");

    private final IntegerTextField txtReadonlyKonto1Min = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtReadonlyKonto1Opt = new IntegerTextField(70, 7, true);

    private final IntegerTextField txtReadonlyKonto2Min = new IntegerTextField(70, 7,true);
    private final IntegerTextField txtReadonlyKonto2Opt = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtReadonlyKonto2Max = new IntegerTextField(70, 7, true);

    private final TextField txtKonto3aTicker = new TextField();
    private final TextField txtKonto3bTicker = new TextField();
    private final IntegerTextField txtRebalancing3aPercent = new IntegerTextField(40, 3, "Prozent", false);
    private final IntegerTextField txtRebalancing3bPercent = new IntegerTextField(40, 3, "Prozent", false);
    private final IntegerTextField txtRebalancingThreshold = new IntegerTextField(40, 3, "Prozent");

    private final WebView webViewAusgabe = new WebView();

    private Configuration selectedConfiguration = null;
    private Stage dialogStage;

    private DatabaseSession currentSession = null;

    private enum Type {
        KAUF, VERKAUF
    }

    public final void show(final Stage pPrimaryStage, double pWidth, double pHeight) {
        dialogStage = pPrimaryStage;
        boolean openAsDialog = pPrimaryStage.isShowing();
        if (openAsDialog) {
            dialogStage = new Stage();
            dialogStage.getIcons().addAll(pPrimaryStage.getIcons());
        }

        dialogStage.setMinHeight(32);
        dialogStage.setMaxHeight(Double.MAX_VALUE);

        dialogStage.setMinWidth(20);
        dialogStage.setMaxWidth(Double.MAX_VALUE);

        dialogStage.setOnShown(_ -> JavaFxUtil.centerDialog(pPrimaryStage, dialogStage));

        if (pWidth >= 0) {
            dialogStage.setWidth(pWidth);
        }
        if (pHeight >= 0) {
            dialogStage.setHeight(pHeight);
        }

        if (createDialogContent()) {
            dialogStage.setTitle("Rebalancing berechnen..");

            if (openAsDialog) {
                dialogStage.showAndWait();
            } else {
                dialogStage.show();
            }
        }
    }

    private boolean createDialogContent() {
        // Haupt-Layout: Vertikal gestapelt (Oben: Spalten, Mitte: Button, Unten: Textfeld)
        VBox rootLayout = new VBox(10);
        rootLayout.setPadding(new Insets(15));
        rootLayout.setAlignment(Pos.TOP_CENTER);

        HBox chooseBox = chooseBox();

        // ==========================================
        // OBERER BEREICH: Horizontal geteilt
        // ==========================================
        HBox topBox = new HBox(5);
        topBox.setAlignment(Pos.TOP_LEFT);

        VBox leftBox = new VBox(10);
        Button btnTransfer = new Button("Daten übernehmen");
        btnTransfer.setMaxWidth(Integer.MAX_VALUE);
        btnTransfer.setOnAction(_ -> doTransfer());
        leftBox.getChildren().addAll(lastAccountWithHighestBox(), btnTransfer, accountWithHighestBox());

        VBox rechterBereich = parameterBox();

        // Beide Bereiche in den oberen Container legen
        topBox.getChildren().addAll(leftBox, rechterBereich);

        // ==========================================
        // MITTLERER BEREICH: Button (Volle Breite)
        // ==========================================

        Button btnAnalysis = new Button("Analysieren..");
        btnAnalysis.setMaxWidth(Double.MAX_VALUE);
        btnAnalysis.setPrefHeight(35);
        HBox.setHgrow(btnAnalysis, Priority.ALWAYS);
        btnAnalysis.setOnAction(_ -> {
            doAnalysis();
            recalculateAccount3Percentage();
        });

        Button btnHistory = new Button("Historie..");
        btnHistory.setMaxWidth(Double.MAX_VALUE);
        btnHistory.setPrefHeight(35);
        HBox.setHgrow(btnHistory, Priority.ALWAYS);
        btnHistory.setOnAction(_ -> doHistory());

        HBox buttonBox = new HBox(5);
        buttonBox.setMaxWidth(Double.MAX_VALUE);
        buttonBox.getChildren().addAll(btnAnalysis, btnHistory);

        // ==========================================
        // UNTERER BEREICH: Grosses Multiline-Textfeld
        // ==========================================
        HBox bottomBox = new HBox(5);
        bottomBox.setAlignment(Pos.TOP_LEFT);

        bottomBox.getChildren().addAll(webViewAusgabe(), newAccountWithHighestBox());

        // Alles in das Haupt-Layout einfügen (von oben nach unten gestapelt)
        rootLayout.getChildren().addAll(chooseBox, topBox, buttonBox, bottomBox);

        // Fenstergrösse angepasst (Breite: 760, Höhe: 700 wegen dem neuen Textfeld)
        Scene scene = new Scene(rootLayout);
        dialogStage.setScene(scene);
        dialogStage.show();

        load(this.selectedConfiguration);

        return true;
    }

    private HBox chooseBox() {
        HBox chooseBox = new HBox(5);
        cbChoose.setEditable(false);
        cbChoose.setMinWidth(400);
        cbChoose.setItems(FXCollections.observableList(getConfigurations()));
        cbChoose.setConverter(new StringConverter<>() {
            @Override
            public String toString(Configuration configuration) {
                return configuration == null ? "" : configuration.getName(); // Zeigt den Value an
            }

            @Override
            public Configuration fromString(String string) {
                return cbChoose.getItems().stream()
                        .filter(item -> item.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        Long selectedConfigId = data.getLong(Data.BASEPATH, "lastConfigId");
        if (selectedConfigId != null) {
            getConfiguration(selectedConfigId).ifPresent(this::doLoadConfig);
        }

        Button btnDelete = new DeleteButton();
        Button btnNew = new NewButton();
        chooseBox.getChildren().addAll(cbChoose, btnNew, btnDelete);

        cbChoose.getSelectionModel().selectedItemProperty().addListener((_,
                                                                         _, newItem) -> {
            if (newItem != null) {
                doLoadConfig(newItem);
            }
        });
        btnNew.setOnAction(_ -> {
            ConfigNamePopup configNamePopup = new ConfigNamePopup(this.dialogStage, "Neue Konfiguration", "Name");
            String name = configNamePopup.showAndGetResult();
            if (name != null) {
                doNewConfig(name);
            }
        });
        btnDelete.setOnAction(_ -> {
            Configuration configurationToDelete = cbChoose.getValue();
            if (configurationToDelete != null) {
                doDeleteConfig(configurationToDelete);
            }
        });

        return chooseBox;
    }

    private VBox lastAccountWithHighestBox() {
        VBox vBox = new VBox(5);

        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(lastAccountBox(), lastKonto3HighestPriceBox());

        vBox.getChildren().addAll(hBox);

        return vBox;
    }

    private VBox lastAccountBox() {
        tbLastAccount.setPrefWidth(380);
        tbLastAccount.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGridPane();
        gridLinks.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        btnCalendar.setOnMouseClicked(_ -> {
            calLastKontoDate.show();
        });

        gridLinks.add(new Label("Datum:"), 0, 0);
        gridLinks.add(txtLastKontoDate, 3, 0);
        gridLinks.add(btnCalendar, 4, 0);
        txtLastKontoDate.textProperty().addListener((_, oldText, newText) -> {
            if (newText != null && !newText.isEmpty() && !newText.equals(oldText)) {
                update(this.selectedConfiguration);
            }
        });

        gridLinks.add(new Label("Konto 1:"), 0, 1);
        gridLinks.add(txtLastKonto1, 3, 1);
        gridLinks.add(new Label("CHF"), 4, 1);
        addAutoSaveListener(txtLastKonto1);
        addUpdateListener(txtLastKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 2);
        gridLinks.add(txtLastKonto2, 3, 2);
        gridLinks.add(new Label("CHF"), 4, 2);
        addAutoSaveListener(txtLastKonto2);
        addUpdateListener(txtLastKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 3);
        gridLinks.add(txtLastNumber3a, 1, 3);
        gridLinks.add(txtLastPrice3a, 2, 3);
        addUpdateListener(txtLastPrice3a);
        gridLinks.add(txtReadonlyLastKonto3a, 3, 3);
        gridLinks.add(new Label("CHF"), 4, 3);
        addAutoSaveListener(txtLastNumber3a, txtLastPrice3a);
        addUpdateListener(txtLastNumber3a, txtLastPrice3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 4);
        gridLinks.add(txtLastNumber3b, 1, 4);
        gridLinks.add(txtLastPrice3b, 2, 4);
        addUpdateListener(txtLastPrice3b);
        gridLinks.add(txtReadonlyLastKonto3b, 3, 4);
        gridLinks.add(new Label("CHF"), 4, 4);
        addAutoSaveListener(txtLastNumber3b, txtLastPrice3b);
        addUpdateListener(txtLastNumber3b, txtLastPrice3b);

        gridLinks.add(lblLastVerteilung, 3, 5);

        tbLastAccount.getChildren().add(gridLinks);

        return tbLastAccount;
    }

    private VBox lastKonto3HighestPriceBox() {
        TitledBorder tbKonto3HighestPrice = new TitledBorder("Höchststand");
        tbKonto3HighestPrice.setPrefWidth(140);
        tbKonto3HighestPrice.setPrefHeight(170);
        tbKonto3HighestPrice.setAlignment(Pos.TOP_LEFT);

        GridPane gridKonto3 = new StandartGridPane();

        RowConstraints row0 = new RowConstraints();
        row0.setPrefHeight(50);
        gridKonto3.getRowConstraints().add(row0);

        gridKonto3.add(txtLastHighestPrice3a, 0, 2);
        gridKonto3.add(new Label("CHF"), 1, 2);
        txtLastHighestPrice3a.focusedProperty().addListener((_, _, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                selectedConfiguration.getLastAccount().setHighest3a(txtLastHighestPrice3a.getDouble());
                update(selectedConfiguration);
            }
        });

        gridKonto3.add(txtLastHighestPrice3b, 0, 3);
        gridKonto3.add(new Label("CHF"), 1, 3);
        txtLastHighestPrice3b.focusedProperty().addListener((_, _, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                selectedConfiguration.getLastAccount().setHighest3b(txtLastHighestPrice3b.getDouble());
                update(selectedConfiguration);
            }
        });
        tbKonto3HighestPrice.getChildren().add(gridKonto3);

        return tbKonto3HighestPrice;
    }

    private VBox accountWithHighestBox() {
        VBox vBox = new VBox(5);

        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(accountBox(), konto3HighestPriceBox());

        vBox.getChildren().addAll(hBox);

        return vBox;
    }

    private VBox accountBox() {
        tbCurrentAccount.setPrefWidth(380);
        tbCurrentAccount.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGridPane();
        gridLinks.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        gridLinks.add(new Label("Konto 1:"), 0, 0);
        gridLinks.add(txtKonto1, 3, 0);
        gridLinks.add(new Label("CHF"), 4, 0);
        addAutoSaveListener(txtKonto1);
        addUpdateListener(txtKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 1);
        gridLinks.add(txtKonto2, 3, 1);
        gridLinks.add(new Label("CHF"), 4, 1);
        gridLinks.add(lblProfit2, 5, 1);
        addAutoSaveListener(txtKonto2);
        addUpdateListener(txtKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 2);
        gridLinks.add(txtNumber3a, 1, 2);
        gridLinks.add(txtPrice3a, 2, 2);
        gridLinks.add(txtReadonlyKonto3a, 3, 2);
        gridLinks.add(new Label("CHF"), 4, 2);
        gridLinks.add(lblProfit3a, 5, 2);
        addAutoSaveListener(txtNumber3a, txtPrice3a);
        addUpdateListener(txtNumber3a, txtPrice3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 3);
        gridLinks.add(txtNumber3b, 1, 3);
        gridLinks.add(txtPrice3b, 2, 3);
        gridLinks.add(txtReadonlyKonto3b, 3, 3);
        gridLinks.add(new Label("CHF"), 4, 3);
        gridLinks.add(lblProfit3b, 5, 3);
        addAutoSaveListener(txtNumber3b, txtPrice3b);
        addUpdateListener(txtNumber3b, txtPrice3b);

        gridLinks.add(lblVerteilung, 3, 4);

        tbCurrentAccount.getChildren().add(gridLinks);

        return tbCurrentAccount;
    }

    private VBox konto3HighestPriceBox() {
        TitledBorder tbKonto3HighestPrice = new TitledBorder("Höchststand");
        tbKonto3HighestPrice.setPrefWidth(140);
        tbKonto3HighestPrice.setPrefHeight(170);
        tbKonto3HighestPrice.setAlignment(Pos.TOP_LEFT);

        GridPane gridKonto3 = new StandartGridPane();

        RowConstraints row0 = new RowConstraints();
        row0.setPrefHeight(50);
        gridKonto3.getRowConstraints().add(row0);

        gridKonto3.add(txtHighestPrice3a, 0, 2);
        gridKonto3.add(new Label("CHF"), 1, 2);
        txtHighestPrice3a.focusedProperty().addListener((_, _, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                selectedConfiguration.getCurrentAccount().setHighest3a(txtHighestPrice3a.getDouble());
                update(selectedConfiguration);
            }
        });

        gridKonto3.add(txtHighestPrice3b, 0, 3);
        gridKonto3.add(new Label("CHF"), 1, 3);
        txtHighestPrice3b.focusedProperty().addListener((_, _, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                selectedConfiguration.getCurrentAccount().setHighest3b(txtHighestPrice3b.getDouble());
                update(selectedConfiguration);
            }
        });
        tbKonto3HighestPrice.getChildren().add(gridKonto3);

        return tbKonto3HighestPrice;
    }

    private VBox parameterBox() {
        VBox rechterBereich = new TitledBorder("Parameter");
        rechterBereich.setPrefWidth(400);

        GridPane gridRechtsKonto12 = new StandartGridPane();

        gridRechtsKonto12.add(new Label("Bezug/Jahr:"), 0, 0);
        gridRechtsKonto12.add(txtBezugJahr, 1, 0);
        gridRechtsKonto12.add(new Label("CHF"), 2, 0);
        gridRechtsKonto12.add(lblBezugJahrPercentage, 3, 0);
        addAutoSaveListener(txtBezugJahr);
        addUpdateListener(txtBezugJahr);
        Tooltip tooltip = new Tooltip("Trinity-Studie: 4% optimal, 5% grenzwertig, 6% riskant");
        lblBezugJahrPercentage.setTooltip(tooltip);
        gridRechtsKonto12.add(new Label("min"), 1, 1);
        gridRechtsKonto12.add(new Label("opt"), 2, 1);
        gridRechtsKonto12.add(new Label("max"), 3, 1);

        gridRechtsKonto12.add(new Label("Konto 1:"), 0, 2);
        gridRechtsKonto12.add(txtReadonlyKonto1Min, 1, 2);
        gridRechtsKonto12.add(txtReadonlyKonto1Opt, 2, 2);

        gridRechtsKonto12.add(new Label("Konto 2:"), 0, 3);
        gridRechtsKonto12.add(txtReadonlyKonto2Min, 1, 3);
        gridRechtsKonto12.add(txtReadonlyKonto2Opt, 2, 3);
        gridRechtsKonto12.add(txtReadonlyKonto2Max, 3, 3);

        VBox gridRebalancing3Bereich = new TitledBorder("Konto 3");
        GridPane gridRebalancing3 = new StandartGridPane();
        txtKonto3aTicker.setPrefWidth(80);
        txtKonto3aTicker.setPromptText("Ticker 3a");
        txtKonto3bTicker.setPrefWidth(80);
        txtKonto3bTicker.setPromptText("Ticker 3b");
        gridRebalancing3.add(txtKonto3aTicker, 1, 0);
        gridRebalancing3.add(txtKonto3bTicker, 4, 0);

        gridRebalancing3.add(new Label("Verteilung 3a/3b:"), 0, 1);
        gridRebalancing3.add(txtRebalancing3aPercent, 1, 1);
        gridRebalancing3.add(new Label("%"), 2, 1);
        gridRebalancing3.add(new Label("/"), 3, 1);
        gridRebalancing3.add(txtRebalancing3bPercent, 4, 1);
        gridRebalancing3.add(new Label("%"), 5, 1);
        addAutoSaveListener(txtRebalancing3aPercent);
        addAutoSaveListener(txtRebalancing3bPercent);

        gridRebalancing3.add(new Label("Schwelle:"), 0, 2);
        gridRebalancing3.add(txtRebalancingThreshold, 1, 2);
        gridRebalancing3.add(new Label("%"), 2, 2);
        addAutoSaveListener(txtRebalancingThreshold);
        gridRebalancing3Bereich.getChildren().add(gridRebalancing3);

        rechterBereich.getChildren().addAll(gridRechtsKonto12, gridRebalancing3Bereich);
        return rechterBereich;
    }

    private WebView webViewAusgabe() {
        webViewAusgabe.setPrefHeight(210);
        webViewAusgabe.setPrefWidth(500);
        return webViewAusgabe;
    }

    private VBox newAccountWithHighestBox() {
        VBox vBox = new VBox(5);

        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(newAccountBox(), newKonto3HighestPriceBox());

        Button btnHandle = new Button("Übernehmen..");
        btnHandle.setMaxWidth(Double.MAX_VALUE);
        btnHandle.setPrefHeight(35);
        btnHandle.setOnAction(_ -> doAccept());

        vBox.getChildren().addAll(hBox, btnHandle);

        return vBox;
    }

    private VBox newAccountBox() {
        tbNewAccount.setPrefWidth(350);
        tbNewAccount.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGridPane();
        gridLinks.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        gridLinks.add(new Label("Datum:"), 0, 0);
        gridLinks.add(txtNewKontoDate, 3, 0);

        gridLinks.add(new Label("Konto 1:"), 0, 1);
        gridLinks.add(txtNewKonto1, 3, 1);
        gridLinks.add(new Label("CHF"), 4, 1);
        addAutoSaveListener(txtNewKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 2);
        gridLinks.add(txtNewKonto2, 3, 2);
        gridLinks.add(new Label("CHF"), 4, 2);
        addAutoSaveListener(txtNewKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 3);
        gridLinks.add(txtNewNumber3a, 1, 3);
        gridLinks.add(txtNewPrice3a, 2, 3);
        gridLinks.add(txtReadonlyNewKonto3a, 3, 3);
        gridLinks.add(new Label("CHF"), 4, 3);
        addAutoSaveListener(txtNewNumber3a, txtNewPrice3a);
        addUpdateListener(txtNewNumber3a, txtNewPrice3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 4);
        gridLinks.add(txtNewNumber3b, 1, 4);
        gridLinks.add(txtNewPrice3b, 2, 4);
        gridLinks.add(txtReadonlyNewKonto3b, 3, 4);
        gridLinks.add(new Label("CHF"), 4, 4);
        addAutoSaveListener(txtNewNumber3b, txtNewPrice3b);
        addUpdateListener(txtNewNumber3b, txtNewPrice3b);

        gridLinks.add(lblNewVerteilung, 3, 5);

        tbNewAccount.getChildren().add(gridLinks);

        return tbNewAccount;
    }

    private VBox newKonto3HighestPriceBox() {
        TitledBorder tbKonto3HighestPrice = new TitledBorder("Höchststand");
        tbKonto3HighestPrice.setPrefWidth(140);
        tbKonto3HighestPrice.setPrefHeight(170);
        tbKonto3HighestPrice.setAlignment(Pos.TOP_LEFT);

        GridPane gridKonto3 = new StandartGridPane();

        RowConstraints row0 = new RowConstraints();
        row0.setPrefHeight(50);
        gridKonto3.getRowConstraints().add(row0);

        gridKonto3.add(txtNewHighestPrice3a, 0, 2);
        gridKonto3.add(new Label("CHF"), 1, 2);
        txtNewHighestPrice3a.focusedProperty().addListener((_, _, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                selectedConfiguration.getNewAccount().setHighest3a(txtNewHighestPrice3a.getDouble());
                update(selectedConfiguration);
            }
        });

        gridKonto3.add(txtNewHighestPrice3b, 0, 3);
        gridKonto3.add(new Label("CHF"), 1, 3);
        txtNewHighestPrice3b.focusedProperty().addListener((_, _, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                selectedConfiguration.getNewAccount().setHighest3b(txtNewHighestPrice3b.getDouble());
                update(selectedConfiguration);
            }
        });
        tbKonto3HighestPrice.getChildren().add(gridKonto3);

        return tbKonto3HighestPrice;
    }

    private void addAutoSaveListener(TextField ... textFields) {
        for (TextField tf: textFields) {
            tf.focusedProperty().addListener((_, _, newValue) -> {
                // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
                if (!newValue) {
                    // Code wird ausgeführt, wenn das Feld VERLASSEN wird
                    update(selectedConfiguration);
                }
            });
        }
    }

    private void addUpdateListener(TextField ... textFields) {
        for (TextField tf : textFields) {
            tf.textProperty().addListener((_, _, newText) -> {
                if (newText != null && !newText.isEmpty()) {
                    recalculateAccount3();
                    recalculateAccount3Percentage();
                    recalculateBezugJahrPercentage();
                    updateTitle();
                    updateHoechststand();
                }
            });
        }
    }

    // Functionalities
    private void doLoadConfig(@NotNull Configuration configuration) {
        selectedConfiguration = configuration;
        cbChoose.getSelectionModel().select(configuration);
        data.store(Data.BASEPATH, "lastConfigId", configuration.getId());

        load(this.selectedConfiguration);
    }

    private void doNewConfig(String newName) {
        if (newName != null && !newName.isBlank()) {
            Configuration configuration = createNewConfiguration(newName);

            cbChoose.getItems().add(configuration);
            cbChoose.getSelectionModel().select(configuration);
            doLoadConfig(configuration);
        }
    }

    private void doDeleteConfig(Configuration configuration) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfiguration löschen");
        alert.setHeaderText(String.format("<%s> wird gelöscht!", configuration.getName()));
        alert.setContentText("Sind Sie sicher?");
        alert.initOwner(this.dialogStage);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            delete(configuration);
            cbChoose.getItems().remove(configuration);
            if (!getConfigurationLabels().isEmpty()) {
                cbChoose.getItems().stream().findFirst().ifPresent(this::doLoadConfig);
            }
        }
    }

    private void doTransfer() {
        txtKonto1.setInt(txtLastKonto1.getInt());
        txtKonto2.setInt(txtLastKonto2.getInt());
        txtNumber3a.setInt(txtLastNumber3a.getInt());
        txtPrice3a.setDouble(txtLastPrice3a.getDouble());
        txtNumber3b.setInt(txtLastNumber3b.getInt());
        txtPrice3b.setDouble(txtLastPrice3b.getDouble());

        txtHighestPrice3a.setDouble(Math.max(Math.max(txtLastHighestPrice3a.getDouble(), txtPrice3a.getDouble()),
                txtHighestPrice3a.getDouble()));
        txtHighestPrice3b.setDouble(Math.max(Math.max(txtLastHighestPrice3b.getDouble(), txtPrice3b.getDouble()),
                txtHighestPrice3b.getDouble()));

        updateTransferred();
        recalculateAccount3();
    }

    private void doAnalysis() {
        ThreePotState state = getRebalancedState();

        // Do calculation
        txtNewKontoDate.setLocalDate(LocalDate.now());
        txtNewKonto1.setInt(state.getPot1());
        txtNewKonto2.setInt(state.getPot2());
        txtNewNumber3a.setInt(state.getNumOfShares3a());
        txtNewPrice3a.setDouble(state.getPrice3a());
        txtNewNumber3b.setInt(state.getNumOfShares3b());
        txtNewPrice3b.setDouble(state.getPrice3b());
        txtNewHighestPrice3a.setDouble(state.getHighestPrice3a());
        txtNewHighestPrice3b.setDouble(state.getHighestPrice3b());
        showText(state);

        updateTitle();
        update(selectedConfiguration);
    }

    private void doHistory() {
        HtmlPopup popup = new HtmlPopup(
                this.dialogStage,
                getSelectedKey());

        popup.show();
    }

    private void doAccept() {
        System.out.println("do Accept");
        txtLastKonto1.setInt(txtNewKonto1.getInt());
        txtLastKonto2.setInt(txtNewKonto2.getInt());
        txtLastNumber3a.setInt(txtNewNumber3a.getInt());
        txtLastPrice3a.setDouble(txtNewPrice3a.getDouble());
        txtLastNumber3b.setInt(txtNewNumber3b.getInt());
        txtLastPrice3b.setDouble(txtNewPrice3b.getDouble());

        update(selectedConfiguration);
        updateTransferred();

        webViewAusgabe.getEngine().getLoadWorker().stateProperty().addListener((_, _, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                try {
                    String content = HistoryUtil.fill(getCurrentState(), getNewState(), getAusgabeHtml());
                    HistoryUtil.add(getSelectedKey(), content);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        });
    }

    private ThreePotState getCurrentState() {
        return new ThreePotState(txtKonto1.getInt(), txtKonto2.getInt(),
                txtNumber3a.getInt(), txtPrice3a.getDouble(), txtNumber3b.getInt(),
                txtPrice3b.getDouble(), txtHighestPrice3a.getDouble(), txtHighestPrice3b.getDouble());
    }

    private ThreePotState getNewState() {
        return new ThreePotState(txtNewKonto1.getInt(), txtNewKonto2.getInt(),
                txtNewNumber3a.getInt(), txtNewPrice3a.getDouble(), txtNewNumber3b.getInt(),
                txtNewPrice3b.getDouble(), txtNewHighestPrice3a.getDouble(), txtNewHighestPrice3b.getDouble());
    }

    private ThreePotState getRebalancedState() {
        // Do pot rebalancing
        double rebalancePercentage3a = ((double)txtRebalancing3aPercent.getInt()) / 100;
        double rebalancePercentage3b = ((double)txtRebalancing3bPercent.getInt()) / 100;
        double rebalanceThreshold = ((double)txtRebalancingThreshold.getInt()) / 100;

        // First do account3 rebalancing
        Pot3System pot3System = new Pot3System(txtNumber3a.getInt(), txtPrice3a.getDouble(),
                txtNumber3b.getInt(), txtPrice3b.getDouble());
        pot3System.setParams(rebalancePercentage3a, rebalancePercentage3b, rebalanceThreshold);
        Pots3State pots3State = pot3System.rebalance();

        ThreePotState state = new ThreePotState(txtKonto1.getInt(), txtKonto2.getInt(),
                pots3State.getNumberOfShares3a(), txtPrice3a.getDouble(), pots3State.getNumberOfShares3b(),
                txtPrice3b.getDouble(), txtHighestPrice3a.getDouble(), txtHighestPrice3b.getDouble());
        PotThreshold threshold = PotThreshold.of(txtBezugJahr.getInt());
        ThreePotSystem threePotSystem = new ThreePotSystem(threshold,
                rebalancePercentage3a,
                rebalancePercentage3b);
        threePotSystem.updateState(state);

        return state;
    }

    private List<Data.KeyValue> getConfigurationLabels() {
        return data.getChildrenValues(Data.BASEPATH,"label");
    }

    private @Nullable String getSelectedKey() {
        if (selectedConfiguration == null) {
            return null;
        }
        return selectedConfiguration.getName().replace(" ", "").replace("/", "_");
    }

    private void fillValues(Configuration configuration) {
        if (configuration != null) {
            configuration.getLastAccount().setDate(txtLastKontoDate.getLocalDate());
            configuration.getLastAccount().setAccount1(txtLastKonto1.getInt());
            configuration.getLastAccount().setAccount2(txtLastKonto2.getInt());
            configuration.getLastAccount().setNumber3a(txtLastNumber3a.getInt());
            configuration.getLastAccount().setPrice3a(txtLastPrice3a.getDouble());
            configuration.getLastAccount().setNumber3b(txtLastNumber3b.getInt());
            configuration.getLastAccount().setPrice3b(txtLastPrice3b.getDouble());

            configuration.getCurrentAccount().setAccount1(txtKonto1.getInt());
            configuration.getCurrentAccount().setAccount2(txtKonto2.getInt());
            configuration.getCurrentAccount().setNumber3a(txtNumber3a.getInt());
            configuration.getCurrentAccount().setPrice3a(txtPrice3a.getDouble());
            configuration.getCurrentAccount().setNumber3b(txtNumber3b.getInt());
            configuration.getCurrentAccount().setPrice3b(txtPrice3b.getDouble());

            configuration.getNewAccount().setDate(txtNewKontoDate.getLocalDate());
            configuration.getNewAccount().setAccount1(txtNewKonto1.getInt());
            configuration.getNewAccount().setAccount2(txtNewKonto2.getInt());
            configuration.getNewAccount().setNumber3a(txtNewNumber3a.getInt());
            configuration.getNewAccount().setPrice3a(txtNewPrice3a.getDouble());
            configuration.getNewAccount().setNumber3b(txtNewNumber3b.getInt());
            configuration.getNewAccount().setPrice3b(txtNewPrice3b.getDouble());

            configuration.getSetting().setAnnualWithdrawal(txtBezugJahr.getInt());
            configuration.getSetting().setTicker3a(txtKonto3aTicker.getText());
            configuration.getSetting().setTicker3b(txtKonto3bTicker.getText());
            configuration.getSetting().setPercentage3a(txtRebalancing3aPercent.getInt());
            configuration.getSetting().setPercentage3b(txtRebalancing3bPercent.getInt());
            configuration.getSetting().setThresholdPercentage(txtRebalancingThreshold.getInt());

            configuration.setBalancingText(getAusgabeHtml());

            updateTransferred();
        }
    }

    private void load(Configuration configuration) {
        if (configuration != null) {
            LocalDate lastDate = configuration.getLastAccount().getDate();
            if (lastDate != null) {
                txtLastKontoDate.setLocalDate(lastDate);
                btnCalendar.setVisible(false);
                btnCalendar.setManaged(false);
            } else {
                txtLastKontoDate.setText("");
                btnCalendar.setVisible(true);
                btnCalendar.setManaged(true);
            }
            txtLastKonto1.setInt(configuration.getLastAccount().getAccount1());
            txtLastKonto2.setInt(configuration.getLastAccount().getAccount2());
            txtLastNumber3a.setInt(configuration.getLastAccount().getNumber3a());
            txtLastPrice3a.setDouble(configuration.getLastAccount().getPrice3a());
            txtLastNumber3b.setInt(configuration.getLastAccount().getNumber3b());
            txtLastPrice3b.setDouble(configuration.getLastAccount().getPrice3b());
            txtLastHighestPrice3a.setDouble(configuration.getLastAccount().getHighest3a());
            txtLastHighestPrice3b.setDouble(configuration.getLastAccount().getHighest3b());

            txtKonto1.setInt(configuration.getCurrentAccount().getAccount1());
            txtKonto2.setInt(configuration.getCurrentAccount().getAccount2());
            txtNumber3a.setInt(configuration.getCurrentAccount().getNumber3a());
            txtPrice3a.setDouble(configuration.getCurrentAccount().getPrice3a());
            txtNumber3b.setInt(configuration.getCurrentAccount().getNumber3b());
            txtPrice3b.setDouble(configuration.getCurrentAccount().getPrice3b());
            txtHighestPrice3a.setDouble(configuration.getCurrentAccount().getHighest3a());
            txtHighestPrice3b.setDouble(configuration.getCurrentAccount().getHighest3b());

            txtNewKontoDate.setLocalDate(configuration.getNewAccount().getDate());
            txtNewKonto1.setInt(configuration.getNewAccount().getAccount1());
            txtNewKonto2.setInt(configuration.getNewAccount().getAccount2());
            txtNewNumber3a.setInt(configuration.getNewAccount().getNumber3a());
            txtNewPrice3a.setDouble(configuration.getNewAccount().getPrice3a());
            txtNewNumber3b.setInt(configuration.getNewAccount().getNumber3b());
            txtNewPrice3b.setDouble(configuration.getNewAccount().getPrice3b());
            txtNewHighestPrice3a.setDouble(configuration.getNewAccount().getHighest3a());
            txtNewHighestPrice3b.setDouble(configuration.getNewAccount().getHighest3b());

            txtBezugJahr.setInt(configuration.getSetting().getAnnualWithdrawal());

            String txtKonto3aTickerText = configuration.getSetting().getTicker3a();
            if (txtKonto3aTickerText != null) {
                txtKonto3aTicker.setText(txtKonto3aTickerText);
            }
            String txtKonto3bTickerText = configuration.getSetting().getTicker3b();
            if (txtKonto3bTickerText != null) {
                txtKonto3bTicker.setText(txtKonto3bTickerText);
            }
            txtRebalancing3aPercent.setInt(configuration.getSetting().getPercentage3a());
            txtRebalancing3bPercent.setInt(configuration.getSetting().getPercentage3b());
            txtRebalancingThreshold.setInt(configuration.getSetting().getThresholdPercentage());

            String txtAusgabeGrossText = configuration.getBalancingText();
            webViewAusgabe.setAccessibleText(Objects.requireNonNullElse(txtAusgabeGrossText, ""));

            recalculateAccount3();
            updateTitle();
            updateTransferred();
        }
    }

    private String getAusgabeHtml() {
        return (String)webViewAusgabe.getEngine().executeScript("document.documentElement.outerHTML");
    }

    private void setAusgabeHtml(String html) {
        final WebEngine webEngine = webViewAusgabe.getEngine();
        webEngine.loadContent(html);
    }

    private void updateTransferred() {
        txtKonto1.setTransferred(txtKonto1.getInt() == txtLastKonto1.getInt());
        txtKonto2.setTransferred(txtKonto2.getInt() == txtLastKonto2.getInt());
        txtNumber3a.setTransferred(txtNumber3a.getInt() == txtLastNumber3a.getInt());
        txtPrice3a.setTransferred(txtPrice3a.getDouble() == txtLastPrice3a.getDouble());
        txtNumber3b.setTransferred(txtNumber3b.getInt() == txtLastNumber3b.getInt());
        txtPrice3b.setTransferred(txtPrice3b.getDouble() == txtLastPrice3b.getDouble());
        txtHighestPrice3a.setTransferred(txtHighestPrice3a.getDouble() == txtLastHighestPrice3a.getDouble());
        txtHighestPrice3b.setTransferred(txtHighestPrice3b.getDouble() == txtLastHighestPrice3b.getDouble());
    }

    private void updateTitle() {
        int letzterAccount = txtLastKonto1.getInt() + txtLastKonto2.getInt() + txtReadonlyLastKonto3a.getInt() + txtReadonlyLastKonto3b.getInt();
        tbLastAccount.setTitle(String.format("Letzter Kontostand: %s CHF",
                FormatUtil.getDecimalFormatter().format(letzterAccount)));
        int aktuellerAccount = txtKonto1.getInt() + txtKonto2.getInt() + txtReadonlyKonto3a.getInt() + txtReadonlyKonto3b.getInt();
        tbCurrentAccount.setTitle(String.format("Aktueller Kontostand: %s CHF",
                FormatUtil.getDecimalFormatter().format(aktuellerAccount)));
        int neuerAccount = txtNewKonto1.getInt() + txtNewKonto2.getInt() + txtReadonlyNewKonto3a.getInt() + txtReadonlyNewKonto3b.getInt();
        tbNewAccount.setTitle(String.format("Neuer Kontostand: %s CHF",
                FormatUtil.getDecimalFormatter().format(neuerAccount)));
    }

    private void updateHoechststand() {
        if (txtLastHighestPrice3a.getText() == null || txtLastHighestPrice3a.getText().isBlank()
                || txtLastHighestPrice3a.getDouble() == 0) {
            txtLastHighestPrice3a.setText(txtLastPrice3a.getText());
        } else {
            double lastHighestPrice3a = txtLastHighestPrice3a.getDouble();
            double lastPrice3a = txtLastPrice3a.getDouble();
            if (lastPrice3a > lastHighestPrice3a) {
                txtLastHighestPrice3a.setDouble(lastPrice3a);
            }
        }
        if (txtLastHighestPrice3b.getText() == null || txtLastHighestPrice3b.getText().isBlank()
                || txtLastHighestPrice3b.getDouble() == 0) {
            txtLastHighestPrice3b.setText(txtLastPrice3b.getText());
        } else {
            double lastHighestPrice3b = txtLastHighestPrice3b.getDouble();
            double lastPrice3b = txtLastPrice3b.getDouble();
            if (lastPrice3b > lastHighestPrice3b) {
                txtLastHighestPrice3b.setDouble(lastPrice3b);
            }
        }
        if (txtHighestPrice3a.getText() == null || txtHighestPrice3a.getText().isBlank()
                || txtHighestPrice3a.getDouble() == 0) {
            txtHighestPrice3a.setText(txtPrice3a.getText());
        } else {
            double highestPrice3a = txtHighestPrice3a.getDouble();
            double currentPrice3a = txtPrice3a.getDouble();
            if (currentPrice3a > highestPrice3a) {
                txtHighestPrice3a.setDouble(currentPrice3a);
            }
        }
        if (txtHighestPrice3b.getText() == null || txtHighestPrice3b.getText().isBlank()
                || txtHighestPrice3b.getDouble() == 0) {
            txtHighestPrice3b.setText(txtPrice3b.getText());
        } else {
            double highestPrice3b = txtHighestPrice3b.getDouble();
            double currentPrice3b = txtPrice3b.getDouble();
            if (currentPrice3b > highestPrice3b) {
                txtHighestPrice3b.setDouble(currentPrice3b);
            }
        }
    }

    private void recalculateProfit() {
        lblProfit2.setText(getProfitAsText(txtLastKonto2.getText(), txtKonto2.getText()));
        lblProfit3a.setText(getProfitAsText(txtReadonlyLastKonto3a.getText(), txtReadonlyKonto3a.getText()));
        lblProfit3b.setText(getProfitAsText(txtReadonlyLastKonto3b.getText(), txtReadonlyKonto3b.getText()));
    }

    private void recalculateAccount3() {
        txtReadonlyLastKonto3a.setInt((int)Math.round(txtLastNumber3a.getInt() * txtLastPrice3a.getDouble()));
        txtReadonlyLastKonto3b.setInt((int)Math.round(txtLastNumber3b.getInt() * txtLastPrice3b.getDouble()));
        txtReadonlyKonto3a.setInt((int)Math.round(txtNumber3a.getInt() * txtPrice3a.getDouble()));
        txtReadonlyKonto3b.setInt((int)Math.round(txtNumber3b.getInt() * txtPrice3b.getDouble()));
        txtReadonlyNewKonto3a.setInt((int)Math.round(txtNewNumber3a.getInt() * txtNewPrice3a.getDouble()));
        txtReadonlyNewKonto3b.setInt((int)Math.round(txtNewNumber3b.getInt() * txtNewPrice3b.getDouble()));

        recalculateAccount3Percentage();
        recalculateProfit();
    }

    private void recalculateAccount3Percentage() {
        lblLastVerteilung.setText(getVerteilungText(txtReadonlyLastKonto3a.getInt(), txtReadonlyLastKonto3b.getInt()));
        lblVerteilung.setText(getVerteilungText(txtReadonlyKonto3a.getInt(), txtReadonlyKonto3b.getInt()));
        lblNewVerteilung.setText(getVerteilungText(txtReadonlyNewKonto3a.getInt(), txtReadonlyNewKonto3b.getInt()));
    }

    private void recalculateBezugJahrPercentage() {
        double amount = txtLastKonto1.getInt() + txtLastKonto2.getInt() + txtReadonlyLastKonto3a.getInt() + txtReadonlyLastKonto3b.getInt();
        double bezug = txtBezugJahr.getInt();
        double percentage = amount != 0? bezug/ amount * 100: 0;
        lblBezugJahrPercentage.setText(String.format("%.2f", percentage) + "%");

        PotThreshold threshold = PotThreshold.of(txtBezugJahr.getInt());
        txtReadonlyKonto1Min.setInt(threshold.getPot1Min());
        txtReadonlyKonto1Opt.setInt(threshold.getPot1Opt());
        txtReadonlyKonto2Min.setInt(threshold.getPot2Min());
        txtReadonlyKonto2Opt.setInt(threshold.getPot2Opt());
        txtReadonlyKonto2Max.setInt(threshold.getPot2Max());
    }

    private String getVerteilungText(double val1, double val2) {
        double total = val1 + val2;
        double p1 = total != 0 ? val1 * 100 / total : val1;
        double p2 = total != 0 ? val2 * 100 / total : val2;
        return Math.round(p1) + "%/ " + Math.round(p2) + "%";
    }

    private String getProfitAsText(String oldBalanceTxt, String newBalanceTxt) {
        return String.format("%.2f", getProfit(oldBalanceTxt, newBalanceTxt)) + "%";
    }

    private double getProfit(String oldBalanceTxt, String newBalanceTxt) {
        double oldValue = getInt(oldBalanceTxt);
        double newValue = getInt(newBalanceTxt);
        return getProfit(oldValue, newValue);
    }

    private double getProfit(double oldBalance, double newBalance) {
        double profit = 0d;
        if (oldBalance > 0) {
            profit = (newBalance - oldBalance) / oldBalance * 100;
        }
        return profit;
    }

    private int getInt(String text) {
        int value = 0;
        if (text != null && !text.isEmpty()) {
            value = Integer.parseInt(text.replace("'", ""));
        }
        return value;
    }

    private void showText(ThreePotState newState) {
        String ausgabeHtml = "";
        if (newState.getChange().getFrom2To1() > 0 || newState.getChange().getFrom2To3() > 0) {
            int sellKonto2 = newState.getChange().getFrom2To1() + newState.getChange().getFrom2To3();
            ausgabeHtml += String.format("Verkauf Konto 2: %s CHF\n", FormatUtil.getDecimalFormatter().format(sellKonto2));
            if (newState.getChange().getFrom2To1() > 0) {
                ausgabeHtml += "<ul><li>";
                ausgabeHtml += String.format("Einlage Konto 1: %s CHF\n", FormatUtil.getDecimalFormatter().format(newState.getChange().getFrom2To1()));
                ausgabeHtml += "</li></ul>";
            }
            if (newState.getChange().getFrom2To3() > 0) {
                ausgabeHtml += konto3KaufVerkaufText(Type.KAUF, newState.getChange().getFrom2To3(),
                        newState.getChange().getChangeShares3a(), newState.getChange().getChangeShares3b());
            }
            ausgabeHtml += "\n";
        }
        if (newState.getChange().getFrom3To1() > 0 || newState.getChange().getFrom3To2() > 0) {
            int sellKonto3 = newState.getChange().getFrom3To1() + newState.getChange().getFrom3To2();
            ausgabeHtml += konto3KaufVerkaufText(Type.VERKAUF, sellKonto3,
                    newState.getChange().getChangeShares3a(), newState.getChange().getChangeShares3b());
            ausgabeHtml += "<ul>";
            if (newState.getChange().getFrom3To1() > 0) {
                ausgabeHtml += "<li>";
                ausgabeHtml += String.format("Einlage Konto 1: %s CHF\n", FormatUtil.getDecimalFormatter().format(newState.getChange().getFrom3To1()));
                ausgabeHtml += "</li>";
            }
            if (newState.getChange().getFrom3To2() > 0) {
                ausgabeHtml += "<li>";
                ausgabeHtml += String.format("Einlage Konto 2: %s CHF\n", FormatUtil.getDecimalFormatter().format(newState.getChange().getFrom3To2()));
                ausgabeHtml += "</li>";
            }
            ausgabeHtml += "<ul>";
            ausgabeHtml += "\n";
        }
        if (newState.getChange().isChangedHighestPrice3a()) {
            ausgabeHtml += String.format("Neuer Höchststand 3a: %s CHF\n", FormatUtil.getDoubleFormatter().format(newState.getHighestPrice3a()));
        }
        if (newState.getChange().isChangedHighestPrice3b()) {
            ausgabeHtml += String.format("Neuer Höchststand 3b: %s CHF\n", FormatUtil.getDoubleFormatter().format(newState.getHighestPrice3b()));
        }
        setAusgabeHtml(ausgabeHtml);
    }

    private String konto3KaufVerkaufText(Type type, int sellBuyKonto3, int numOfShares3a, int numOfShares3b) {
        String text = "Verkauf ";
        if (type == Type.KAUF) {
            text = "Kauf ";
        }
        if (numOfShares3a != 0 && numOfShares3b != 0) {
            text += String.format("Konto 3: %s CHF (3a: %s ETF, 3b: %s ETF)\n",
                    FormatUtil.getDecimalFormatter().format(sellBuyKonto3), FormatUtil.getDecimalFormatter().format(numOfShares3a),
                    FormatUtil.getDecimalFormatter().format(numOfShares3b));
        } else if (numOfShares3a != 0) {
            text += String.format("Konto 3: %s CHF (3a: %s ETF)\n", FormatUtil.getDecimalFormatter().format(sellBuyKonto3),
                    FormatUtil.getDecimalFormatter().format(numOfShares3a));
        } else if (numOfShares3b != 0) {
            text += String.format("Konto 3: %s CHF (3b: %s ETF)\n", FormatUtil.getDecimalFormatter().format(sellBuyKonto3),
                    FormatUtil.getDecimalFormatter().format(numOfShares3b));
        }
        return text;
    }

    private Configuration createNewConfiguration(String name) {
        List<Configuration> existing = getCurrentSession()
                .read(Configuration.class, null, List.of(DatabaseSession.Param.of("name", name)));
        if (existing != null && !existing.isEmpty()) {
            return existing.getFirst();
        }

        Configuration configuration = new Configuration();
        configuration.setName(name);
        configuration.setSetting(new Setting());
        configuration.setLastAccount(new Account());
        configuration.setNewAccount(new Account());
        configuration.setCurrentAccount(new Account());
        fillValues(configuration);
        try {
            getCurrentSession().insert(configuration);
        } catch(DatabaseException dbEx) {
            MessageDialog.showError(this.dialogStage, "Insert Fehler", dbEx.getMessage());
        }
        return configuration;
    }

    private void update(Configuration configuration) {
        if (configuration != null) {
            fillValues(configuration);
            try {
                getCurrentSession().update(configuration);
            } catch (DatabaseException dbEx) {
                MessageDialog.showError(this.dialogStage, "Update Fehler", dbEx.getMessage());
            }
        }
    }

    private void delete(Configuration configuration) {
        if (configuration != null) {
            try {
                getCurrentSession().delete(configuration);
            } catch (DatabaseException dbEx) {
                MessageDialog.showError(this.dialogStage, "Delete Fehler", dbEx.getMessage());
            }
        }
    }

    private List<Configuration> getConfigurations() {
        return getCurrentSession().readAll(Configuration.class, List.of("name"));
    }

    private Optional<Configuration> getConfiguration(long id) {
        return getCurrentSession().readByPK(Configuration.class, id);
    }

    private DatabaseSession getCurrentSession() {
        if (this.currentSession == null || !this.currentSession.isConnected()) {
            this.currentSession = DatabaseManager.openSession();
        }
        return this.currentSession;
    }
}
