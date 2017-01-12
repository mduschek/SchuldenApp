package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by yg on 15/11/16.
 */

public class TblMyDebts {
    public static final String TABLE_NAME="MyDepts";


    //region PrimaryKeys
    public static final String PERS_I_OWE_IBAN="pers_i_owe_iban";
    //endregion

    //region ForeignKeys
    public static final String STATUS="status";
    //endregion

    public static final String ID = "_id";
    public static final String PERS_I_OWE_DATE="pers_i_owe_date";
    public static final String PERS_I_OWE_FIRSTNAME="person_i_owe_firstname";
    public static final String PERS_I_OWE_LASTNAME="person_i_owe_lastname";
    public static final String PERS_I_OWE_VALUE="person_i_owe_value";
    public static final String PERS_I_OWE_USUAGE="person_i_owe_usuage";

    public static final String SQL_CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ( "+
            ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            PERS_I_OWE_IBAN+" TEXT NOT NULL,"+
            PERS_I_OWE_DATE+" DATE NOT NULL,"+
            STATUS+" TEXT NOT NULL,"+
            PERS_I_OWE_FIRSTNAME+" TEXT," +
            PERS_I_OWE_LASTNAME+" TEXT NOT NULL," +
            PERS_I_OWE_VALUE+" DOUBLE NOT NULL," +
            PERS_I_OWE_USUAGE+" TEXT," +
            "FOREIGN KEY(status) REFERENCES Statuses(status));";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";


}
