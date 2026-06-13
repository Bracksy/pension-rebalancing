package ch.immi.pension.data;

public class PotsRebalancing {
    private int from1To3 = 0;
    private int from2To1 = 0;
    private int from3To1 = 0;
    private int from3To2 = 0;

    public int getFrom1To3() {
        return from1To3;
    }

    public void setFrom1To3(int from1To3) {
        this.from1To3 = from1To3;
    }

    public void setFrom1To3Min(int from1To3_1, int from1To3_2) {
        this.from1To3 = Math.min(from1To3_1, from1To3_2);
    }

    public void setFrom1To3Min(int from1To3_1, int from1To3_2, int from1To3_3) {
        this.from1To3 = Math.min(Math.min(from1To3_1, from1To3_2), from1To3_3);
    }

    public int getFrom2To1() {
        return from2To1;
    }

    public void setFrom2To1(int from2To1) {
        this.from2To1 = from2To1;
    }

    public void setFrom2To1Min(int from2To1_1, int from2To1_2) {
        this.from2To1 = Math.min(from2To1_1, from2To1_2);
    }

    public int getFrom3To1() {
        return from3To1;
    }

    public void setFrom3To1(int from3To1) {
        this.from3To1 = from3To1;
    }

    public void setFrom3To1Min(int from3To1_1, int from3To1_2) {
        this.from3To1 = Math.min(from3To1_1, from3To1_2);
    }

    public void setFrom3To1Min(int from3To1_1, int from3To1_2, int from3To1_3) {
        this.from3To1 = Math.min(Math.min(from3To1_1, from3To1_2), from3To1_3);
    }

    public int getFrom3To2() {
        return from3To2;
    }

    public void setFrom3To2(int from3To2) {
        this.from3To2 = from3To2;
    }

    public void setFrom3To2Min(int from3To2_1, int from3To2_2) {
        this.from3To2 = Math.min(from3To2_1, from3To2_2);
    }

    public void setFrom3To2Min(int from3To2_1, int from3To2_2, int from3To2_3) {
        this.from3To2 = Math.min(Math.min(from3To2_1, from3To2_2), from3To2_3);
    }
}
