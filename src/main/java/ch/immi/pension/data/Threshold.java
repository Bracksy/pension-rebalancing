package ch.immi.pension.data;

public class Threshold {
    private int konto1Min;
    private int konto1Opt;
    private int konto1Max;

    private int konto2Min;
    private int konto2Opt;
    private int konto2Max;

    public static Threshold of(int bezug) {
        Threshold threshold = new Threshold();
        threshold.konto1Min = 2 * bezug;
        threshold.konto1Opt = 3 * bezug;
        threshold.konto1Max = 4 * bezug;
        threshold.konto2Min = 3 * bezug;
        threshold.konto2Opt = 5 * bezug;
        threshold.konto2Max = 7 * bezug;
        return threshold;
    }

    public int getKonto1Max() {
        return konto1Max;
    }

    public int getKonto1Min() {
        return konto1Min;
    }

    public int getKonto1Opt() {
        return konto1Opt;
    }

    public int getKonto2Max() {
        return konto2Max;
    }

    public int getKonto2Min() {
        return konto2Min;
    }

    public int getKonto2Opt() {
        return konto2Opt;
    }
}
