package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by yg on 15/11/16.
 */

public class TblMyDepts {
    public static final String TABLE_NAME="MyDepts";


    //region PrimaryKeys
    public static final String PERS_I_OWE_IBAN="pers_i_owe_iban";
    //endregion

    //region ForeignKeys
    public static final String STATUS_ID=TblStatus.STATUS_ID;
    //endregion


    public static final String PERS_I_OWE_DATE="pers_i_owe_date";
    public static final String PERS_I_OWE_FIRSTNAME="person_i_owe_firstname";
    public static final String PERS_I_OWE_LASTNAME="person_i_owe_lastname";
    public static final String PERS_I_OWE_VALUE="person_i_owe_value";
    public static final String PERS_I_OWE_USUAGE="person_i_owe_usuage";

    public static final String SQL_CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+
            PERS_I_OWE_IBAN+" INTEGER PRIMARY KEY AUTO_INCREMENT,"+
            PERS_I_OWE_DATE+" DATETIME DEFAULT current_timestamp,"+
            STATUS_ID+" BOOLEAN,"+
            PERS_I_OWE_FIRSTNAME+" TEXT," +
            PERS_I_OWE_LASTNAME+" TEXT," +
            PERS_I_OWE_VALUE+" DOUBLE," +
            PERS_I_OWE_USUAGE+" TEXT);";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";


}
