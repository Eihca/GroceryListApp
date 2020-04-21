package ph.appdev.grocerylistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "GroceryList.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TBL_USER = "user_table";
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_NAME = "name";
    public static final String LIST_ID = "list_id";

    private final String DROP_TBL_USER = "DROP TABLE IF EXISTS "  + TBL_USER;

    private String CREATE_TBL_USER = "create table " + TBL_USER +  " (" +
    USER_ID  + " integer primary key," +
    USER_EMAIL + " text UNIQUE," +
    USER_PASSWORD + " text," +
    USER_NAME + " text," +
    LIST_ID + " integer)";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TBL_USER);
        onCreate(db);
    }

    public void dropDB(SQLiteDatabase db){
        db.execSQL(DROP_TBL_USER);
        db.execSQL(CREATE_TBL_USER);
    }

    public boolean saveUser(String name, String email, String password)
    {
        Cursor cursor = getUser(email);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);

        long result;
        if (cursor.getCount() == 0) { // Record does not exist
            contentValues.put("email", email);
            result = db.insert(TBL_USER, null, contentValues);
        } else { // Record exists
            result = db.update(TBL_USER, contentValues, "email=?", new String[] { email });
        }

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getUser(String email){

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TBL_USER + " WHERE email=?";
        return db.rawQuery(sql, new String[] { email });
    }

}
