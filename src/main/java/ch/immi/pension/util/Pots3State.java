package ch.immi.pension.util;

public class Pots3State {
    private final int numberOfShares3a;
    private final int numberOfShares3b;

    public Pots3State(int numberOfShares3a, int numberOfShares3b) {
        this.numberOfShares3a = numberOfShares3a;
        this.numberOfShares3b = numberOfShares3b;
    }

    public int getNumberOfShares3a() {
        return numberOfShares3a;
    }

    public int getNumberOfShares3b() {
        return numberOfShares3b;
    }

}
