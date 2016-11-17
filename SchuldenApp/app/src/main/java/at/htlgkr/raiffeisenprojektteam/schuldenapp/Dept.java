package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by Alexander on 16.11.16.
 */

public class Dept
{
    public static int OWN_DEPT = 0, SBDY_OWES_ME_DEPT = 1;

    private int deptType;
    private String deptorFirstName, deptorLastName, usuage, iBan;
    private double value;

    public Dept(int deptType, String deptorFirstName, String deptorLastName, String usuage, String iBan, double value) {
        this.deptType = deptType;
        this.deptorFirstName = deptorFirstName;
        this.deptorLastName = deptorLastName;
        this.usuage = usuage;
        this.iBan = iBan;
        this.value = value;
    }
}
