package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by yg on 15/11/16.
 */

public class TblWhoOwesMe {
    public static final String TABLE_NAME="WhoOwesMe";

    //<PrimaryKeys>
    public static final String PERS_WHO_OWES_ME_ID="pers_who_owes_me_id AS _id";
    public static final String PERS_WHO_OWES_ME_IBAN="pers_who_owes_me_iban";
    public static final String PERS_WHO_OWES_ME_DATE="pers_who_owes_me_date";
    //</PrimaryKeys>

    //<ForeignKeys>
    public static final String CLIENT_ID= TblClients.CLIENT_ID;
    public static final String STATUS_ID=TblStatus.STATUS_ID;
    //</ForeignKeys>

    public static final String PERS_WHO_OWES_ME_FIRSTNAME="pers_who_owes_me_firstname";
    public static final String PERS_WHO_OWES_ME_LASTNAME="pers_who_owes_me_lastname";
    public static final String PERS_WHO_OWES_ME_VALUE="pers_who_owes_me_value";
    public static final String PERS_WHO_OWES_ME_COMMENT="pers_who_owes_me_comment";

    public static final String SQL_CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+
            PERS_WHO_OWES_ME_ID+" PRIMARY KEY NUMBER,"+
            PERS_WHO_OWES_ME_IBAN+" PRIMARY KEY NUMBER,"+
            PERS_WHO_OWES_ME_DATE+" DATETIME DEFAULT current_timestamp,"+
            CLIENT_ID+" FOREIGN KEY NUMBER,"+
            STATUS_ID+" FOREIGN KEY NUMBER,"+
            PERS_WHO_OWES_ME_FIRSTNAME+" VARCHAR2," +
            PERS_WHO_OWES_ME_LASTNAME+" VARCHAR2," +
            PERS_WHO_OWES_ME_VALUE+" DOUBLE," +
            PERS_WHO_OWES_ME_COMMENT+" VARCHAR2);";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";
}
