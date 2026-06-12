package ch.immi.pension.data;

public class AccountRebalancing {
    private final int konto3a;
    private final int konto3b;

    public AccountRebalancing(int konto3a, int konto3b) {
        this.konto3a = konto3a;
        this.konto3b = konto3b;
    }

    public int getKonto3a() {
        return konto3a;
    }

    public int getKonto3b() {
        return konto3b;
    }
}
