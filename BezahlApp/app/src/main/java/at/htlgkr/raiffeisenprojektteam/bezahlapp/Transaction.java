package at.htlgkr.raiffeisenprojektteam.bezahlapp;

import java.io.Serializable;

/**
 * Created by michael on 09.02.17.
 */

public class Transaction implements Serializable{

    private String bic;
    private String creditor;
    private String iban;
    private float amount;
    private String reason;
    private String reference;
    private String text;
    private String message;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Transaction(String bic, String creditor, String iban, float amount, String reason, String reference, String text, String message) {
        this.bic = bic;
        this.creditor = creditor;
        this.iban = iban;
        this.amount = amount;
        this.reason = reason;
        this.reference = reference;
        this.text = text;
        this.message = message;
        this.ID = 0;
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

    @Override
    public String toString() {
        return this.creditor+" " + this.amount +"€";
    }
}
