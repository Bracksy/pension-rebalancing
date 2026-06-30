package ch.immi.pension.util;

public class ThreePotState {
    private int pot1;
    private int pot2;
    private int numOfShares3a;
    private int numOfShares3b;
    private final double price3a;
    private final double price3b;

    private double highestPrice3a;
    private double highestPrice3b;

    private final ThreePotChange threePotChange;

    public ThreePotState(int pot1, int pot2, int numOfShares3a, double price3a, int numOfShares3b, double price3b,
                         double highestPrice3a, double highestPrice3b) {
        this.pot1 = pot1;
        this.pot2 = pot2;
        this.numOfShares3a = numOfShares3a;
        this.numOfShares3b = numOfShares3b;
        this.price3a = price3a;
        this.price3b = price3b;
        this.highestPrice3a = highestPrice3a;
        this.highestPrice3b = highestPrice3b;
        this.threePotChange = new ThreePotChange();
    }

    public void moveFrom2To1(int amount) {
        pot2 -= amount;
        pot1 += amount;
        threePotChange.addFrom2To1(amount);
    }

    public void moveFrom3To2(int shares3a, int shares3b) {
        int amount = (int)(shares3a * price3a + shares3b * price3b);
        numOfShares3a -= shares3a;
        numOfShares3b -= shares3b;
        pot2 += amount;
        threePotChange.addChangeShares3a(-shares3a);
        threePotChange.addChangeShares3b(-shares3b);
        threePotChange.addFrom3To2(amount);
    }

    public void moveFrom3To1(int shares3a, int shares3b) {
        int amount = (int)(shares3a * price3a + shares3b * price3b);
        numOfShares3a -= shares3a;
        numOfShares3b -= shares3b;
        pot1 += amount;
        threePotChange.addChangeShares3a(-shares3a);
        threePotChange.addChangeShares3b(-shares3b);
        threePotChange.addFrom3To1(amount);
    }

    public void moveFrom2To3(int shares3a, int shares3b) {
        int amount = (int)(shares3a * price3a + shares3b * price3b);
        numOfShares3a += shares3a;
        numOfShares3b += shares3b;
        pot2 -= amount;
        threePotChange.addChangeShares3a(shares3a);
        threePotChange.addChangeShares3b(shares3b);
        threePotChange.addFrom2To3(amount);
    }

    public void setHighestPrice3a(double newHighestPrice3a) {
        highestPrice3a = newHighestPrice3a;
        threePotChange.setChangedHighestPrice3a(true);
    }

    public void setHighestPrice3b(double newHighestPrice3b) {
        highestPrice3b = newHighestPrice3b;
        threePotChange.setChangedHighestPrice3b(true);
    }

    public double getHighestPrice3a() {
        return highestPrice3a;
    }

    public double getHighestPrice3b() {
        return highestPrice3b;
    }

    public int getNumOfShares3a() {
        return numOfShares3a;
    }

    public int getNumOfShares3b() {
        return numOfShares3b;
    }

    public int getPot1() {
        return pot1;
    }

    public int getPot2() {
        return pot2;
    }

    public double getPrice3a() {
        return price3a;
    }

    public double getPrice3b() {
        return price3b;
    }

    public ThreePotChange getChange() {
        return threePotChange;
    }
}

