package ch.immi.pension;

import ch.immi.pension.data.AccountRebalancing;
import ch.immi.pension.data.PotsRebalancing;
import ch.immi.pension.javafx.StandartGripPane;
import ch.immi.pension.persistence.Data;
import ch.immi.pension.javafx.NumberTextField;
import ch.immi.pension.javafx.TitledBorder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Application extends javafx.application.Application {

    final Data data = Data.getInstance();

    NumberTextField txtLastKonto1 = new NumberTextField(80, 7);
    NumberTextField txtLastKonto2 = new NumberTextField(80, 7);
    NumberTextField txtLastKonto3a = new NumberTextField(80, 7);
    NumberTextField txtLastKonto3b = new NumberTextField(80, 7);
    Label lblLastVerteilung = new Label("40%/60%");

    NumberTextField txtKonto1 = new NumberTextField(80, 7);
    NumberTextField txtKonto2 = new NumberTextField(80, 7);
    NumberTextField txtKonto3a = new NumberTextField(80, 7);
    NumberTextField txtKonto3b = new NumberTextField(80, 7);
    Label lblVerteilung = new Label("40%/60%");

    NumberTextField txtNewKonto1 = new NumberTextField(80, 7);
    NumberTextField txtNewKonto2 = new NumberTextField(80, 7);
    NumberTextField txtNewKonto3a = new NumberTextField(80, 7);
    NumberTextField txtNewKonto3b = new NumberTextField(80, 7);
    Label lblNewVerteilung = new Label("40%/60%");

    Label lblProfit2 = new Label("5%");
    Label lblProfit3a = new Label("5%");
    Label lblProfit3b = new Label("5%");

    NumberTextField txtKonto1Crit = new NumberTextField(80, 7);
    NumberTextField txtKonto1Min = new NumberTextField(80, 7);
    NumberTextField txtKonto1Max = new NumberTextField(80, 7);

    NumberTextField txtKonto2Crit = new NumberTextField(80, 7);
    NumberTextField txtKonto2Min = new NumberTextField(80, 7);
    NumberTextField txtKonto2Max = new NumberTextField(80, 7);

    NumberTextField txtKonto3Plus10 = new NumberTextField(40, 3);
    NumberTextField txtKonto3Plus5 = new NumberTextField(40, 3);
    NumberTextField txtKonto3Min20 = new NumberTextField(40, 3);
    NumberTextField txtKonto3Min30 = new NumberTextField(40, 3);

    NumberTextField txtRebalancing3aPercent = new NumberTextField(40, 3);
    NumberTextField txtRebalancing3bPercent = new NumberTextField(40, 3);
    NumberTextField txtRebalancingThreshold = new NumberTextField(40, 3);

    TextArea txtAusgabeGross = new TextArea();

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
        btnHistory.setOnAction(event -> {
            doHistory();
        });

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
        Scene scene = new Scene(rootLayout, 850, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        readValuesFromRegistry();
    }

    private VBox letzterKontoStandBox() {
        VBox linkerBereich = new TitledBorder("Letzter Kontostand");
        linkerBereich.setPrefWidth(250);
        linkerBereich.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGripPane();
        gridLinks.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        gridLinks.add(new Label("Konto 1:"), 0, 0);
        gridLinks.add(txtLastKonto1, 1, 0);
        gridLinks.add(new Label("CHF"), 2, 0);
        addAutoSaveListener(txtLastKonto1);
        addRecalculateProfitListener(txtLastKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 1);
        gridLinks.add(txtLastKonto2, 1, 1);
        gridLinks.add(new Label("CHF"), 2, 1);
        addAutoSaveListener(txtLastKonto2);
        addRecalculateProfitListener(txtLastKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 2);
        gridLinks.add(txtLastKonto3a, 1, 2);
        gridLinks.add(new Label("CHF"), 2, 2);
        addAutoSaveListener(txtLastKonto3a);
        addRecalculateProfitListener(txtLastKonto3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 3);
        gridLinks.add(txtLastKonto3b, 1, 3);
        gridLinks.add(new Label("CHF"), 2, 3);
        addAutoSaveListener(txtLastKonto3b);
        addRecalculateProfitListener(txtLastKonto3b);

        gridLinks.add(lblLastVerteilung, 1, 4);

        linkerBereich.getChildren().add(gridLinks);

        return linkerBereich;
    }

    private VBox aktuellerKontoStandBox() {
        VBox linkerBereich = new TitledBorder("Aktueller Kontostand");
        linkerBereich.setPrefWidth(250);
        linkerBereich.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGripPane();
        gridLinks.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        gridLinks.add(new Label("Konto 1:"), 0, 0);
        gridLinks.add(txtKonto1, 1, 0);
        gridLinks.add(new Label("CHF"), 2, 0);
        addAutoSaveListener(txtKonto1);
        addRecalculateProfitListener(txtKonto1);

        gridLinks.add(new Label("Konto 2:"), 0, 1);
        gridLinks.add(txtKonto2, 1, 1);
        gridLinks.add(new Label("CHF"), 2, 1);
        gridLinks.add(lblProfit2, 4, 1);
        addAutoSaveListener(txtKonto2);
        addRecalculateProfitListener(txtKonto2);

        gridLinks.add(new Label("Konto 3a:"), 0, 2);
        gridLinks.add(txtKonto3a, 1, 2);
        gridLinks.add(new Label("CHF"), 2, 2);
        gridLinks.add(lblProfit3a, 4, 2);
        addAutoSaveListener(txtKonto3a);
        addRecalculateProfitListener(txtKonto3a);

        gridLinks.add(new Label("Konto 3b:"), 0, 3);
        gridLinks.add(txtKonto3b, 1, 3);
        gridLinks.add(new Label("CHF"), 2, 3);
        gridLinks.add(lblProfit3b, 4, 3);
        addAutoSaveListener(txtKonto3b);
        addRecalculateProfitListener(txtKonto3b);

        gridLinks.add(lblVerteilung, 1, 4);

        linkerBereich.getChildren().add(gridLinks);

        return linkerBereich;
    }

    private VBox parameterBox() {
        VBox rechterBereich = new TitledBorder("Parameter");
        rechterBereich.setPrefWidth(450);

        GridPane gridRechtsKonto12 = new StandartGripPane();
        gridRechtsKonto12.add(new Label("krit"), 1, 0);
        gridRechtsKonto12.add(new Label("min"), 2, 0);
        gridRechtsKonto12.add(new Label("max"), 3, 0);

        gridRechtsKonto12.add(new Label("Konto 1:"), 0, 1);
        gridRechtsKonto12.add(txtKonto1Crit, 1, 1);
        addAutoSaveListener(txtKonto1Crit);
        gridRechtsKonto12.add(txtKonto1Min, 2, 1);
        addAutoSaveListener(txtKonto1Min);
        gridRechtsKonto12.add(txtKonto1Max, 3, 1);
        addAutoSaveListener(txtKonto1Max);

        gridRechtsKonto12.add(new Label("Konto 2:"), 0, 2);
        gridRechtsKonto12.add(txtKonto2Crit, 1, 2);
        addAutoSaveListener(txtKonto2Crit);
        gridRechtsKonto12.add(txtKonto2Min, 2, 2);
        addAutoSaveListener(txtKonto2Min);
        gridRechtsKonto12.add(txtKonto2Max, 3, 2);
        addAutoSaveListener(txtKonto2Max);

        VBox parameterKonto3Bereich = new TitledBorder("Konto 3");
        GridPane gridRechtsKonto3 = new StandartGripPane();

        gridRechtsKonto3.add(new Label("+10%:"), 0, 0);
        gridRechtsKonto3.add(txtKonto3Plus10, 1, 0);
        gridRechtsKonto3.add(new Label("% sichern"), 2, 0);
        addAutoSaveListener(txtKonto3Plus10);

        gridRechtsKonto3.add(new Label("+5%:"), 0, 1);
        gridRechtsKonto3.add(txtKonto3Plus5, 1, 1);
        gridRechtsKonto3.add(new Label("% sichern"), 2, 1);
        addAutoSaveListener(txtKonto3Plus5);

        gridRechtsKonto3.add(new Label("-20%:"), 4, 0);
        gridRechtsKonto3.add(txtKonto3Min20, 5, 0);
        gridRechtsKonto3.add(new Label("% von Konto 1"), 6, 0);
        addAutoSaveListener(txtKonto3Min20);

        gridRechtsKonto3.add(new Label("-30%:"), 4, 1);
        gridRechtsKonto3.add(txtKonto3Min30, 5, 1);
        gridRechtsKonto3.add(new Label("% von Konto 1"), 6, 1);
        addAutoSaveListener(txtKonto3Min30);

        parameterKonto3Bereich.getChildren().add(gridRechtsKonto3);

        VBox parameterRebalancing3Bereich = new TitledBorder("Rebalancing 3a/3b");
        GridPane gridRebalancing3 = new StandartGripPane();

        gridRebalancing3.add(new Label("Verteilung 3a/3b:"), 0, 0);
        gridRebalancing3.add(txtRebalancing3aPercent, 1, 0);
        gridRebalancing3.add(new Label("%"), 2, 0);
        gridRebalancing3.add(new Label("/"), 3, 0);
        gridRebalancing3.add(txtRebalancing3bPercent, 4, 0);
        gridRebalancing3.add(new Label("%"), 5, 0);
        addAutoSaveListener(txtRebalancing3aPercent);
        addAutoSaveListener(txtRebalancing3bPercent);

        gridRebalancing3.add(new Label("Schwelle:"), 0, 1);
        gridRebalancing3.add(txtRebalancingThreshold, 1, 1);
        gridRebalancing3.add(new Label("%"), 2, 1);
        addAutoSaveListener(txtRebalancingThreshold);

        parameterRebalancing3Bereich.getChildren().add(gridRebalancing3);

        rechterBereich.getChildren().addAll(gridRechtsKonto12, parameterKonto3Bereich, parameterRebalancing3Bereich);
        return rechterBereich;
    }

    private TextArea textAusgabeArea() {
        txtAusgabeGross.setPromptText("Rebalancingvorschlag erscheint hier...");
        txtAusgabeGross.setPrefHeight(250); // Setzt eine grosszügige Standardhöhe
        txtAusgabeGross.setWrapText(true);   // Automatischer Zeilenumbruch bei langem Text
        return txtAusgabeGross;
    }

    private VBox neuerKontoStandBox() {
        VBox linkerBereich = new TitledBorder("Neuer Kontostand");
        linkerBereich.setPrefWidth(250);
        linkerBereich.setAlignment(Pos.TOP_LEFT);

        GridPane gridLinks = new StandartGripPane();
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
        btnHandle.setOnAction(event -> {
            doAccept();
        });

        linkerBereich.getChildren().add(gridLinks);
        linkerBereich.getChildren().add(btnHandle);

        return linkerBereich;
    }

    private void addAutoSaveListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (!newValue) {
                // Code wird ausgeführt, wenn das Feld VERLASSEN wird
                System.out.println("textField " + textField.getText() + " verlassen!");
                storeValuesIntoRegistry();
            }
        });
    }

    private void addRecalculateProfitListener(TextField textField) {
        textField.textProperty().addListener((observable, oldText, newText) -> {
            // 'newValue' ist true, wenn das Feld betreten wird, und false, wenn es verlassen wird
            if (newText != null && !newText.isEmpty()) {
                recalculateProfit();
                recalculateAccount3Percentage();
            }
        });
    }

    private void storeValuesIntoRegistry() {
        System.out.println("do Store");
        data.store("txtLastKonto1", txtLastKonto1.getText());
        data.store("txtLastKonto2", txtLastKonto2.getText());
        data.store("txtLastKonto3a", txtLastKonto3a.getText());
        data.store("txtLastKonto3b", txtLastKonto3b.getText());
        data.store("txtKonto1", txtKonto1.getText());
        data.store("txtKonto2", txtKonto2.getText());
        data.store("txtKonto3a", txtKonto3a.getText());
        data.store("txtKonto3b", txtKonto3b.getText());
        data.store("txtNewKonto1", txtNewKonto1.getText());
        data.store("txtNewKonto2", txtNewKonto2.getText());
        data.store("txtNewKonto3a", txtNewKonto3a.getText());
        data.store("txtNewKonto3b", txtNewKonto3b.getText());

        data.store("txtKonto1Crit", txtKonto1Crit.getText());
        data.store("txtKonto1Min", txtKonto1Min.getText());
        data.store("txtKonto1Max", txtKonto1Max.getText());
        data.store("txtKonto2Crit", txtKonto2Crit.getText());
        data.store("txtKonto2Min", txtKonto2Min.getText());
        data.store("txtKonto2Max", txtKonto2Max.getText());

        data.store("txtKonto3Min10", txtKonto3Min30.getText());
        data.store("txtKonto3Min5", txtKonto3Min20.getText());
        data.store("txtKonto3Plus5", txtKonto3Plus5.getText());
        data.store("txtKonto3Plus10", txtKonto3Plus10.getText());

        data.store("txtRebalancing3aPercent", txtRebalancing3aPercent.getText());
        data.store("txtRebalancing3bPercent", txtRebalancing3bPercent.getText());
        data.store("txtRebalancingThreshold", txtRebalancingThreshold.getText());

    }

    private void readValuesFromRegistry() {
        System.out.println("do Store");
        txtLastKonto1.setText(data.getString("txtLastKonto1", "0"));
        txtLastKonto2.setText(data.getString("txtLastKonto2", "0"));
        txtLastKonto3a.setText(data.getString("txtLastKonto3a", "0"));
        txtLastKonto3b.setText(data.getString("txtLastKonto3b", "0"));
        txtKonto1.setText(data.getString("txtKonto1", "0"));
        txtKonto2.setText(data.getString("txtKonto2", "0"));
        txtKonto3a.setText(data.getString("txtKonto3a", "0"));
        txtKonto3b.setText(data.getString("txtKonto3b", "0"));
        txtNewKonto1.setText(data.getString("txtNewKonto1", "0"));
        txtNewKonto2.setText(data.getString("txtNewKonto2", "0"));
        txtNewKonto3a.setText(data.getString("txtNewKonto3a", "0"));
        txtNewKonto3b.setText(data.getString("txtNewKonto3b", "0"));

        txtKonto1Crit.setText(data.getString("txtKonto1Crit", "0"));
        txtKonto1Min.setText(data.getString("txtKonto1Min", "0"));
        txtKonto1Max.setText(data.getString("txtKonto1Max", "0"));
        txtKonto2Crit.setText(data.getString("txtKonto2Crit", "0"));
        txtKonto2Min.setText(data.getString("txtKonto2Min", "0"));
        txtKonto2Max.setText(data.getString("txtKonto2Max", "0"));

        txtKonto3Min30.setText(data.getString("txtKonto3Min10", "0"));
        txtKonto3Min20.setText(data.getString("txtKonto3Min5", "0"));
        txtKonto3Plus5.setText(data.getString("txtKonto3Plus5", "0"));
        txtKonto3Plus10.setText(data.getString("txtKonto3Plus10", "0"));

        txtRebalancing3aPercent.setText(data.getString("txtRebalancing3aPercent", "0"));
        txtRebalancing3bPercent.setText(data.getString("txtRebalancing3bPercent", "0"));
        txtRebalancingThreshold.setText(data.getString("txtRebalancingThreshold", "0"));

        recalculateProfit();
    }

    private void recalculateProfit() {
        lblProfit2.setText(getProfitAsText(txtLastKonto2.getText(), txtKonto2.getText()));
        lblProfit3a.setText(getProfitAsText(txtLastKonto3a.getText(), txtKonto3a.getText()));
        lblProfit3b.setText(getProfitAsText(txtLastKonto3b.getText(), txtKonto3b.getText()));
    }

    private void recalculateAccount3Percentage() {
        lblLastVerteilung.setText(getPercentage(txtLastKonto3a, txtLastKonto3b));
        lblVerteilung.setText(getPercentage(txtKonto3a, txtKonto3b));
        lblNewVerteilung.setText(getPercentage(txtNewKonto3a, txtNewKonto3b));
    }

    private String getPercentage(TextField tf1, TextField tf2) {
        double kt1 = getInt(tf1);
        double kt2 = getInt(tf2);
        double p1 = kt1 + kt2 != 0? kt1 * 100 / (kt1 + kt2): kt1;
        double p2 = kt2 + kt2 != 0? kt2 * 100 / (kt1 + kt2): kt2;
        return Math.round(p1) + "% / " + Math.round(p2) + "%";
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

    private int getInt(TextField textField) {
        return getInt(textField.getText());
    }

    private int getInt(String text) {
        int value = 0;
        if (text != null && !text.isEmpty()) {
            value = Integer.parseInt(text.replace("'", ""));
        }
        return value;
    }

    private void showText(PotsRebalancing potsRebalancing, AccountRebalancing accountRebalancing) {
        if (potsRebalancing.getFrom3To1() > 0) {
            txtAusgabeGross.setText(String.format("Konto 3 -> Konto 1: %s CHF\n", getDecimalFormatter().format(potsRebalancing.getFrom3To1())));
        }
        if (potsRebalancing.getFrom3To2() > 0) {
            txtAusgabeGross.setText(String.format("Konto 3 -> Konto 2: %s CHF\n", getDecimalFormatter().format(potsRebalancing.getFrom3To2())));
        }
        if (potsRebalancing.getFrom2To1() > 0) {
            txtAusgabeGross.setText(String.format("Konto 2 -> Konto 1: %s CHF\n", getDecimalFormatter().format(potsRebalancing.getFrom2To1())));
        }
        if (potsRebalancing.getFrom1To3() > 0) {
            txtAusgabeGross.setText(String.format("Konto 1 -> Konto 3: %s CHF\n", getDecimalFormatter().format(potsRebalancing.getFrom1To3())));
        }

    }

    // Functionalities
    private void doAnalysis() {
        // Do pot rebalancing
        double profit2 = getProfit(txtLastKonto2.getText(), txtKonto2.getText());
        int lastKonto3 = getInt(txtLastKonto3a) + getInt(txtLastKonto3b);
        int konto3 = getInt(txtKonto3a) + getInt(txtKonto3b);
        double profit3 = getProfit(lastKonto3, konto3);

        PotsRebalancer potsRebalancer = new PotsRebalancer(getInt(txtKonto1), getInt(txtKonto2), konto3, profit2, profit3);
        potsRebalancer.setKonto1Params(getInt(txtKonto1Crit), getInt(txtKonto1Min));
        potsRebalancer.setKonto2Params(getInt(txtKonto2Crit), getInt(txtKonto2Min), getInt(txtKonto2Max));
        potsRebalancer.setKonto3Params(getInt(txtKonto3Plus10), getInt(txtKonto3Plus5), getInt(txtKonto3Min20), getInt(txtKonto3Min30));
        PotsRebalancing potsRebalancing = potsRebalancer.rebalancing();

        // Do account3 rebalancing
        double profit3a = getProfit(txtLastKonto3a.getText(), txtKonto3a.getText());
        double profit3b = getProfit(txtLastKonto3b.getText(), txtKonto3b.getText());
        AccountRebalancing accountRebalancing = new AccountRebalancer(getInt(txtKonto3a), getInt(txtKonto3b), profit3a, profit3b).rebalance();

        // Do calculation
        txtNewKonto1.setTextReformat(Integer.toString(getInt(txtKonto1) + potsRebalancing.getFrom2To1() + potsRebalancing.getFrom3To1() - potsRebalancing.getFrom1To3()));
        txtNewKonto2.setTextReformat(Integer.toString(getInt(txtKonto2) + potsRebalancing.getFrom3To2() - potsRebalancing.getFrom2To1()));
        txtNewKonto3a.setTextReformat(Integer.toString(getInt(txtKonto3a) + potsRebalancing.getFrom1To3() - potsRebalancing.getFrom3To1() - potsRebalancing.getFrom3To2()));
        txtNewKonto3b.setTextReformat(Integer.toString(getInt(txtKonto3b) + potsRebalancing.getFrom1To3() - potsRebalancing.getFrom3To1() - potsRebalancing.getFrom3To2()));
        showText(potsRebalancing, accountRebalancing);
    }

    private void doHistory() {
        System.out.println("do Historie");
    }

    private void doAccept() {
        System.out.println("do Accept");
        txtLastKonto1.setText(txtNewKonto1.getText());
        txtLastKonto2.setText(txtNewKonto2.getText());
        txtLastKonto3a.setText(txtNewKonto3a.getText());
        txtLastKonto3b.setText(txtNewKonto3b.getText());

    }
}
