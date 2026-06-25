package ch.immi.pension.util;

public class Pot3System {
    private final int numOfShares3a;
    private final int numOfShares3b;
    private final double price3a;
    private final double price3b;

    private double percentage3a;
    private double percentage3b;

    private double threshold;

    public Pot3System(int numOfShares3a, double price3a, int numOfShares3b, double price3b) {
        this.numOfShares3a = numOfShares3a;
        this.numOfShares3b = numOfShares3b;
        this.price3a = price3a;
        this.price3b = price3b;
    }

    public void setParams(double percentage3a, double percentage3b, double threshold) {
        this.percentage3a = percentage3a;
        this.percentage3b = percentage3b;
        this.threshold = threshold;
    }

    public Pots3State rebalance() {
        int newNumOfShares3a = numOfShares3a;
        int newNumOfShares3b = numOfShares3b;

        int pot3a = (int)(numOfShares3a * price3a);
        int pot3b = (int)(numOfShares3b * price3b);
        double currPercentage3a = ((double)pot3a) / ((double)(pot3a + pot3b));
        if (Math.abs(percentage3a - currPercentage3a) > threshold) {
            int total = pot3a + pot3b;
            int newPot3a = (int)(total * percentage3a);
            int newPot3b = (int)(total * percentage3b);
            newNumOfShares3a = (int)(newPot3a / price3a);
            newNumOfShares3b = (int)(newPot3b / price3b);
        }
        return new Pots3State(newNumOfShares3a, newNumOfShares3b);
    }

}
