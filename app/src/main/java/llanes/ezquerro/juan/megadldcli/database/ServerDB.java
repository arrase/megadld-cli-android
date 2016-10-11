package llanes.ezquerro.juan.megadldcli.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServerDB extends SQLiteOpenHelper {

    public static final String SERVERS_TABLE_NAME = "server";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "megadld";
    private static final String SERVERS_TABLE_CREATE =
            "CREATE TABLE " + SERVERS_TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "ip TEXT, " +
                    "port INTEGER);";

    public ServerDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SERVERS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

