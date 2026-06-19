package ch.immi.pension;

import ch.immi.pension.data.PotsRebalancing;
import ch.immi.pension.data.Threshold;

public class PotsRebalancer {
    private final int konto1;
    private final int konto2;
    private final int konto3;

    private final double profit3;

    private final Threshold threshold;

    private int konto3Plus10;
    private int konto3Plus5;
    private int konto3Min20;
    private int konto3Min30;

    public PotsRebalancer(int konto1, int konto2, int konto3, double profit2, double profit3, int bezug) {
        this.konto1 = konto1;
        this.konto2 = konto2;
        this.konto3 = konto3;
        this.profit3 = profit3;
        this.threshold = Threshold.of(bezug);
    }

    public PotsRebalancing rebalancing() {
        PotsRebalancing potsRebalancing = new PotsRebalancing();
        step1_checkKonto1(potsRebalancing);
        step2_checkKonto2(potsRebalancing);
        step3_checkKonto3(potsRebalancing);
        summarizeFrom3To2To1(potsRebalancing);
        return potsRebalancing;
    }

    private void step1_checkKonto1(PotsRebalancing potsRebalancing) {
        int kto1ToOptAmount = konto1 < threshold.getKonto1Opt() ? threshold.getKonto1Opt() - konto1: 0;
        int kto1ToOptAmount2 = konto1 < (threshold.getKonto1Opt() - 10000)? threshold.getKonto1Opt() - konto1 - 10000: 0;
        int kto1CriAmount = konto1 < threshold.getKonto1Min() ? threshold.getKonto1Min() - konto1: 0;
        int amountFrom3 = profit3 > 0? (int)(konto3 * profit3 / 100): 0;
        if (konto1 < threshold.getKonto1Min()) {
            if (konto2 - kto1ToOptAmount > threshold.getKonto2Min()) {
                potsRebalancing.setFrom2To1Min(kto1ToOptAmount, konto2);
            } else if (konto2 - kto1ToOptAmount2 > threshold.getKonto2Min()) {
                potsRebalancing.setFrom2To1Min(kto1ToOptAmount2, konto2);
            } else if (konto2 - kto1CriAmount > threshold.getKonto2Min() || konto3 == 0) {
                potsRebalancing.setFrom2To1Min(kto1CriAmount, konto2);
            } else {
                int kto2ToMinAmount = konto2 > threshold.getKonto2Min() ? konto2 - threshold.getKonto2Min() : 0;
                potsRebalancing.setFrom2To1Min(kto2ToMinAmount, konto3);
                potsRebalancing.setFrom3To1Min(Math.max(amountFrom3, kto1CriAmount - kto2ToMinAmount), konto3);
            }
        } else if (konto1 < threshold.getKonto1Opt()) {
            if (konto2 - kto1ToOptAmount > threshold.getKonto2Min()) {
                potsRebalancing.setFrom2To1Min(kto1ToOptAmount, konto2);
            } else if (konto2 - kto1CriAmount > threshold.getKonto2Min()) {
                potsRebalancing.setFrom2To1Min(kto1CriAmount, konto2);
            } else {
                if (profit3 > 10) {
                    potsRebalancing.setFrom3To1Min(amountFrom3, kto1ToOptAmount, konto3);
                } else if (profit3 > 5) {
                    potsRebalancing.setFrom3To1Min(amountFrom3, kto1ToOptAmount2, konto3);
                } else if (profit3 > 0) {
                    potsRebalancing.setFrom3To1Min(amountFrom3, kto1CriAmount, konto3);
                }
            }
        } else if (konto1 > threshold.getKonto1Max()) {
            int amount = threshold.getKonto1Max() - konto1;
            potsRebalancing.setFrom1To3(amount);
        }
    }

    private void step2_checkKonto2(PotsRebalancing potsRebalancing) {
        int newKonto2 = konto2 - potsRebalancing.getFrom2To1();
        int kto2MinAmount = newKonto2 < threshold.getKonto2Opt() ? threshold.getKonto2Opt() - newKonto2: 0;
        int kto2CriAmount = newKonto2 < threshold.getKonto2Min() ? threshold.getKonto2Min() - newKonto2: 0;
        int amountFrom3 = profit3 > 0? (int)(konto3 * profit3 / 100): 0;
        if (newKonto2 < threshold.getKonto2Min()) {
            potsRebalancing.setFrom3To2Min(kto2CriAmount, konto3);
        } else if (newKonto2 < threshold.getKonto2Opt()) {
            if (amountFrom3 > kto2MinAmount) {
                potsRebalancing.setFrom3To2Min(kto2MinAmount, konto3);
            } else {
                potsRebalancing.setFrom3To2Min(Math.max(amountFrom3, kto2CriAmount), konto3);
            }
        } else if (newKonto2 > threshold.getKonto2Max()) {
            int amount = threshold.getKonto2Max() - newKonto2;
            potsRebalancing.setFrom2To1(amount);
        }
    }

    private void step3_checkKonto3(PotsRebalancing potsRebalancing) {
        if (profit3 > 10) {
            if (konto3Plus10 > 0) {
                int amount = (int) (konto3 * (double) konto3Plus10 / 100);
                potsRebalancing.setFrom3To2Min(amount, potsRebalancing.getFrom3To2());
            }
        } else if (profit3 > 5) {
            if (konto3Plus5 > 0) {
                int amount = (int) (konto3 * (double) konto3Plus5 / 100);
                potsRebalancing.setFrom3To2Min(amount, potsRebalancing.getFrom3To2());
            }
        } else if (profit3 < -30) {
            if (konto3Min30 > 0) {
                int amount = (int)(konto1 * (double) konto3Min30 / 100);
                if (konto1 - amount > threshold.getKonto1Opt()) {
                    potsRebalancing.setFrom1To3(amount);
                }
            }
        } else if (profit3 < -20) {
            if (konto3Min20 > 0) {
                int amount = (int)(konto1 * (double) konto3Min20 / 100);
                if (konto1 - amount > threshold.getKonto1Opt()) {
                    potsRebalancing.setFrom1To3(amount);
                }
            }
        }
    }

    private void summarizeFrom3To2To1(PotsRebalancing potsRebalancing) {
        int from3To2 = potsRebalancing.getFrom3To2();
        int from2To1 = potsRebalancing.getFrom2To1();
        int from3To1 = potsRebalancing.getFrom3To1();

        if (from3To2 > 0 && from3To2 < from2To1) {
            from3To1 = from3To2;
            from2To1 = from2To1 - from3To2;
            from3To2 = 0;
        } else if (from2To1 > 0 && from3To2 > from2To1) {
            from3To2 = from3To2 - from2To1;
            from3To1 = from2To1;
            from2To1 = 0;
        } else if (from3To2 > 0 && from3To2 == from2To1){
            from3To1 = from3To2;
            from3To2 = 0;
            from2To1 = 0;
        }
        potsRebalancing.setFrom3To1(from3To1);
        potsRebalancing.setFrom2To1(from2To1);
        potsRebalancing.setFrom3To2(from3To2);
    }
}
