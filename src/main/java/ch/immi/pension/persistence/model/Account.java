package ch.immi.pension.persistence.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer account1;
    Integer account2;
    Integer number3a;
    Double price3a;
    Double highest3a;
    Integer number3b;
    Double price3b;
    Double highest3b;
    LocalDate date;

    public Integer getAccount1() {
        return account1;
    }

    public void setAccount1(Integer account1) {
        this.account1 = account1;
    }

    public Integer getAccount2() {
        return account2;
    }

    public void setAccount2(Integer account2) {
        this.account2 = account2;
    }

    public Double getHighest3a() {
        return highest3a;
    }

    public void setHighest3a(Double highest3a) {
        this.highest3a = highest3a;
    }

    public Double getHighest3b() {
        return highest3b;
    }

    public void setHighest3b(Double highest3b) {
        this.highest3b = highest3b;
    }

    public Integer getNumber3a() {
        return number3a;
    }

    public void setNumber3a(Integer number3a) {
        this.number3a = number3a;
    }

    public Integer getNumber3b() {
        return number3b;
    }

    public void setNumber3b(Integer number3b) {
        this.number3b = number3b;
    }

    public Double getPrice3a() {
        return price3a;
    }

    public void setPrice3a(Double price3a) {
        this.price3a = price3a;
    }

    public Double getPrice3b() {
        return price3b;
    }

    public void setPrice3b(Double price3b) {
        this.price3b = price3b;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
