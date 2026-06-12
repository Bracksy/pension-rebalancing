package ch.immi.pension.data;

public class Rebalancing {
    private int konto1;
    private int konto2;
    private int konto3;
    private int from1To3 = 0;
    private int from2To1 = 0;
    private int from3To1 = 0;
    private int from3To2 = 0;

    public Rebalancing(int konto1, int konto2, int konto3) {
        this.konto1 = konto1;
        this.konto2 = konto2;
        this.konto3 = konto3;
    }

    public int getFrom1To3() {
        return from1To3;
    }

    public void setFrom1To3(int from1To3) {
        this.from1To3 = from1To3;
    }

    public int getFrom2To1() {
        return from2To1;
    }

    public void setFrom2To1(int from2To1) {
        this.from2To1 = from2To1;
    }

    public int getFrom3To1() {
        return from3To1;
    }

    public void setFrom3To1(int from3To1) {
        this.from3To1 = from3To1;
    }

    public int getFrom3To2() {
        return from3To2;
    }

    public void setFrom3To2(int from3To2) {
        this.from3To2 = from3To2;
    }

    public int getKonto1() {
        return konto1;
    }

    public int getKonto2() {
        return konto2;
    }

    public int getKonto3() {
        return konto3;
    }
}
