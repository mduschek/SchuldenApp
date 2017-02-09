package at.htlgkr.raiffeisenprojektteam.bezahlapp;

/**
 * Created by michael on 09.02.17.
 */

public class Transaction {

    private String iban;
    private String debtorFirstname;
    private String debtorLastname;
    private String usage;
    private String date;
    private double value;

    public Transaction(String iban, String debtorFirstname, String debtorLastname, String usage, String date, double value) {
        this.iban = iban;
        this.debtorFirstname = debtorFirstname;
        this.debtorLastname = debtorLastname;
        this.usage = usage;
        this.date = date;
        this.value = value;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getDebtorFirstname() {
        return debtorFirstname;
    }

    public void setDebtorFirstname(String debtorFirstname) {
        this.debtorFirstname = debtorFirstname;
    }

    public String getDebtorLastname() {
        return debtorLastname;
    }

    public void setDebtorLastname(String debtorLastname) {
        this.debtorLastname = debtorLastname;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
