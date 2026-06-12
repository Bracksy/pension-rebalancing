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
        int minAmount = konto1Min - konto1;
        if (konto1 < konto1Cri) {
            if (profit2 > 0 || konto2 - minAmount > konto2Min) {
                potsRebalancing.setFrom2To1(minAmount);
            } else {
                potsRebalancing.setFrom3To1(minAmount);
            }
        } else if (konto1 < konto1Min) {
            if (profit2 > 0) {
                potsRebalancing.setFrom2To1(minAmount);
            } else if (profit3 > 0) {
                int amount = (int) (konto3 * profit3 / 100);
                potsRebalancing.setFrom3To1(Math.max(amount, minAmount));
            }
        }
    }

    private void step2_checkKonto2(PotsRebalancing potsRebalancing) {
        int newKonto2 = konto2 - potsRebalancing.getFrom2To1();
        int minAmount = konto2Min - konto2;
        if (newKonto2 < konto2Cri) {
            potsRebalancing.setFrom3To2(minAmount);
        } else if (konto2 < konto2Min) {
            if (profit3 > 0) {
                int amount = (int) (konto3 * profit3 / 100);
                potsRebalancing.setFrom3To2(Math.max(amount, minAmount));
            }
        } else if (newKonto2 > konto2Max) {
            int amount = newKonto2 - konto2Max;
            potsRebalancing.setFrom2To1(potsRebalancing.getFrom2To1() + amount);
        }
    }

    private void step3_checkKonto3(PotsRebalancing potsRebalancing) {
        if (profit3 > 10) {
            if (konto3Plus10 > 0) {
                int amount = (int) (konto3 * (double) konto3Plus10 / 100);
                potsRebalancing.setFrom3To2(Math.max(amount,  potsRebalancing.getFrom3To2()));
            }
        } else if (profit3 > 5) {
            if (konto3Plus5 > 0) {
                int amount = (int) (konto3 * (double) konto3Plus5 / 100);
                potsRebalancing.setFrom3To2(Math.max(amount,  potsRebalancing.getFrom3To2()));
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
        int from3to2 = potsRebalancing.getFrom3To2();
        int from2To1 = potsRebalancing.getFrom2To1();
        int from3To1 = potsRebalancing.getFrom3To1();

        if (from3to2 > 0 && from3to2 < from2To1) {
            from3To1 = from2To1 - from3to2;
            from2To1 = from3to2;
            from3to2 = 0;
        } else if (from2To1 > 0 && from3to2 > from2To1) {
            from3To1 = from3to2 - from2To1;
            from3to2 = from2To1;
            from2To1 = 0;
        } else if (from3to2 > 0 && from3to2 == from2To1){
            from3To1 = from3to2;
            from3to2 = 0;
            from2To1 = 0;
        }
        potsRebalancing.setFrom3To1(from3To1);
        potsRebalancing.setFrom2To1(from2To1);
        potsRebalancing.setFrom3To2(from3to2);
    }
}
