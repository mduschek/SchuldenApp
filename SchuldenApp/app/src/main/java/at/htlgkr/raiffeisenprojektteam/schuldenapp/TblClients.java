package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by yg on 15/11/16.
 */

public class TblClients {
    public static final String TABLE_NAME="Clients";

    //          <PrimaryKeys>
    public static final String CLIENT_IBAN="client_iban";
    //          </PrimaryKeys>

    public static final String ID="_id";
    public static final String CLIENT_FIRSTNAME="client_firstname";
    public static final String CLIENT_LASTNAME="client_lastname";
    public static final String CLIENT_BANK_BALANCE="client_bank_balance";

    public static final String SQL_CREATE_TABEL="CREATE TABLE "+TABLE_NAME+" ( "+
            ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CLIENT_IBAN+" TEXT NOT NULL,"+
            CLIENT_FIRSTNAME+" TEXT,"+
            CLIENT_LASTNAME+" TEXT,"+
            CLIENT_BANK_BALANCE+" DOUBLE);";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";
}
