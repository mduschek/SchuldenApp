package at.htlgkr.raiffeisenprojektteam.bezahlapp;

/**
 * Created by michael on 09.02.17.
 */

public class Transaction {

    private String iban;
    private String partnerFirstname;
    private String partnerLastname;
    private String usage;
    private String date;
    private double value;

    public Transaction(String iban, String partnerFirstname, String partnerLastname, String usage, String date, double value) {
        this.iban = iban;
        this.partnerFirstname = partnerFirstname;
        this.partnerLastname = partnerLastname;
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

    public String getPartnerFirstname() {
        return partnerFirstname;
    }

    public void setPartnerFirstname(String partnerFirstname) {
        this.partnerFirstname = partnerFirstname;
    }

    public String getPartnerLastname() {
        return partnerLastname;
    }

    public void setPartnerLastname(String partnerLastname) {
        this.partnerLastname = partnerLastname;
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
