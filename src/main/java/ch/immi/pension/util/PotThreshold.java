package ch.immi.pension.util;

public class PotThreshold {

    private int pot1Min;
    private int pot1Opt;

    private int pot2Min;
    private int pot2Opt;
    private int pot2Max;

    public static PotThreshold of(int bezug) {
        PotThreshold threshold = new PotThreshold();
        float POT1_MIN_FACTOR = 1.5f;
        float POT1_OPT_FACTOR = 3f;

        float POT2_MIN_FACTOR = 2.5f;
        float POT2_OPT_FACTOR = 5f;
        float POT2_MAX_FACTOR = 7f;

        threshold.pot1Min = (int)(POT1_MIN_FACTOR * bezug);
        threshold.pot1Opt = (int)(POT1_OPT_FACTOR * bezug);

        threshold.pot2Min = (int)(POT2_MIN_FACTOR * bezug);
        threshold.pot2Opt = (int)(POT2_OPT_FACTOR * bezug);
        threshold.pot2Max = (int)(POT2_MAX_FACTOR * bezug);
        return threshold;
    }

    public int getPot1Min() {
        return pot1Min;
    }

    public int getPot1Opt() {
        return pot1Opt;
    }

    public int getPot2Max() {
        return pot2Max;
    }

    public int getPot2Min() {
        return pot2Min;
    }

    public int getPot2Opt() {
        return pot2Opt;
    }
}
