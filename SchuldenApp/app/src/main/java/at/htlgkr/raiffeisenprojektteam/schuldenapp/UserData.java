package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by Perndorfer on 28.01.2017.
 */

public class UserData {
    public static String firstname,lastname, iban, bic, email;


    public static String getString() {
        return firstname+lastname +iban+bic+email;
    }
}
