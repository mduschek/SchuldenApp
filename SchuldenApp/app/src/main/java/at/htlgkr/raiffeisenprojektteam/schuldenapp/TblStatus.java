package at.htlgkr.raiffeisenprojektteam.schuldenapp;

/**
 * Created by yg on 15/11/16.
 */

public class TblStatus {
    public static final String TABLE_NAME="Statuses";

    public static final String STATUS_ID="status_id AS _id";
    public static final String STATUS="status";

    public static final String SQL_CREATE_TABEL="CREATE TABLE "+TABLE_NAME+"("+
            STATUS_ID+" INTEGER PRIMARY KEY AUTO_INCREMENT,"+
            STATUS+" TEXT NOT NULL);";
    public static final String SQL_DROP_TABLE="DROP TABLE "+TABLE_NAME+";";
}
