package ch.immi.pension.persistence.model;

import jakarta.persistence.*;

@Entity
@Table(name = "setting")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer annualWithdrawal;
    String ticker3a;
    String ticker3b;
    Integer percentage3a;
    Integer percentage3b;
    Integer thresholdPercentage;

    public Integer getAnnualWithdrawal() {
        return annualWithdrawal;
    }

    public void setAnnualWithdrawal(Integer annualWithdrawal) {
        this.annualWithdrawal = annualWithdrawal;
    }

    public Integer getPercentage3a() {
        return percentage3a;
    }

    public void setPercentage3a(Integer percentage3a) {
        this.percentage3a = percentage3a;
    }

    public Integer getPercentage3b() {
        return percentage3b;
    }

    public void setPercentage3b(Integer percentage3b) {
        this.percentage3b = percentage3b;
    }

    public Integer getThresholdPercentage() {
        return thresholdPercentage;
    }

    public void setThresholdPercentage(Integer thresholdPercentage) {
        this.thresholdPercentage = thresholdPercentage;
    }

    public String getTicker3a() {
        return ticker3a;
    }

    public void setTicker3a(String ticker3a) {
        this.ticker3a = ticker3a;
    }

    public String getTicker3b() {
        return ticker3b;
    }

    public void setTicker3b(String ticker3b) {
        this.ticker3b = ticker3b;
    }
}
