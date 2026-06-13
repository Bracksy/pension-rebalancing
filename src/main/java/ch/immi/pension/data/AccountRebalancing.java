package ch.immi.pension.data;

public class AccountRebalancing {
    private final int konto3aChange;
    private final int konto3bChange;

    public AccountRebalancing(int konto3aChange, int konto3bChange) {
        this.konto3aChange = konto3aChange;
        this.konto3bChange = konto3bChange;
    }

    public int getKonto3aChange() {
        return konto3aChange;
    }

    public int getKonto3bChange() {
        return konto3bChange;
    }
}
