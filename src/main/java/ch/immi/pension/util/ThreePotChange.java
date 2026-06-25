package ch.immi.pension.util;

public class ThreePotChange {
    int changeShares3a = 0;
    int changeShares3b = 0;
    boolean changedHighestPrice3a = false;
    boolean changedHighestPrice3b = false;
    int from1To3 = 0;
    int from2To1 = 0;
    int from2To3 = 0;
    int from3To1 = 0;
    int from3To2 = 0;

    public ThreePotChange() {
    }

    public boolean isChangedHighestPrice3a() {
        return changedHighestPrice3a;
    }

    public void setChangedHighestPrice3a(boolean changedHighestPrice3a) {
        this.changedHighestPrice3a = changedHighestPrice3a;
    }

    public boolean isChangedHighestPrice3b() {
        return changedHighestPrice3b;
    }

    public void setChangedHighestPrice3b(boolean changedHighestPrice3b) {
        this.changedHighestPrice3b = changedHighestPrice3b;
    }

    public int getChangeShares3a() {
        return changeShares3a;
    }

    public void addChangeShares3a(int changeShares3a) {
        this.changeShares3a += changeShares3a;
    }

    public int getChangeShares3b() {
        return changeShares3b;
    }

    public void addChangeShares3b(int changeShares3b) {
        this.changeShares3b += changeShares3b;
    }

    public int getFrom2To1() {
        return from2To1;
    }

    public void addFrom2To1(int from2To1) {
        this.from2To1 += from2To1;
    }

    public int getFrom3To1() {
        return from3To1;
    }

    public void addFrom3To1(int from3To1) {
        this.from3To1 += from3To1;
    }

    public int getFrom3To2() {
        return from3To2;
    }

    public void addFrom3To2(int from3To2) {
        this.from3To2 += from3To2;
    }

    public int getFrom1To3() {
        return from1To3;
    }

    public void addFrom1To3(int from1To3) {
        this.from1To3 += from1To3;
    }

    public int getFrom2To3() {
        return from2To3;
    }

    public void addFrom2To3(int from2To3) {
        this.from2To3 += from2To3;
    }
}
