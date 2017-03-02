package at.htlgkr.raiffeisenprojektteam.bezahlapp;

/**
 * Created by michael on 09.02.17.
 */

public class Transaction {

    private String bic;
    private String creditor;
    private String iban;
    private float amount;
    private String reason;
    private String reference;
    private String text;
    private String message;

    public Transaction(String bic, String creditor, String iban, float amount, String reason, String reference, String text, String message) {
        this.bic = bic;
        this.creditor = creditor;
        this.iban = iban;
        this.amount = amount;
        this.reason = reason;
        this.reference = reference;
        this.text = text;
        this.message = message;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*
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
    */
}
