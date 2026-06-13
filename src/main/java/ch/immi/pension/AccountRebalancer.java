package ch.immi.pension;

import ch.immi.pension.data.AccountRebalancing;

public class AccountRebalancer {
    private final int konto3a;
    private final int konto3b;

    private int percentage3a;
    private int percentage3b;

    private int threshold;

    public AccountRebalancer(int konto3a, int konto3b) {
        this.konto3a = konto3a;
        this.konto3b = konto3b;
    }

    public void setParams(int percentage3a, int percentage3b, int threshold) {
        this.percentage3a = percentage3a;
        this.percentage3b = percentage3b;
        this.threshold = threshold;
    }

    public AccountRebalancing rebalance(int amount) {
        int newKonto3a = konto3a;
        int newKonto3b = konto3b;
        if (amount > 0) {
            newKonto3a = (konto3a + konto3b + amount) * percentage3a / 100;
            newKonto3b = (konto3a + konto3b + amount) * percentage3b / 100;
        } else {
            int currentPerc3a = konto3a / (konto3a + konto3b) * 100;
            if (Math.abs(percentage3a - currentPerc3a) > threshold) {
                int total = konto3a + konto3b;
                newKonto3a = total * percentage3a / 100;
                newKonto3b = total * percentage3b / 100;
            }
        }
        return new AccountRebalancing(newKonto3a - konto3a, newKonto3b - konto3b);
    }

}
