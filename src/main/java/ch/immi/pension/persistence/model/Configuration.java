package ch.immi.pension.persistence.model;

import jakarta.persistence.*;

@Entity
@Table(name = "configuration")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "setting")
    Setting setting;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "lastAccount")
    Account lastAccount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "currentAccount")
    Account currentAccount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "newAccount")
    Account newAccount;

    String balancingText;

    public String getBalancingText() {
        return balancingText;
    }

    public void setBalancingText(String balancingText) {
        this.balancingText = balancingText;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public int getId() {
        return id;
    }

    public Account getLastAccount() {
        return lastAccount;
    }

    public void setLastAccount(Account lastAccount) {
        this.lastAccount = lastAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getNewAccount() {
        return newAccount;
    }

    public void setNewAccount(Account newAccount) {
        this.newAccount = newAccount;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
}
