package ch.immi.pension;

import ch.immi.pension.data.AccountRebalancing;
import ch.immi.pension.data.PotsRebalancing;
import ch.immi.pension.data.Threshold;
import ch.immi.pension.javafx.*;
import ch.immi.pension.persistence.Data;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Application extends javafx.application.Application {

    private final Data data = Data.getInstance();

    private final TitledBorder tbLetzerKontostand = new TitledBorder("Letzter Kontostand");
    private final TitledBorder tbAktuellerKontostand = new TitledBorder("Aktueller Kontostand");
    private final TitledBorder tbNeuerKontostand = new TitledBorder("Neuer Kontostand");

    private final IntegerTextField txtLastKonto1 = new IntegerTextField(70, 7);
    private final IntegerTextField txtLastKonto2 = new IntegerTextField(70, 7);
    private final IntegerTextField txtLastAmount3a = new IntegerTextField(45, 4);
    private final FloatTextField txtLastPrice3a = new FloatTextField(60, 6);
    private final IntegerTextField txtLastKonto3a = new IntegerTextField(70, 7);
    private final IntegerTextField txtLastAmount3b = new IntegerTextField(45, 4);
    private final FloatTextField txtLastPrice3b = new FloatTextField(60, 6);
    private final IntegerTextField txtLastKonto3b = new IntegerTextField(70, 7);
    private final Label lblLastVerteilung = new Label("40%/60%");

    private final IntegerTextField txtKonto1 = new IntegerTextField(80, 7);
    private final IntegerTextField txtKonto2 = new IntegerTextField(80, 7);
    private final IntegerTextField txtKonto3a = new IntegerTextField(80, 7);
    private final IntegerTextField txtKonto3b = new IntegerTextField(80, 7);
    private final Label lblVerteilung = new Label("40%/60%");

    private final IntegerTextField txtNewKonto1 = new IntegerTextField(80, 7);
    private final IntegerTextField txtNewKonto2 = new IntegerTextField(80, 7);
    private final IntegerTextField txtNewKonto3a = new IntegerTextField(80, 7);
    private final IntegerTextField txtNewKonto3b = new IntegerTextField(80, 7);
    private final Label lblNewVerteilung = new Label("40%/60%");

    private final Label lblProfit2 = new Label("5%");
    private final Label lblProfit3a = new Label("5%");
    private final Label lblProfit3b = new Label("5%");

    private final IntegerTextField txtBezugJahr = new IntegerTextField(80, 7);
    private final Label lblBezugJahrPercentage = new Label("0%");

    private final IntegerTextField txtKonto1Min = new IntegerTextField(80, 7);
    private final IntegerTextField txtKonto1Opt = new IntegerTextField(80, 7);
    private final IntegerTextField txtKonto1Max = new IntegerTextField(80, 7);

    private final IntegerTextField txtKonto2Min = new IntegerTextField(80, 7);
    private final IntegerTextField txtKonto2Opt = new IntegerTextField(80, 7);
    private final IntegerTextField txtKonto2Max = new IntegerTextField(80, 7);

    private final FloatTextField txtHighestPrice3a = new FloatTextField(60, 3);
    private final FloatTextField txtHighestPrice3b = new FloatTextField(60, 3);

    private final IntegerTextField txtRebalancing3aPercent = new IntegerTextField(40, 3);
    private final IntegerTextField txtRebalancing3bPercent = new IntegerTextField(40, 3);
    private final IntegerTextField txtRebalancingThreshold = new IntegerTextField(40, 3);

    private final TextArea txtAusgabeGross = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rebalancing berechnen..");

        // Haupt-Layout: Vertikal gestapelt (Oben: Spalten, Mitte: Button, Unten: Textfeld)
        VBox rootLayout = new VBox(15);
        rootLayout.setPadding(new Insets(15));
        rootLayout.setAlignment(Pos.TOP_CENTER);

        // ==========================================
        // OBERER BEREICH: Horizontal geteilt
        // ==========================================
        HBox obererBereich = new HBox(5);
        obererBereich.setAlignment(Pos.TOP_LEFT);

        // --- LINKS: Kontostand ---
        VBox linkerBereich = new VBox(5);
        linkerBereich.getChildren().addAll(letzterKontoStandBox(), aktuellerKontoStandBox());

        // --- RECHTS: Parameter ---
        VBox rechterBereich = parameterBox();

        // Beide Bereiche in den oberen Container legen
        obererBereich.getChildren().addAll(linkerBereich, rechterBereich);

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
        HBox untererBereich = new HBox(5);
        untererBereich.setAlignment(Pos.TOP_LEFT);

        untererBereich.getChildren().addAll(textAusgabeArea(), neuerKontoStandBox());

        // Alles in das Haupt-Layout einfügen (von oben nach unten gestapelt)
        rootLayout.getChildren().addAll(obererBereich, buttonBox, untererBereich);

        // Fenstergrösse angepasst (Breite: 760, Höhe: 700 wegen dem neuen Textfeld)
        Scene scene = new Scene(rootLayout, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        readValuesFromRegistry();
    }

    private VBox letzterKontoStandBox() {
        tbLetzerKontostand.setPrefWidth(320);
        tbLetzerKontostand.setAlignment(Pos.TOP_LEFT);

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
        gridLinks.add(txtLastAmount3a, 1, 2);
        gridLinks.add(txtLastPrice3a, 2, 2);
        gridLinks.add(txtLastKonto3a, 3, 2);
        txtLastKonto3a.setReadonly(true);
        gridLinks.add(new Label("CHF"), 4, 2);
        addAutoSaveListener(txtLastAmount3a, txtLastPrice3a);
        addUpdateListener(txtLastAmount3a, txtLastPrice3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 3);
        gridLinks.add(txtLastAmount3b, 1, 3);
        gridLinks.add(txtLastPrice3b, 2, 3);
        gridLinks.add(txtLastKonto3b, 3, 3);
        txtLastKonto3b.setReadonly(true);
        gridLinks.add(new Label("CHF"), 4, 3);
        addAutoSaveListener(txtLastAmount3b, txtLastPrice3b);
        addUpdateListener(txtLastAmount3b, txtLastPrice3b);

        gridLinks.add(lblLastVerteilung, 3, 4);

        tbLetzerKontostand.getChildren().add(gridLinks);

        return tbLetzerKontostand;
    }

    private VBox aktuellerKontoStandBox() {
        tbAktuellerKontostand.setPrefWidth(320);
        tbAktuellerKontostand.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGridPane();
        gridLinks.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        gridLinks.add(new Label("Konto 1:"), 0, 0);
        gridLinks.add(txtKonto1, 1, 0);
        gridLinks.add(new Label("CHF"), 2, 0);
        addAutoSaveListener(txtKonto1);
        addUpdateListener(txtKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 1);
        gridLinks.add(txtKonto2, 1, 1);
        gridLinks.add(new Label("CHF"), 2, 1);
        gridLinks.add(lblProfit2, 4, 1);
        addAutoSaveListener(txtKonto2);
        addUpdateListener(txtKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 2);
        gridLinks.add(txtKonto3a, 1, 2);
        gridLinks.add(new Label("CHF"), 2, 2);
        gridLinks.add(lblProfit3a, 4, 2);
        addAutoSaveListener(txtKonto3a);
        addUpdateListener(txtKonto3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 3);
        gridLinks.add(txtKonto3b, 1, 3);
        gridLinks.add(new Label("CHF"), 2, 3);
        gridLinks.add(lblProfit3b, 4, 3);
        addAutoSaveListener(txtKonto3b);
        addUpdateListener(txtKonto3b);

        gridLinks.add(lblVerteilung, 1, 4);

        tbAktuellerKontostand.getChildren().add(gridLinks);

        return tbAktuellerKontostand;
    }

    private VBox parameterBox() {
        VBox rechterBereich = new TitledBorder("Parameter");
        rechterBereich.setPrefWidth(500);

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
        gridRechtsKonto12.add(txtKonto1Min, 1, 2);
        txtKonto1Min.setReadonly(true);
        gridRechtsKonto12.add(txtKonto1Opt, 2, 2);
        txtKonto1Opt.setReadonly(true);
        gridRechtsKonto12.add(txtKonto1Max, 3, 2);
        txtKonto1Max.setReadonly(true);

        gridRechtsKonto12.add(new Label("Konto 2:"), 0, 3);
        gridRechtsKonto12.add(txtKonto2Min, 1, 3);
        txtKonto2Min.setReadonly(true);
        gridRechtsKonto12.add(txtKonto2Opt, 2, 3);
        txtKonto2Opt.setReadonly(true);
        gridRechtsKonto12.add(txtKonto2Max, 3, 3);
        txtKonto2Max.setReadonly(true);

        VBox parameterKonto3Bereich = new TitledBorder("Konto 3a/3b");
        GridPane gridKonto3 = new StandartGridPane();
        gridKonto3.add(new Label("Höchststand 3a:"), 0, 0);
        gridKonto3.add(txtHighestPrice3a, 1, 0);
        gridKonto3.add(new Label("Höchststand 3b:"), 0, 1);
        gridKonto3.add(txtHighestPrice3b, 1, 1);
        parameterKonto3Bereich.getChildren().add(gridKonto3);

        VBox gridRebalancing3Bereich = new TitledBorder("Rebalancing 3a/3b");
        GridPane gridRebalancing3 = new StandartGridPane();
        gridRebalancing3.add(new Label("Verteilung 3a/3b:"), 0, 0);
        gridRebalancing3.add(txtRebalancing3aPercent, 1, 0);
        gridRebalancing3.add(new Label("%"), 2, 0);
        gridRebalancing3.add(new Label("/"), 3, 0);
        gridRebalancing3.add(txtRebalancing3bPercent, 4, 0);
        gridRebalancing3.add(new Label("%"), 5, 0);
        addAutoSaveListener(txtRebalancing3aPercent);
        addAutoSaveListener(txtRebalancing3bPercent);
        gridRebalancing3.add(new Label("Schwelle:"), 6, 0);
        gridRebalancing3.add(txtRebalancingThreshold, 7, 0);
        gridRebalancing3.add(new Label("%"), 8, 0);
        addAutoSaveListener(txtRebalancingThreshold);
        gridRebalancing3Bereich.getChildren().add(gridRebalancing3);

        rechterBereich.getChildren().addAll(gridRechtsKonto12, parameterKonto3Bereich, gridRebalancing3Bereich);
        return rechterBereich;
    }

    private TextArea textAusgabeArea() {
        txtAusgabeGross.setPromptText("Rebalancingvorschlag erscheint hier...");
        txtAusgabeGross.setPrefHeight(250); // Setzt eine grosszügige Standardhöhe
        txtAusgabeGross.setWrapText(true);   // Automatischer Zeilenumbruch bei langem Text
        return txtAusgabeGross;
    }

    private VBox neuerKontoStandBox() {
        tbNeuerKontostand.setPrefWidth(250);
        tbNeuerKontostand.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGridPane();
        gridLinks.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        gridLinks.add(new Label("Konto 1:"), 0, 0);
        gridLinks.add(txtNewKonto1, 1, 0);
        gridLinks.add(new Label("CHF"), 2, 0);
        addAutoSaveListener(txtNewKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 1);
        gridLinks.add(txtNewKonto2, 1, 1);
        gridLinks.add(new Label("CHF"), 2, 1);
        addAutoSaveListener(txtNewKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 2);
        gridLinks.add(txtNewKonto3a, 1, 2);
        gridLinks.add(new Label("CHF"), 2, 2);
        addAutoSaveListener(txtNewKonto3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 3);
        gridLinks.add(txtNewKonto3b, 1, 3);
        gridLinks.add(new Label("CHF"), 2, 3);
        addAutoSaveListener(txtNewKonto3b);

        gridLinks.add(lblNewVerteilung, 1, 4);

        Button btnHandle = new Button("Übernehmen..");
        btnHandle.setMaxWidth(Double.MAX_VALUE);
        btnHandle.setPrefHeight(35);
        btnHandle.setOnAction(event -> doAccept());

        tbNeuerKontostand.getChildren().add(gridLinks);
        tbNeuerKontostand.getChildren().add(btnHandle);

        return tbNeuerKontostand;
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
        for (TextField tf: textFields) {
            tf.textProperty().addListener((observable, oldText, newText) -> {
                // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
                if (newText != null && !newText.isEmpty()) {
                    recalculateProfit();
                    recalculateAccount3();
                    recalculateAccount3Percentage();
                    recalculateBezugJahrPercentage();
                    updateTitle();
                }
            });
        }
    }

    // Functionalities
    private void doAnalysis() {
        // Do pot rebalancing
        double profit2 = getProfit(txtLastKonto2.getText(), txtKonto2.getText());
        int lastKonto3 = txtLastKonto3a.getInt() + txtLastKonto3b.getInt();
        int konto3 = txtKonto3a.getInt() + txtKonto3b.getInt();
        double profit3 = getProfit(lastKonto3, konto3);

        PotsRebalancer potsRebalancer = new PotsRebalancer(txtKonto1.getInt(), txtKonto2.getInt(), konto3,
                profit2, profit3, txtBezugJahr.getInt());
        PotsRebalancing potsRebalancing = potsRebalancer.rebalancing();

        // Do account3 rebalancing
        AccountRebalancer accountRebalancer = new AccountRebalancer(txtKonto3a.getInt(), txtKonto3b.getInt());
        accountRebalancer.setParams(txtRebalancing3aPercent.getInt(), txtRebalancing3bPercent.getInt(), txtRebalancingThreshold.getInt());
        AccountRebalancing accountRebalancing = accountRebalancer.rebalance(potsRebalancing.getFrom3To1()+potsRebalancing.getFrom3To2()-potsRebalancing.getFrom1To3());

        // Do calculation
        txtNewKonto1.setTextReformat(Integer.toString(txtKonto1.getInt() + potsRebalancing.getFrom2To1() + potsRebalancing.getFrom3To1() - potsRebalancing.getFrom1To3()));
        txtNewKonto2.setTextReformat(Integer.toString(txtKonto2.getInt() + potsRebalancing.getFrom3To2() - potsRebalancing.getFrom2To1()));
        txtNewKonto3a.setTextReformat(Integer.toString(txtKonto3a.getInt() + accountRebalancing.getKonto3aChange()));
        txtNewKonto3b.setTextReformat(Integer.toString(txtKonto3b.getInt() + accountRebalancing.getKonto3bChange()));
        showText(potsRebalancing, accountRebalancing);

        updateTitle();
        storeValuesIntoRegistry();
    }

    private void doHistory() {
        System.out.println("do Historie");
    }

    private void doAccept() {
        System.out.println("do Accept");
        txtLastKonto1.setTextReformat(txtNewKonto1.getText());
        txtLastKonto2.setTextReformat(txtNewKonto2.getText());
        txtLastKonto3a.setTextReformat(txtNewKonto3a.getText());
        txtLastKonto3b.setTextReformat(txtNewKonto3b.getText());
    }

    private void storeValuesIntoRegistry() {
        data.store("txtLastKonto1", txtLastKonto1.getText());
        data.store("txtLastKonto2", txtLastKonto2.getText());
        data.store("txtLastAmount3a", txtLastAmount3a.getText());
        data.store("txtLastPrice3a", txtLastPrice3a.getText());
        data.store("txtLastAmount3b", txtLastAmount3b.getText());
        data.store("txtLastPrice3b", txtLastPrice3b.getText());

        data.store("txtKonto1", txtKonto1.getText());
        data.store("txtKonto2", txtKonto2.getText());
        data.store("txtKonto3a", txtKonto3a.getText());
        data.store("txtKonto3b", txtKonto3b.getText());

        data.store("txtNewKonto1", txtNewKonto1.getText());
        data.store("txtNewKonto2", txtNewKonto2.getText());
        data.store("txtNewKonto3a", txtNewKonto3a.getText());
        data.store("txtNewKonto3b", txtNewKonto3b.getText());

        data.store("txtBezugJahr", txtBezugJahr.getText());

        data.store("txtRebalancing3aPercent", txtRebalancing3aPercent.getText());
        data.store("txtRebalancing3bPercent", txtRebalancing3bPercent.getText());
        data.store("txtRebalancingThreshold", txtRebalancingThreshold.getText());

        data.store("txtAusgabeGross", txtAusgabeGross.getText());
    }

    private void readValuesFromRegistry() {
        txtLastKonto1.setTextReformat(data.getString("txtLastKonto1", "0"));
        txtLastKonto2.setTextReformat(data.getString("txtLastKonto2", "0"));
        txtLastKonto3a.setTextReformat(data.getString("txtLastKonto3a", "0"));
        txtLastKonto3b.setTextReformat(data.getString("txtLastKonto3b", "0"));

        txtKonto1.setTextReformat(data.getString("txtKonto1", "0"));
        txtKonto2.setTextReformat(data.getString("txtKonto2", "0"));
        txtLastAmount3a.setTextReformat(data.getString("txtLastAmount3a", "0"));
        txtLastPrice3a.setTextReformat(data.getString("txtLastPrice3a", "0"));
        txtLastAmount3b.setTextReformat(data.getString("txtLastAmount3b", "0"));
        txtLastPrice3b.setTextReformat(data.getString("txtLastPrice3b", "0"));

        txtNewKonto1.setTextReformat(data.getString("txtNewKonto1", "0"));
        txtNewKonto2.setTextReformat(data.getString("txtNewKonto2", "0"));
        txtNewKonto3a.setTextReformat(data.getString("txtNewKonto3a", "0"));
        txtNewKonto3b.setTextReformat(data.getString("txtNewKonto3b", "0"));

        txtBezugJahr.setTextReformat(data.getString("txtBezugJahr", "0"));

        txtRebalancing3aPercent.setTextReformat(data.getString("txtRebalancing3aPercent", "0"));
        txtRebalancing3bPercent.setTextReformat(data.getString("txtRebalancing3bPercent", "0"));
        txtRebalancingThreshold.setTextReformat(data.getString("txtRebalancingThreshold", "0"));

        txtAusgabeGross.setText(data.getString("txtAusgabeGross", ""));

        recalculateProfit();
        recalculateAccount3();
        updateTitle();
    }


    private void updateTitle() {
        int letzterKontostand = txtLastKonto1.getInt() + txtLastKonto2.getInt() + txtLastKonto3a.getInt() + txtLastKonto3b.getInt();
        tbLetzerKontostand.setTitle(String.format("Letzter Kontos: %s CHF",
                getDecimalFormatter().format(letzterKontostand)));
        int aktuellerKontostand = txtKonto1.getInt() + txtKonto2.getInt() + txtKonto3a.getInt() + txtKonto3b.getInt();
        tbAktuellerKontostand.setTitle(String.format("Aktueller Kontostand: %s CHF",
                getDecimalFormatter().format(aktuellerKontostand)));
        int neuerKontostand = txtNewKonto1.getInt() + txtNewKonto2.getInt() + txtNewKonto3a.getInt() + txtNewKonto3b.getInt();
        tbNeuerKontostand.setTitle(String.format("Neuer Kontostand: %s CHF",
                getDecimalFormatter().format(neuerKontostand)));
    }

    private void recalculateProfit() {
        lblProfit2.setText(getProfitAsText(txtLastKonto2.getText(), txtKonto2.getText()));
        lblProfit3a.setText(getProfitAsText(txtLastKonto3a.getText(), txtKonto3a.getText()));
        lblProfit3b.setText(getProfitAsText(txtLastKonto3b.getText(), txtKonto3b.getText()));
    }

    private void recalculateAccount3() {
        txtLastKonto3a.setTextReformat(Integer.toString(Math.round(txtLastAmount3a.getInt() * txtLastPrice3a.getFloat())));
        txtLastKonto3b.setTextReformat(Integer.toString(Math.round(txtLastAmount3b.getInt() * txtLastPrice3b.getFloat())));
    }

    private void recalculateAccount3Percentage() {
        lblLastVerteilung.setText(getVerteilungText(txtLastKonto3a.getInt(), txtLastKonto3b.getInt()));
        lblVerteilung.setText(getVerteilungText(txtKonto3a.getInt(), txtKonto3b.getInt()));
        lblNewVerteilung.setText(getVerteilungText(txtNewKonto3a.getInt(), txtNewKonto3b.getInt()));
    }

    private void recalculateBezugJahrPercentage() {
        double amount = txtLastKonto1.getInt() + txtLastKonto2.getInt() + txtLastKonto3a.getInt() + txtLastKonto3b.getInt();
        double bezug = txtBezugJahr.getInt();
        double percentage = amount != 0? bezug/ amount * 100: 0;
        lblBezugJahrPercentage.setText(String.format("%.2f", percentage) + "%");

        Threshold threshold = Threshold.of(txtBezugJahr.getInt());
        txtKonto1Min.setTextReformat(Integer.toString(threshold.getKonto1Min()));
        txtKonto1Opt.setTextReformat(Integer.toString(threshold.getKonto1Opt()));
        txtKonto1Max.setTextReformat(Integer.toString(threshold.getKonto1Max()));
        txtKonto2Min.setTextReformat(Integer.toString(threshold.getKonto2Min()));
        txtKonto2Opt.setTextReformat(Integer.toString(threshold.getKonto2Opt()));
        txtKonto2Max.setTextReformat(Integer.toString(threshold.getKonto2Max()));
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

    private void showText(PotsRebalancing potsRebalancing, AccountRebalancing accountRebalancing) {
        String ausgabeText = "";
        if (potsRebalancing.getFrom3To1() > 0) {
            ausgabeText += String.format("Konto 3 -> Konto 1: %s CHF\n", getDecimalFormatter().format(potsRebalancing.getFrom3To1()));
        }
        if (potsRebalancing.getFrom3To2() > 0) {
            ausgabeText += String.format("Konto 3 -> Konto 2: %s CHF\n", getDecimalFormatter().format(potsRebalancing.getFrom3To2()));
        }
        if (potsRebalancing.getFrom2To1() > 0) {
            ausgabeText += String.format("Konto 2 -> Konto 1: %s CHF\n", getDecimalFormatter().format(potsRebalancing.getFrom2To1()));
        }
        if (potsRebalancing.getFrom1To3() > 0) {
            ausgabeText += String.format("Konto 1 -> Konto 3: %s CHF\n", getDecimalFormatter().format(potsRebalancing.getFrom1To3()));
        }
        if (accountRebalancing.getKonto3aChange() > 0) {
            ausgabeText += String.format("Konto 3a: %s CHF\n", getDecimalFormatter().format(accountRebalancing.getKonto3aChange()));
        }
        if (accountRebalancing.getKonto3bChange() > 0) {
            ausgabeText += String.format("Konto 3b: %s CHF\n", getDecimalFormatter().format(accountRebalancing.getKonto3bChange()));
        }
        txtAusgabeGross.setText(ausgabeText);
    }
}
