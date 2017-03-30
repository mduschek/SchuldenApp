package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by dusch on 30.03.2017.
 */

public class TblDebts {
    public static final String TABLE_NAME="Debts";
    public static final String ID = "_id";
    public static final String I_AM_CREDITOR = "iAmCreditor;";
    public static final String FIRSTNAME="firstname";
    public static final String LASTNAME ="lastname";
    public static final String USUAGE ="usage";
    public static final String IBAN="iban";
    public static final String STATUS="status";
    public static final String VALUE ="value";
    public static final String DATE="date";

    public static final String SQL_CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ( "+
            ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            //I_AM_CREDITOR+" BOOLEAN NOT NULL," +
            FIRSTNAME+" TEXT," +
            LASTNAME +" TEXT NOT NULL," +
            USUAGE +" TEXT," +
            IBAN+" TEXT NOT NULL,"+
            STATUS+" TEXT NOT NULL,"+
            VALUE +" DOUBLE NOT NULL," +
            DATE+" DATE NOT NULL);";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";
}
