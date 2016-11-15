package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by yg on 15/11/16.
 */

public class TblClients {
    public static final String TABLE_NAME="Clients";

    //          <PrimaryKeys>
    public static final String CLIENT_ID="client_id AS _id";
    public static final String CLIENT_IBAN="client_iban";
    //          </PrimaryKeys>

    public static final String CLIENT_FIRSTNAME="client_firstname";
    public static final String CLIENT_LASTNAME="client_lastname";
    public static final String CLIENT_BANK_BALANCE="client_bank_balance";

    public static final String SQL_CREATE_TABEL="CREATE TABLE "+TABLE_NAME+"("+
            CLIENT_ID+" PRIMARY KEY NUMBER,"+
            CLIENT_IBAN+" PRIMARY KEY NUMBER,"+
            CLIENT_FIRSTNAME+" VARCHAR2,"+
            CLIENT_LASTNAME+" VARCHAR2,"+
            CLIENT_BANK_BALANCE+" DOUBLE);";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";

}
