package at.htlgkr.raiffeisenprojektteam.schuldenapp;

import java.io.Serializable;

/**
 * Created by Alexander on 16.11.16.
 */

public class Debt implements Serializable
{
    public static int OWN_DEPT = 0, SBDY_OWES_ME_DEPT = 1;

    private int id;
    private boolean iAmCreditor;
    private String deptorFirstName, deptorLastName, usuage, iBan, status;
    private double value;
    private String date;

    public Debt(int id,boolean iAmCreditor, String deptorFirstName, String deptorLastName, String usuage, String iBan, String status, double value, String date) {
        this.id=id;
        this.iAmCreditor = iAmCreditor;
        this.deptorFirstName = deptorFirstName;
        this.deptorLastName = deptorLastName;
        this.usuage = usuage;
        this.iBan = iBan;
        this.status = status;   //open, not_paid, paid
        this.value = value;
        this.date = date;
    }

    public static int getOwnDept() {
        return OWN_DEPT;
    }

    public static void setOwnDept(int ownDept) {
        OWN_DEPT = ownDept;
    }

    public static int getSbdyOwesMeDept() {
        return SBDY_OWES_ME_DEPT;
    }

    public static void setSbdyOwesMeDept(int sbdyOwesMeDept) {
        SBDY_OWES_ME_DEPT = sbdyOwesMeDept;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isiAmCreditor() {
        return iAmCreditor;
    }

    public void setiAmCreditor(boolean iAmCreditor) {
        this.iAmCreditor = iAmCreditor;
    }

    public String getDeptorFirstName() {
        return deptorFirstName;
    }

    public void setDeptorFirstName(String deptorFirstName) {
        this.deptorFirstName = deptorFirstName;
    }

    public String getDeptorLastName() {
        return deptorLastName;
    }

    public void setDeptorLastName(String deptorLastName) {
        this.deptorLastName = deptorLastName;
    }

    public String getUsuage() {
        return usuage;
    }

    public void setUsuage(String usuage) {
        this.usuage = usuage;
    }

    public String getiBan() {
        return iBan;
    }

    public void setiBan(String iBan) {
        this.iBan = iBan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return deptorFirstName+" "+deptorLastName+" "+value+"€";
    }
}