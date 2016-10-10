package llanes.ezquerro.juan.megadldcli.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import llanes.ezquerro.juan.megadldcli.database.ServerDB;


public class ServersContentProvider extends ContentProvider {
    private ServerDB mServerDB;

    private static final String AUTH = "llanes.ezquerro.juan.megadldcli.providers";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTH + "/servers");

    //UriMatcher
    private static final int SERVERS = 1;
    private static final int SERVER_ID = 2;
    private static final UriMatcher uriMatcher;

    //Inicializamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTH, "servers", SERVERS);
        uriMatcher.addURI(AUTH, "servers/#", SERVER_ID);
    }

    public static final class Server implements BaseColumns {
        private Server() {
        }

        //Nombres de columnas
        public static final String NAME = "name";
        public static final String IP = "ip";
        public static final String PORT = "port";
    }


    @Override
    public boolean onCreate() {
        mServerDB = new ServerDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if (uriMatcher.match(uri) == SERVER_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = mServerDB.getReadableDatabase();

        return db.query(ServerDB.SERVERS_TABLE_NAME, projection, where,
                selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case SERVERS:
                return "vnd.android.cursor.dir/vnd.megadldcli.servers";
            case SERVER_ID:
                return "vnd.android.cursor.item/vnd.megadldcli.server";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId;

        SQLiteDatabase db = mServerDB.getWritableDatabase();

        regId = db.insert(ServerDB.SERVERS_TABLE_NAME, null, values);

        return ContentUris.withAppendedId(CONTENT_URI, regId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        if (uriMatcher.match(uri) == SERVER_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = mServerDB.getWritableDatabase();

        return db.delete(ServerDB.SERVERS_TABLE_NAME, where, selectionArgs);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
