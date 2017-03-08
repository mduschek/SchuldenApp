package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by Alexander on 08.03.17.
 */

public class TblData
{
    public static final String TABLE_NAME="Data";
    public static final String IBAN="iban";

    //region ForeignKeys
    public static final String STATUS="status";
    //endregion

    public static final String ID = "_id";
    public static final String DATE="date";
    public static final String FIRSTNAME="irstname";
    public static final String LASTNAME="lastname";
    public static final String VALUE="value";
    public static final String USAGE="usage";

    public static final String SQL_CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ( "+
            ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            IBAN+" TEXT,"+
            DATE+" DATE NOT NULL,"+
            STATUS+" TEXT NOT NULL,"+
            FIRSTNAME+" TEXT," +
            LASTNAME+" TEXT NOT NULL," +
            VALUE+" DOUBLE NOT NULL," +
            USAGE+" TEXT," +
            "FOREIGN KEY(status) REFERENCES Statuses(status));";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";
}
