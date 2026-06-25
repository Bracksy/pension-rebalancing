package ch.immi.pension;

import ch.immi.pension.util.Pots3State;
import ch.immi.pension.util.PotThreshold;
import ch.immi.pension.javafx.*;
import ch.immi.pension.persistence.Data;
import ch.immi.pension.util.Pot3System;
import ch.immi.pension.util.ThreePotState;
import ch.immi.pension.util.ThreePotSystem;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Application extends javafx.application.Application {
    private final String ACCOUNT_HINT = "Kontostand";
    private final String PRICE_HINT = "Preis";
    private final String AMOUNT_HINT = "Anzahl";

    private final Data data = new Data();

    private final ComboBox<Data.KeyValue> cbChoose = new ComboBox<>();

    private final TitledBorder tbLastAccount = new TitledBorder("Letzter Kontostand");
    private final TitledBorder tbCurrentAccount = new TitledBorder("Aktueller Kontostand");
    private final TitledBorder tbNewAccount = new TitledBorder("Neuer Kontostand");

    private final IntegerTextField txtLastKonto1 = new IntegerTextField(70, 7, ACCOUNT_HINT);
    private final IntegerTextField txtLastKonto2 = new IntegerTextField(70, 7, ACCOUNT_HINT);
    private final IntegerTextField txtLastNumber3a = new IntegerTextField(45, 4, AMOUNT_HINT);
    private final DoubleTextField txtLastPrice3a = new DoubleTextField(60, 6, PRICE_HINT);
    private final IntegerTextField txtReadonlyLastKonto3a = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtLastNumber3b = new IntegerTextField(45, 4, AMOUNT_HINT);
    private final DoubleTextField txtLastPrice3b = new DoubleTextField(60, 6, PRICE_HINT);
    private final IntegerTextField txtReadonlyLastKonto3b = new IntegerTextField(70, 7, true);
    private final Label lblLastVerteilung = new Label("40%/60%");
    private final DoubleTextField txtLastHighestPrice3a = new DoubleTextField(60, 6, PRICE_HINT);
    private final DoubleTextField txtLastHighestPrice3b = new DoubleTextField(60, 6, PRICE_HINT);

    private final IntegerTextField txtKonto1 = new IntegerTextField(80, 7, ACCOUNT_HINT);
    private final IntegerTextField txtKonto2 = new IntegerTextField(80, 7, ACCOUNT_HINT);
    private final IntegerTextField txtNumber3a = new IntegerTextField(45, 4, AMOUNT_HINT);
    private final DoubleTextField txtPrice3a = new DoubleTextField(60, 6, PRICE_HINT);
    private final IntegerTextField txtReadonlyKonto3a = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtNumber3b = new IntegerTextField(45, 4, AMOUNT_HINT);
    private final DoubleTextField txtPrice3b = new DoubleTextField(60, 6, PRICE_HINT);
    private final IntegerTextField txtReadonlyKonto3b = new IntegerTextField(70, 7, true);
    private final Label lblVerteilung = new Label("40%/60%");
    private final DoubleTextField txtHighestPrice3a = new DoubleTextField(60, 6, PRICE_HINT);
    private final DoubleTextField txtHighestPrice3b = new DoubleTextField(60, 6, PRICE_HINT);

    private final IntegerTextField txtNewKonto1 = new IntegerTextField(80, 7, ACCOUNT_HINT);
    private final IntegerTextField txtNewKonto2 = new IntegerTextField(80, 7, ACCOUNT_HINT);
    private final IntegerTextField txtNewNumber3a = new IntegerTextField(45, 4, AMOUNT_HINT);
    private final DoubleTextField txtNewPrice3a = new DoubleTextField(60, 6, PRICE_HINT);
    private final IntegerTextField txtReadonlyNewKonto3a = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtNewNumber3b = new IntegerTextField(45, 4, AMOUNT_HINT);
    private final DoubleTextField txtNewPrice3b = new DoubleTextField(60, 6, PRICE_HINT);
    private final IntegerTextField txtReadonlyNewKonto3b = new IntegerTextField(70, 7, true);
    private final Label lblNewVerteilung = new Label("40%/60%");
    private final DoubleTextField txtNewHighestPrice3a = new DoubleTextField(60, 6, PRICE_HINT);
    private final DoubleTextField txtNewHighestPrice3b = new DoubleTextField(60, 6, PRICE_HINT);

    private final Label lblProfit2 = new Label("5%");
    private final Label lblProfit3a = new Label("5%");
    private final Label lblProfit3b = new Label("5%");

    private final IntegerTextField txtBezugJahr = new IntegerTextField(80, 7, "Betrag");
    private final Label lblBezugJahrPercentage = new Label("0%");

    private final IntegerTextField txtReadonlyKonto1Min = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtReadonlyKonto1Opt = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtReadonlyKonto1Max = new IntegerTextField(70, 7, true);

    private final IntegerTextField txtReadonlyKonto2Min = new IntegerTextField(70, 7,true);
    private final IntegerTextField txtReadonlyKonto2Opt = new IntegerTextField(70, 7, true);
    private final IntegerTextField txtReadonlyKonto2Max = new IntegerTextField(70, 7, true);

    private final TextField txtKonto3aTicker = new TextField();
    private final TextField txtKonto3bTicker = new TextField();
    private final IntegerTextField txtRebalancing3aPercent = new IntegerTextField(40, 3, "Prozent");
    private final IntegerTextField txtRebalancing3bPercent = new IntegerTextField(40, 3, "Prozent");
    private final IntegerTextField txtRebalancingThreshold = new IntegerTextField(40, 3, "Prozent");

    private final TextArea txtAusgabeGross = new TextArea();

    private String selectedKey = null;
    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        primaryStage.setTitle("Rebalancing berechnen..");

        // Haupt-Layout: Vertikal gestapelt (Oben: Spalten, Mitte: Button, Unten: Textfeld)
        VBox rootLayout = new VBox(15);
        rootLayout.setPadding(new Insets(15));
        rootLayout.setAlignment(Pos.TOP_CENTER);

        HBox chooseBox = chooseBox();

        // ==========================================
        // OBERER BEREICH: Horizontal geteilt
        // ==========================================
        HBox topBox = new HBox(5);
        topBox.setAlignment(Pos.TOP_LEFT);

        VBox leftBox = new VBox(10);
        leftBox.getChildren().addAll(lastAccountWithHighestBox(), accountWithHighestBox());

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
        btnAnalysis.setOnAction(event -> {
            doAnalysis();
            recalculateAccount3Percentage();
        });

        Button btnHistory = new Button("Historie..");
        btnHistory.setMaxWidth(Double.MAX_VALUE);
        btnHistory.setPrefHeight(35);
        HBox.setHgrow(btnHistory, Priority.ALWAYS);
        btnHistory.setOnAction(event -> doHistory());

        HBox buttonBox = new HBox(5);
        buttonBox.setMaxWidth(Double.MAX_VALUE);
        buttonBox.getChildren().addAll(btnAnalysis, btnHistory);

        // ==========================================
        // UNTERER BEREICH: Grosses Multiline-Textfeld
        // ==========================================
        HBox bottomBox = new HBox(5);
        bottomBox.setAlignment(Pos.TOP_LEFT);

        bottomBox.getChildren().addAll(textAusgabeArea(), newAccountWithHighestBox());

        // Alles in das Haupt-Layout einfügen (von oben nach unten gestapelt)
        rootLayout.getChildren().addAll(chooseBox, topBox, buttonBox, bottomBox);

        // Fenstergrösse angepasst (Breite: 760, Höhe: 700 wegen dem neuen Textfeld)
        Scene scene = new Scene(rootLayout, 900, 680);
        primaryStage.setScene(scene);
        primaryStage.show();

        readValuesFromRegistry();
    }

    private HBox chooseBox() {
        HBox chooseBox = new HBox(5);
        cbChoose.setEditable(true);
        cbChoose.setMinWidth(300);
        cbChoose.setItems(FXCollections.observableList(getConfigurationLabels()));
        cbChoose.setConverter(new StringConverter<>() {
            @Override
            public String toString(Data.KeyValue object) {
                return object == null ? "" : object.getValue(); // Zeigt den Value an
            }

            @Override
            public Data.KeyValue fromString(String string) {
                return cbChoose.getItems().stream()
                        .filter(item -> item.getValue().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        selectedKey = data.getString(Data.BASEPATH, "lastSelectedKey");
        getConfiguration(selectedKey).ifPresent(selectedKeyValue -> {
            cbChoose.getSelectionModel().select(selectedKeyValue);
            doLoadConfig(selectedKeyValue);
        });

        Button btnDelete = new DeleteButton();
        chooseBox.getChildren().addAll(cbChoose, btnDelete);

        cbChoose.getSelectionModel().selectedItemProperty().addListener((observable,
                                                                         oldItem, newItem) -> {
            if (newItem != null) {
                doLoadConfig(newItem);
            } else {
                String newConfigText = cbChoose.getEditor().getText();
                if (newConfigText != null && !newConfigText.isBlank()) {
                    doNewConfig(newConfigText);
                }
            }
        });
        btnDelete.setOnAction(event -> {
            Data.KeyValue keyValueToDelete = cbChoose.getValue();
            if (keyValueToDelete != null) {
                doDeleteConfig(keyValueToDelete);
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

        gridLinks.add(new Label("Konto 1:"), 0, 0);
        gridLinks.add(txtLastKonto1, 3, 0);
        gridLinks.add(new Label("CHF"), 4, 0);
        addAutoSaveListener(txtLastKonto1);
        addUpdateListener(txtLastKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 1);
        gridLinks.add(txtLastKonto2, 3, 1);
        gridLinks.add(new Label("CHF"), 4, 1);
        addAutoSaveListener(txtLastKonto2);
        addUpdateListener(txtLastKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 2);
        gridLinks.add(txtLastNumber3a, 1, 2);
        gridLinks.add(txtLastPrice3a, 2, 2);
        addUpdateListener(txtLastPrice3a);
        gridLinks.add(txtReadonlyLastKonto3a, 3, 2);
        gridLinks.add(new Label("CHF"), 4, 2);
        addAutoSaveListener(txtLastNumber3a, txtLastPrice3a);
        addUpdateListener(txtLastNumber3a, txtLastPrice3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 3);
        gridLinks.add(txtLastNumber3b, 1, 3);
        gridLinks.add(txtLastPrice3b, 2, 3);
        addUpdateListener(txtLastPrice3b);
        gridLinks.add(txtReadonlyLastKonto3b, 3, 3);
        gridLinks.add(new Label("CHF"), 4, 3);
        addAutoSaveListener(txtLastNumber3b, txtLastPrice3b);
        addUpdateListener(txtLastNumber3b, txtLastPrice3b);

        gridLinks.add(lblLastVerteilung, 3, 4);

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
        txtLastHighestPrice3a.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                data.store(selectedKey, "txtLastHighestPrice3a", txtLastHighestPrice3a.getDouble());
            }
        });

        gridKonto3.add(txtLastHighestPrice3b, 0, 3);
        gridKonto3.add(new Label("CHF"), 1, 3);
        txtLastHighestPrice3b.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                data.store(selectedKey, "txtLastHighestPrice3b", txtLastHighestPrice3b.getDouble());
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
        txtHighestPrice3a.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                data.store(selectedKey, "txtHighestPrice3a", txtHighestPrice3a.getDouble());
            }
        });

        gridKonto3.add(txtHighestPrice3b, 0, 3);
        gridKonto3.add(new Label("CHF"), 1, 3);
        txtHighestPrice3b.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                data.store(selectedKey, "txtHighestPrice3b", txtHighestPrice3b.getDouble());
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
        gridRechtsKonto12.add(txtReadonlyKonto1Max, 3, 2);

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

    private TextArea textAusgabeArea() {
        txtAusgabeGross.setPromptText("Rebalancingvorschlag erscheint hier...");
        txtAusgabeGross.setPrefHeight(210);
        txtAusgabeGross.setWrapText(true);
        return txtAusgabeGross;
    }

    private VBox newAccountWithHighestBox() {
        VBox vBox = new VBox(5);

        HBox hBox = new HBox(5);
        hBox.getChildren().addAll(newAccountBox(), newKonto3HighestPriceBox());

        Button btnHandle = new Button("Übernehmen..");
        btnHandle.setMaxWidth(Double.MAX_VALUE);
        btnHandle.setPrefHeight(35);
        btnHandle.setOnAction(event -> doAccept());

        vBox.getChildren().addAll(hBox, btnHandle);

        return vBox;
    }

    private VBox newAccountBox() {
        tbNewAccount.setPrefWidth(350);
        tbNewAccount.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGridPane();
        gridLinks.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        gridLinks.add(new Label("Konto 1:"), 0, 0);
        gridLinks.add(txtNewKonto1, 3, 0);
        gridLinks.add(new Label("CHF"), 4, 0);
        addAutoSaveListener(txtNewKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 1);
        gridLinks.add(txtNewKonto2, 3, 1);
        gridLinks.add(new Label("CHF"), 4, 1);
        addAutoSaveListener(txtNewKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 2);
        gridLinks.add(txtNewNumber3a, 1, 2);
        gridLinks.add(txtNewPrice3a, 2, 2);
        gridLinks.add(txtReadonlyNewKonto3a, 3, 2);
        gridLinks.add(new Label("CHF"), 4, 2);
        addAutoSaveListener(txtNewNumber3a, txtNewPrice3a);
        addUpdateListener(txtNewNumber3a, txtNewPrice3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 3);
        gridLinks.add(txtNewNumber3b, 1, 3);
        gridLinks.add(txtNewPrice3b, 2, 3);
        gridLinks.add(txtReadonlyNewKonto3b, 3, 3);
        gridLinks.add(new Label("CHF"), 4, 3);
        addAutoSaveListener(txtNewNumber3b, txtNewPrice3b);
        addUpdateListener(txtNewNumber3b, txtNewPrice3b);

        gridLinks.add(lblNewVerteilung, 3, 4);

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
        txtNewHighestPrice3a.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                data.store(selectedKey, "txtNewHighestPrice3a", txtNewHighestPrice3a.getDouble());
            }
        });

        gridKonto3.add(txtNewHighestPrice3b, 0, 3);
        gridKonto3.add(new Label("CHF"), 1, 3);
        txtNewHighestPrice3b.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                data.store(selectedKey, "txtNewHighestPrice3b", txtNewHighestPrice3b.getDouble());
            }
        });
        tbKonto3HighestPrice.getChildren().add(gridKonto3);

        return tbKonto3HighestPrice;
    }

    private void addAutoSaveListener(TextField ... textFields) {
        for (TextField tf: textFields) {
            tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
                // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
                if (!newValue) {
                    // Code wird ausgeführt, wenn das Feld VERLASSEN wird
                    storeValuesIntoRegistry();
                }
            });
        }
    }

    private void addUpdateListener(TextField ... textFields) {
        for (TextField tf : textFields) {
            tf.textProperty().addListener((observable, oldText, newText) -> {
                if (newText != null && !newText.isEmpty()) {
                    recalculateProfit();
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
    private void doLoadConfig(Data.KeyValue selectedKeyValue) {
        data.store(Data.BASEPATH, "lastSelectedKey", selectedKeyValue.getKey());
        data.store(selectedKeyValue.getKey(), "label", selectedKeyValue.getValue());
        selectedKey = selectedKeyValue.getKey();

        readValuesFromRegistry();
    }

    private void doNewConfig(String newValue) {
        Data.KeyValue changedKeyValue = new Data.KeyValue(getSelectedKey(newValue), newValue);
        cbChoose.getItems().add(changedKeyValue);
        cbChoose.getSelectionModel().select(changedKeyValue);
        doLoadConfig(changedKeyValue);

        readValuesFromRegistry();
        storeValuesIntoRegistry();
    }

    private void doDeleteConfig(Data.KeyValue selectedKeyValue) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfiguration löschen");
        alert.setHeaderText(String.format("<%s> wird gelöscht!", selectedKeyValue.getValue()));
        alert.setContentText("Sind Sie sicher?");
        alert.initOwner(mainStage);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            data.deleteConfiguration(selectedKeyValue.getKey());
            cbChoose.getItems().remove(selectedKeyValue);
            if (!getConfigurationLabels().isEmpty()) {
                Data.KeyValue newSelectedKeyValue = getConfigurationLabels().getFirst();
                cbChoose.getSelectionModel().select(newSelectedKeyValue);
                doLoadConfig(newSelectedKeyValue);
            }
        }
    }

    private void doAnalysis() {
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

        // Do calculation
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
        storeValuesIntoRegistry();
    }

    private void doHistory() {
        System.out.println("do Historie");
    }

    private void doAccept() {
        System.out.println("do Accept");
        txtLastKonto1.setInt(txtNewKonto1.getInt());
        txtLastKonto2.setInt(txtNewKonto2.getInt());
        txtLastNumber3a.setInt(txtNewNumber3a.getInt());
        txtLastPrice3a.setDouble(txtLastPrice3a.getDouble());
        txtLastNumber3b.setInt(txtLastNumber3b.getInt());
        txtLastPrice3b.setDouble(txtLastPrice3b.getDouble());
    }

    private List<Data.KeyValue> getConfigurationLabels() {
        return data.getChildrenValues(Data.BASEPATH,"label");
    }

    private Optional<Data.KeyValue> getConfiguration(String selectedKey) {
        return data.getChildrenValues(Data.BASEPATH,"label").stream()
                .filter(keyValue -> keyValue.getKey().equals(selectedKey)).findFirst();
    }

    private String getSelectedKey(String selectedString) {
        if (selectedString == null) {
            return null;
        }
        return selectedString.replace(" ", "").replace("/", "_");
    }

    private void storeValuesIntoRegistry() {
        data.store(selectedKey, "txtLastKonto1", txtLastKonto1.getInt());
        data.store(selectedKey, "txtLastKonto2", txtLastKonto2.getInt());
        data.store(selectedKey, "txtLastAmount3a", txtLastNumber3a.getInt());
        data.store(selectedKey, "txtLastPrice3a", txtLastPrice3a.getDouble());
        data.store(selectedKey, "txtLastAmount3b", txtLastNumber3b.getInt());
        data.store(selectedKey, "txtLastPrice3b", txtLastPrice3b.getDouble());

        data.store(selectedKey, "txtKonto1", txtKonto1.getInt());
        data.store(selectedKey, "txtKonto2", txtKonto2.getInt());
        data.store(selectedKey, "txtAmount3a", txtNumber3a.getInt());
        data.store(selectedKey, "txtPrice3a", txtPrice3a.getDouble());
        data.store(selectedKey, "txtAmount3b", txtNumber3b.getInt());
        data.store(selectedKey, "txtPrice3b", txtPrice3b.getDouble());

        data.store(selectedKey, "txtNewKonto1", txtNewKonto1.getInt());
        data.store(selectedKey, "txtNewKonto2", txtNewKonto2.getInt());
        data.store(selectedKey, "txtNewAmount3a", txtNewNumber3a.getInt());
        data.store(selectedKey, "txtNewPrice3a", txtNewPrice3a.getDouble());
        data.store(selectedKey, "txtNewAmount3b", txtNewNumber3b.getInt());
        data.store(selectedKey, "txtNewPrice3b", txtNewPrice3b.getDouble());

        data.store(selectedKey, "txtBezugJahr", txtBezugJahr.getInt());

        data.store(selectedKey, "txtKonto3aTicker", txtKonto3aTicker.getText());
        data.store(selectedKey, "txtKonto3bTicker", txtKonto3bTicker.getText());
        data.store(selectedKey, "txtRebalancing3aPercent", txtRebalancing3aPercent.getInt());
        data.store(selectedKey, "txtRebalancing3bPercent", txtRebalancing3bPercent.getInt());
        data.store(selectedKey, "txtRebalancingThreshold", txtRebalancingThreshold.getInt());

        data.store(selectedKey, "txtAusgabeGross", txtAusgabeGross.getText());
    }

    private void readValuesFromRegistry() {
        txtLastKonto1.setInt(data.getInteger(selectedKey, "txtLastKonto1"));
        txtLastKonto2.setInt(data.getInteger(selectedKey, "txtLastKonto2"));
        txtLastNumber3a.setInt(data.getInteger(selectedKey, "txtLastAmount3a"));
        txtLastPrice3a.setDouble(data.getDouble(selectedKey, "txtLastPrice3a"));
        txtLastNumber3b.setInt(data.getInteger(selectedKey, "txtLastAmount3b"));
        txtLastPrice3b.setDouble(data.getDouble(selectedKey, "txtLastPrice3b"));

        txtKonto1.setInt(data.getInteger(selectedKey, "txtKonto1"));
        txtKonto2.setInt(data.getInteger(selectedKey, "txtKonto2"));
        txtNumber3a.setInt(data.getInteger(selectedKey, "txtAmount3a"));
        txtPrice3a.setDouble(data.getDouble(selectedKey, "txtPrice3a"));
        txtNumber3b.setInt(data.getInteger(selectedKey, "txtAmount3b"));
        txtPrice3b.setDouble(data.getDouble(selectedKey, "txtPrice3b"));

        txtNewKonto1.setInt(data.getInteger(selectedKey, "txtNewKonto1"));
        txtNewKonto2.setInt(data.getInteger(selectedKey, "txtNewKonto2"));
        txtNewNumber3a.setInt(data.getInteger(selectedKey, "txtNewAmount3a"));
        txtNewPrice3a.setDouble(data.getDouble(selectedKey, "txtNewPrice3a"));
        txtNewNumber3b.setInt(data.getInteger(selectedKey, "txtNewAmount3b"));
        txtNewPrice3b.setDouble(data.getDouble(selectedKey, "txtNewPrice3b"));

        txtBezugJahr.setInt(data.getInteger(selectedKey, "txtBezugJahr"));

        txtHighestPrice3a.setDouble(data.getDouble(selectedKey, "txtHighestPrice3a"));
        txtHighestPrice3b.setDouble(data.getDouble(selectedKey, "txtHighestPrice3b"));

        String txtKonto3aTickerText = data.getString(selectedKey,"txtKonto3aTicker");
        if (txtKonto3aTickerText != null) {
            txtKonto3aTicker.setText(txtKonto3aTickerText);
        }
        String txtKonto3bTickerText = data.getString(selectedKey,"txtKonto3bTicker");
        if (txtKonto3bTickerText != null) {
            txtKonto3bTicker.setText(txtKonto3bTickerText);
        }
        txtRebalancing3aPercent.setInt(data.getInteger(selectedKey, "txtRebalancing3aPercent"));
        txtRebalancing3bPercent.setInt(data.getInteger(selectedKey, "txtRebalancing3bPercent"));
        txtRebalancingThreshold.setInt(data.getInteger(selectedKey, "txtRebalancingThreshold"));

        String txtAusgabeGrossText = data.getString(selectedKey, "txtAusgabeGross");
        txtAusgabeGross.setText(Objects.requireNonNullElse(txtAusgabeGrossText, ""));

        recalculateProfit();
        recalculateAccount3();
        updateTitle();
    }

    private void updateTitle() {
        int letzterAccount = txtLastKonto1.getInt() + txtLastKonto2.getInt() + txtReadonlyLastKonto3a.getInt() + txtReadonlyLastKonto3b.getInt();
        tbLastAccount.setTitle(String.format("Letzter Kontostand: %s CHF",
                getDecimalFormatter().format(letzterAccount)));
        int aktuellerAccount = txtKonto1.getInt() + txtKonto2.getInt() + txtReadonlyKonto3a.getInt() + txtReadonlyKonto3b.getInt();
        tbCurrentAccount.setTitle(String.format("Aktueller Kontostand: %s CHF",
                getDecimalFormatter().format(aktuellerAccount)));
        int neuerAccount = txtNewKonto1.getInt() + txtNewKonto2.getInt() + txtReadonlyNewKonto3a.getInt() + txtReadonlyNewKonto3b.getInt();
        tbNewAccount.setTitle(String.format("Neuer Kontostand: %s CHF",
                getDecimalFormatter().format(neuerAccount)));
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
            double lastHighestPrice3a = data.getDouble(selectedKey, "txtHighestPrice3a", 0);
            double currentPrice3a = txtPrice3a.getDouble();
            if (currentPrice3a > highestPrice3a) {
                txtHighestPrice3a.setDouble(currentPrice3a);
            } else if (lastHighestPrice3a > 0) {
                txtHighestPrice3a.setDouble(lastHighestPrice3a);
            }
        }
        if (txtHighestPrice3b.getText() == null || txtHighestPrice3b.getText().isBlank()
                || txtHighestPrice3b.getDouble() == 0) {
            txtHighestPrice3b.setText(txtPrice3b.getText());
        } else {
            double highestPrice3b = txtHighestPrice3b.getDouble();
            double lastHighestPrice3b = data.getDouble(selectedKey, "txtHighestPrice3b", 0);
            double currentPrice3b = txtPrice3b.getDouble();
            if (currentPrice3b > highestPrice3b) {
                txtHighestPrice3b.setDouble(currentPrice3b);
            } else if (lastHighestPrice3b > 0) {
                txtHighestPrice3b.setDouble(lastHighestPrice3b);
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
        txtReadonlyKonto1Max.setInt(threshold.getPot1Max());
        txtReadonlyKonto2Min.setInt(threshold.getPot2Min());
        txtReadonlyKonto2Opt.setInt(threshold.getPot2Opt());
        txtReadonlyKonto2Max.setInt(threshold.getPot2Max());
    }

    private String getVerteilungText(double val1, double val2) {
        double total = val1 + val2;
        double p1 = total != 0? val1 * 100 / total: val1;
        double p2 = total != 0? val2 * 100 / total: val2;
        return Math.round(p1) + "%/ " + Math.round(p2) + "%";
    }

    private DecimalFormat getDecimalFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        return new DecimalFormat("#,###", symbols);
    }


    protected DecimalFormat getDoubleFormatter() {
        // Schweizer Hochkomma-Format definieren
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        return new DecimalFormat("#,###.00", symbols);
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
        String ausgabeText = "";
        if (newState.getChange().getFrom1To3() > 0) {
            ausgabeText += String.format("Konto 1: -%s CHF\n", getDecimalFormatter().format(newState.getChange().getFrom1To3()));
            ausgabeText += String.format(" -> Kauf Konto 3: %s CHF (3a: %s ETF, 3b: %s ETF)\n",
                    getDecimalFormatter().format(newState.getChange().getFrom1To3()),
                    getDecimalFormatter().format(newState.getChange().getChangeShares3a()),
                    getDecimalFormatter().format(newState.getChange().getChangeShares3b()));
            ausgabeText += "\n";
        }
        if (newState.getChange().getFrom2To1() > 0 || newState.getChange().getFrom2To3() > 0) {
            int sellKonto2 = newState.getChange().getFrom2To1() + newState.getChange().getFrom2To3();
            ausgabeText += String.format("Verkauf Konto 2: %s CHF\n", getDecimalFormatter().format(sellKonto2));
            if (newState.getChange().getFrom2To1() > 0) {
                ausgabeText += String.format(" -> Einlage Konto 1: %s CHF\n", getDecimalFormatter().format(newState.getChange().getFrom2To1()));
            }
            if (newState.getChange().getFrom2To3() > 0) {
                ausgabeText += String.format(" -> Kauf Konto 3: %s CHF (3a: %s ETF, 3b: %s ETF)\n",
                        getDecimalFormatter().format(newState.getChange().getFrom2To3()),
                        getDecimalFormatter().format(newState.getChange().getChangeShares3a()),
                        getDecimalFormatter().format(newState.getChange().getChangeShares3b()));
            }
            ausgabeText += "\n";
        }
        if (newState.getChange().getFrom3To1() > 0 || newState.getChange().getFrom3To2() > 0) {
            int sellKonto3 = newState.getChange().getFrom3To1() + newState.getChange().getFrom3To2();
            ausgabeText += String.format("Verkauf Konto 3: %s CHF (3a: %s ETF, 3b: %s ETF)\n",
                    getDecimalFormatter().format(sellKonto3),
                    getDecimalFormatter().format(newState.getChange().getChangeShares3a()),
                    getDecimalFormatter().format(newState.getChange().getChangeShares3b()));
            if (newState.getChange().getFrom3To1() > 0) {
                ausgabeText += String.format(" -> Einlage Konto 1: %s CHF\n", getDecimalFormatter().format(newState.getChange().getFrom3To1()));
            }
            if (newState.getChange().getFrom3To2() > 0) {
                ausgabeText += String.format(" -> Einlage Konto 2: %s CHF\n", getDecimalFormatter().format(newState.getChange().getFrom3To2()));
            }
            ausgabeText += "\n";
        }
        if (newState.getChange().isChangedHighestPrice3a()) {
            ausgabeText += String.format("Neuer Höchststand 3a: %s CHF\n", getDoubleFormatter().format(newState.getHighestPrice3a()));
        }
        if (newState.getChange().isChangedHighestPrice3b()) {
            ausgabeText += String.format("Neuer Höchststand 3b: %s CHF\n", getDoubleFormatter().format(newState.getHighestPrice3b()));
        }
        txtAusgabeGross.setText(ausgabeText);
    }
}
