package decodertech.com.saviour;

import android.provider.BaseColumns;


/**
 * Created by Dhruve on 9/19/2017.
 */

public final class ContactDb {

private ContactDb(){
}
public class Contact_person implements BaseColumns{
    public static final String TABLE_NAME = "Contacts";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_LOCATION = "location";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_NUMBER + " TEXT, " +
            COLUMN_LOCATION + " INTEGER" + ")";
}

}
