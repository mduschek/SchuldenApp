package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by yg on 15/11/16.
 */

public class TblWhoOwesMe {
    public static final String TABLE_NAME="WhoOwesMe";

    //region <PrimaryKeys>
    public static final String PERS_WHO_OWES_ME_IBAN="pers_who_owes_me_iban";
    // endregion </PrimaryKeys>

    //region <ForeignKeys>
    public static final String STATUS_ID=TblStatus.STATUS_ID;
    //endregion </ForeignKeys>

    public static final String PERS_WHO_OWES_ME_DATE="pers_who_owes_me_date";
    public static final String PERS_WHO_OWES_ME_FIRSTNAME="pers_who_owes_me_firstname";
    public static final String PERS_WHO_OWES_ME_LASTNAME="pers_who_owes_me_lastname";
    public static final String PERS_WHO_OWES_ME_VALUE="pers_who_owes_me_value";
    public static final String PERS_WHO_OWES_ME_USUAGE="pers_who_owes_me_usuage";

    public static final String SQL_CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+
            PERS_WHO_OWES_ME_IBAN+" INTEGER PRIMARY KEY AUTO_INCREMENT,"+
            PERS_WHO_OWES_ME_DATE+" DATETIME DEFAULT current_timestamp,"+
            STATUS_ID+" FOREIGN KEY NUMBER,"+
            PERS_WHO_OWES_ME_FIRSTNAME+" TEXT," +
            PERS_WHO_OWES_ME_LASTNAME+" TEXT," +
            PERS_WHO_OWES_ME_VALUE+" DOUBLE," +
            PERS_WHO_OWES_ME_USUAGE+" TEXT);";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";
}
