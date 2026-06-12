package ch.immi.pension;

import ch.immi.pension.data.AccountRebalancing;

public class AccountRebalancer {
    private final int konto3a;
    private final int konto3b;

    private final double profit3a;
    private final double profit3b;

    private int percentage3a;
    private int percentage3b;

    private int threshold;

    public AccountRebalancer(int konto3a, int konto3b, double profit3a, double profit3b) {
        this.konto3a = konto3a;
        this.konto3b = konto3b;
        this.profit3a = profit3a;
        this.profit3b = profit3b;
    }

    public void setParams(int percentage3a, int percentage3b, int threshold) {
        this.percentage3a = percentage3a;
        this.percentage3b = percentage3b;
        this.threshold = threshold;
    }

    public AccountRebalancing rebalance() {
        return new AccountRebalancing(konto3a, konto3b);
    }

}
