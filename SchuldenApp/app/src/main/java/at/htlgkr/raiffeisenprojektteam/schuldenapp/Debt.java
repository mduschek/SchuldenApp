package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import java.io.Serializable;

/**
 * Created by Alexander on 16.11.16.
 */

public class Debt implements Serializable
{
    public static int OWN_DEPT = 0, SBDY_OWES_ME_DEPT = 1;

    private boolean iAmCreditor;
    private String deptorFirstName, deptorLastName, usuage, iBan, status;
    private double value;

    public Debt(boolean iAmCreditor, String deptorFirstName, String deptorLastName, String usuage, String iBan, String status, double value) {
        this.iAmCreditor = iAmCreditor;
        this.deptorFirstName = deptorFirstName;
        this.deptorLastName = deptorLastName;
        this.usuage = usuage;
        this.iBan = iBan;
        this.status = status;
        this.value = value;
    }

    @Override
    public String toString() {
        return deptorFirstName+" "+deptorLastName+" "+value+"€";
    }
}
