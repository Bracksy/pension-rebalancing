package ch.immi.pension;

import ch.immi.pension.data.PotsRebalancing;

public class PotsRebalancer {
    private final int konto1;
    private final int konto2;
    private final int konto3;

    private final double profit2;
    private final double profit3;

    private int konto1Cri;
    private int konto1Min;

    private int konto2Cri;
    private int konto2Min;
    private int konto2Max;

    private int konto3Plus10;
    private int konto3Plus5;
    private int konto3Min20;
    private int konto3Min30;

    public PotsRebalancer(int konto1, int konto2, int konto3, double profit2, double profit3) {
        this.konto1 = konto1;
        this.konto2 = konto2;
        this.konto3 = konto3;
        this.profit2 = profit2;
        this.profit3 = profit3;
    }

    public void setKonto1Params(int konto1Cri, int konto1Min) {
        this.konto1Cri = konto1Cri;
        this.konto1Min = konto1Min;
    }

    public void setKonto2Params(int konto2Cri, int konto2Min, int konto2Max) {
        this.konto2Cri = konto2Cri;
        this.konto2Min = konto2Min;
        this.konto2Max = konto2Max;
    }

    public void setKonto3Params(int konto3Plus10, int konto3Plus5, int konto3Min20, int konto3Min30) {
        this.konto3Plus10 = konto3Plus10;
        this.konto3Plus5 = konto3Plus5;
        this.konto3Min20 = konto3Min20;
        this.konto3Min30 = konto3Min30;
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
        int kto1MinAmount = konto1 < konto1Min? konto1Min - konto1: 0;
        int kto1MinAmount2 = konto1 < (konto1Min - 10000)? konto1Min - konto1 - 10000: 0;
        int kto1CriAmount = konto1 < konto1Cri? konto1Cri - konto1: 0;
        int amountFrom3 = profit3 > 0? (int)(konto3 * profit3 / 100): 0;
        if (konto1 < konto1Cri) {
            if (konto2 - kto1MinAmount > konto2Cri) {
                potsRebalancing.setFrom2To1Min(kto1MinAmount, konto2);
            } else if (konto2 - kto1MinAmount2 > konto2Cri) {
                potsRebalancing.setFrom2To1Min(kto1MinAmount2, konto2);
            } else if (konto2 - kto1CriAmount > konto2Cri || konto3 == 0) {
                potsRebalancing.setFrom2To1Min(kto1CriAmount, konto2);
            } else {
                if (profit3 < 2) {
                    potsRebalancing.setFrom3To1Min(kto1CriAmount, konto3);
                } else if (profit3 < 3) {
                    potsRebalancing.setFrom3To1Min(kto1MinAmount2, konto3);
                } else {
                    potsRebalancing.setFrom3To1Min(kto1MinAmount, konto3);
                }
            }
        } else if (konto1 < konto1Min) {
            if (konto2 - kto1MinAmount > konto2Cri) {
                potsRebalancing.setFrom2To1Min(kto1MinAmount, konto2);
            } else if (konto2 - kto1CriAmount > konto2Cri) {
                potsRebalancing.setFrom2To1Min(kto1CriAmount, konto2);
            } else {
                if (profit3 > 10) {
                    potsRebalancing.setFrom3To1Min(amountFrom3, kto1MinAmount, konto3);
                } else if (profit3 > 5) {
                    potsRebalancing.setFrom3To1Min(amountFrom3, kto1MinAmount2, konto3);
                } else if (profit3 > 0) {
                    potsRebalancing.setFrom3To1Min(amountFrom3, kto1CriAmount, konto3);
                }
            }
        }
    }

    private void step2_checkKonto2(PotsRebalancing potsRebalancing) {
        int newKonto2 = konto2 - potsRebalancing.getFrom2To1();
        int kto2MinAmount = newKonto2 < konto2Min? konto2Min - newKonto2: 0;
        int kto2CriAmount = newKonto2 < konto2Cri? konto2Cri - newKonto2: 0;
        int amountFrom3 = profit3 > 0? (int)(konto3 * profit3 / 100): 0;
        if (newKonto2 < konto2Cri) {
            potsRebalancing.setFrom3To2Min(kto2CriAmount, konto3);
        } else if (newKonto2 < konto2Min) {
            if (profit3 > 5) {
                potsRebalancing.setFrom3To2Min(amountFrom3, kto2MinAmount, konto3);
            } else if (profit3 > 0) {
                potsRebalancing.setFrom3To2Min(amountFrom3, kto2CriAmount, konto3);
            }
        } else if (newKonto2 > konto2Max) {
            int amount = newKonto2 - konto2Max + potsRebalancing.getFrom2To1();
            potsRebalancing.setFrom2To1Min(amount, konto2);
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
                if (konto1 - amount > konto1Cri) {
                    potsRebalancing.setFrom1To3(amount);
                }
            }
        } else if (profit3 < -20) {
            if (konto3Min20 > 0) {
                int amount = (int)(konto1 * (double) konto3Min20 / 100);
                if (konto1 - amount > konto1Cri) {
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
            from3To1 = from2To1 - from3To2;
            from2To1 = from3To2;
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
