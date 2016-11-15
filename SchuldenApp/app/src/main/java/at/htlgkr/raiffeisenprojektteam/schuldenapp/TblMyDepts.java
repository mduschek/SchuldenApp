package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by yg on 15/11/16.
 */

public class TblMyDepts {
    public static final String TABLE_NAME="MyDepts";

    //          <PrimaryKeys>
    public static final String PERS_I_OWE_ID="pers_i_owe_id AS _id";
    public static final String PERS_I_OWE_IBAN="pers_i_owe_iban";
    public static final String PERS_I_OWE_DATE="pers_i_owe_date";
    //          </PrimaryKeys>

    //          <ForeignKeys>
    public static final String CLIENT_ID= TblClients.CLIENT_ID;
    public static final String STATUS_ID=TblStatus.STATUS_ID;
    //          </ForeignKeys>

    public static final String PERS_I_OWE_FIRSTNAME="person_i_owe_firstname";
    public static final String PERS_I_OWE_LASTNAME="person_i_owe_lastname";
    public static final String PERS_I_OWE_VALUE="person_i_owe_value";
    public static final String PERS_I_OWE_COMMENT="person_i_owe_comment";

    public static final String SQL_CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+
            PERS_I_OWE_ID+" PRIMARY KEY NUMBER,"+
            PERS_I_OWE_IBAN+" PRIMARY KEY NUMBER,"+
            PERS_I_OWE_DATE+" DATETIME DEFAULT current_timestamp,"+
            CLIENT_ID+" FOREIGN KEY NUMBER,"+
            STATUS_ID+" FOREIGN KEY NUMBER,"+
            PERS_I_OWE_FIRSTNAME+" VARCHAR2," +
            PERS_I_OWE_LASTNAME+" VARCHAR2," +
            PERS_I_OWE_VALUE+" DOUBLE," +
            PERS_I_OWE_COMMENT+" VARCHAR2);";

    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";


}
