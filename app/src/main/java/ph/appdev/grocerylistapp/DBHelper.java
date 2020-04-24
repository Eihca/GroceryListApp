package ph.appdev.grocerylistapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import ph.appdev.grocerylistapp.adapter.AdtnllistAdapter;
import ph.appdev.grocerylistapp.model.Adtnlist;
import ph.appdev.grocerylistapp.model.Checklist;
import ph.appdev.grocerylistapp.model.MyList;
import ph.appdev.grocerylistapp.model.User;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "GroceryList.db";
    private static final int DATABASE_VERSION = 1;

    private final String LOG = "Grocery DB";
    private final static String TBL_USERMYLIST = "usermylist_table";
    private final static String TBL_MYCHECKLIST = "mychecklist_table";
    private final static String TBL_MYADTNLIST = "myadtnlist_table";
    private static final String ID = "usermylist_id";
    private static final String IDORIG = "id";
    private static final String USER_ID = "user_id";
    private static final String MYLIST_ID = "mylist_id";
    private static final String CHECKLIST_ID = "checklist_id";
    private static final String ADTNLIST_ID = "adtnlist_id";

    private static final String CREATE_TABLE_USERMYLIST = "CREATE TABLE "
            + TBL_USERMYLIST + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_ID + " INTEGER," + MYLIST_ID + " INTEGER)";
    private static final String CREATE_TABLE_MYCHECKLIST = "CREATE TABLE "
            + TBL_MYCHECKLIST + "(" + IDORIG + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MYLIST_ID + " INTEGER," + CHECKLIST_ID + " INTEGER)";
    private static final String CREATE_TABLE_MYADTNLIST = "CREATE TABLE "
            + TBL_MYADTNLIST + "(" + IDORIG + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MYLIST_ID + " INTEGER," + ADTNLIST_ID + " INTEGER)";

    private static final String DROP_TABLE_USERMYLIST = "DROP TABLE IF EXISTS "  + TBL_USERMYLIST;
    private static final String DROP_TABLE_MYCHECKLIST = "DROP TABLE IF EXISTS "  + TBL_MYCHECKLIST;
    private static final String DROP_TABLE_MYADTNLIST = "DROP TABLE IF EXISTS "  + TBL_MYADTNLIST;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(User.CREATE_TBL_USER);
        db.execSQL(MyList.CREATE_TBL_MYLIST);
        db.execSQL(Checklist.CREATE_TBL_NAME);
        db.execSQL(Adtnlist.CREATE_TBL_NAME);
        db.execSQL(CREATE_TABLE_USERMYLIST);
        db.execSQL(CREATE_TABLE_MYCHECKLIST);
        db.execSQL(CREATE_TABLE_MYADTNLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(User.DROP_TBL_USER);
        db.execSQL(MyList.DROP_TBL_MYLIST);
        db.execSQL(Checklist.DROP_TBL_NAME);
        db.execSQL(Adtnlist.DROP_TBL_NAME);
        db.execSQL(DROP_TABLE_USERMYLIST);
        db.execSQL(DROP_TABLE_MYCHECKLIST);
        db.execSQL(DROP_TABLE_MYADTNLIST);

        onCreate(db);
    }

    public void dropDB(SQLiteDatabase db){
        db.execSQL(User.DROP_TBL_USER);
        db.execSQL(MyList.DROP_TBL_MYLIST);
        db.execSQL(DROP_TABLE_USERMYLIST);
        db.execSQL(Checklist.DROP_TBL_NAME);
        db.execSQL(Adtnlist.DROP_TBL_NAME);
        db.execSQL(DROP_TABLE_MYCHECKLIST);
        db.execSQL(DROP_TABLE_MYADTNLIST);
        db.execSQL(User.CREATE_TBL_USER);
        db.execSQL(MyList.CREATE_TBL_MYLIST);
        db.execSQL(Checklist.CREATE_TBL_NAME);
        db.execSQL(Adtnlist.CREATE_TBL_NAME);
        db.execSQL(CREATE_TABLE_USERMYLIST);
        db.execSQL(CREATE_TABLE_MYCHECKLIST);
        db.execSQL(CREATE_TABLE_MYADTNLIST);

    }

    public boolean saveUser(String name, String email, String password, double budget)
    {
        Cursor cursor = getUser(email);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User.USER_NAME, name);
        contentValues.put(User.PASSWORD, password);
        contentValues.put(User.BUDGET, budget);

        long result;
        if (cursor.getCount() == 0) { // Record does not exist
            contentValues.put(User.EMAIL, email);
            result = db.insert(User.TBL_USER, null, contentValues);
        } else { // Record exists
            result = db.update(User.TBL_USER, contentValues, User.EMAIL + "=?", new String[] { email });
        }

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getUser(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + User.TBL_USER + " WHERE " + User.EMAIL + " =?";
        return db.rawQuery(sql, new String[] { email });
    }

    public long insertMList(String title, String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        // insert a new row in TBL_MYLIST
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        values.put(MyList.TITLE, title);
        values.put(MyList.NOTE, note);
        long id = db.insert(MyList.TBL_MYLIST, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public void deleteUser(long  user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TBL_USER, ID + " = ?",
                new String[] { String.valueOf(user_id) });
    }

    public MyList getMyList (long mylist_id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        String mlistQuery = "SELECT * FROM " + MyList.TBL_MYLIST + " WHERE " + MyList.ID + " = " + mylist_id;
        Cursor cursor = db.rawQuery(mlistQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare mylist
        MyList mylist = new MyList(
                cursor.getInt(cursor.getColumnIndex(MyList.ID)),
                cursor.getString(cursor.getColumnIndex(MyList.TITLE)),
                cursor.getString(cursor.getColumnIndex(MyList.TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(MyList.NOTE))
                );

        // close the db connection
        cursor.close();

        return mylist;
    }

    public ArrayList<MyList> getUserMyLists(String email) {
        ArrayList< MyList> mylists = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String usermlistsQuery = "SELECT * FROM " + TBL_USERMYLIST + " tum, " +  User.TBL_USER + " tu, " +  MyList.TBL_MYLIST + " tml  WHERE tu." +  User.EMAIL + " = '" + email  +  "' AND tu."  + User.ID + " = tum." + USER_ID + " AND tml." + MyList.ID + " =  tum." +  MYLIST_ID + " ORDER BY " + MyList.TIMESTAMP + " DESC";
        Cursor cursor = db.rawQuery(usermlistsQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MyList mylist = new MyList();
                mylist.setId(cursor.getInt(cursor.getColumnIndex(MyList.ID)));
                mylist.setTitle(cursor.getString(cursor.getColumnIndex(MyList.TITLE)));
                mylist.setTimestamp(cursor.getString(cursor.getColumnIndex(MyList.TIMESTAMP)));
                mylist.setNote(cursor.getString(cursor.getColumnIndex(MyList.NOTE)));

                mylists.add(mylist);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return mylist
        return mylists;
    }

    public int getUserMyListsCount(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String usermlistsQuery = "SELECT * FROM " + TBL_USERMYLIST + " tum, " +  User.TBL_USER + " tu, " +  MyList.TBL_MYLIST + " tml  WHERE tu." +  User.EMAIL + " = '" + email  +  "' AND tu."  + User.ID + " = tum." + USER_ID + " AND tml." + MyList.ID + " =  tum." +  MYLIST_ID;
        Cursor cursor = db.rawQuery(usermlistsQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteMyList(MyList mylist) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting user’s mylist, check if should delete all is true
        // get all checklist and adtnlinfo under this mylist
        List<Checklist> allChecklist = getUserMyListChecklists(mylist.getTitle());

        // delete all user mylist’s checklist
        for (Checklist checklist : allChecklist) {
            // delete checklist
            deleteChecklist(checklist.getId());
        }
        List<Adtnlist> allAdtnlist = getUserMyListAdtnlists(mylist.getTitle());

        // delete all user mylist’s adtnlist
        for (Adtnlist adtnlist : allAdtnlist) {
            // delete adtnlist
            deleteAdtnlist(adtnlist.getId());
        }

        db.delete(MyList.TBL_MYLIST, MyList.ID + " = ?",
                new String[] { String.valueOf(mylist.getId()) });

        db.delete(TBL_MYCHECKLIST, MYLIST_ID + " = ?",
                new String[] { String.valueOf(mylist.getId()) });

        db.delete(TBL_MYADTNLIST, MYLIST_ID + " = ?",
                new String[] { String.valueOf(mylist.getId()) });

    }


    public int updateMList(MyList mylist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MyList.TITLE, mylist.getTitle());
        values.put(MyList.NOTE, mylist.getNote());
        values.put(MyList.TIMESTAMP, mylist.getTimestamp());

        // updating row
        return db.update(MyList.TBL_MYLIST, values, MyList.ID + " = ?",
                new String[]{String.valueOf(mylist.getId())});
    }

    public long insertUserMylists(long user_id, long mylist_id){
        SQLiteDatabase db = this.getWritableDatabase();

        // insert a new row
        ContentValues values = new ContentValues();
        values.put(USER_ID, user_id);
        values.put(MYLIST_ID, mylist_id);

        long id = db.insert(TBL_USERMYLIST, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public void deleteUserMyList(long mylist_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_USERMYLIST, MYLIST_ID + " = ?",
                new String[] { String.valueOf(mylist_id) });
    }

    public long insertChecklist(String name, Double unit_price, int isChecked, Double price, int quantity) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        // insert a new row
        ContentValues values = new ContentValues();
        values.put(Checklist.ITEM_NAME, name);
        values.put(Checklist.ITEM_UNIT_PRICE, unit_price);
        values.put(Checklist.IS_CHECKED, isChecked);
        values.put(Checklist.ITEM_PRICE, price);
        values.put(Checklist.QUANTITY, quantity);

        long id = db.insert(Checklist.TBL_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Checklist getChecklist (long checklist_id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        String clistQuery = "SELECT  * FROM " + Checklist.TBL_NAME + " WHERE "
                + Checklist.ID + " = " + checklist_id;
        Cursor cursor = db.rawQuery(clistQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare mylist
        Checklist checklist = new Checklist(
                cursor.getInt(cursor.getColumnIndex(Checklist.ID)),
                cursor.getString(cursor.getColumnIndex(Checklist.ITEM_NAME)),
                cursor.getDouble(cursor.getColumnIndex(Checklist.ITEM_UNIT_PRICE)),
                cursor.getInt(cursor.getColumnIndex(Checklist.IS_CHECKED)),
                cursor.getDouble(cursor.getColumnIndex(Checklist.ITEM_PRICE)),
                cursor.getInt(cursor.getColumnIndex(Checklist.QUANTITY))

        );

        // close the db connection
        cursor.close();

        return checklist;
    }

    public int updateChecklist(Checklist checklist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Checklist.ITEM_NAME, checklist.getName());
        values.put(Checklist.ITEM_UNIT_PRICE, checklist.getUnitPrice());
        values.put(Checklist.IS_CHECKED, checklist.getisChecked());
        values.put(Checklist.ITEM_PRICE, checklist.getPrice());
        values.put(Checklist.QUANTITY, checklist.getQuantity());
        // updating row
        return db.update(Checklist.TBL_NAME, values, Checklist.ID + " = ?",
                new String[]{String.valueOf(checklist.getId())});
    }

    public long insertMyChecklists(long mylist_id, long checklist_id){
        SQLiteDatabase db = this.getWritableDatabase();

        // insert a new row
        ContentValues values = new ContentValues();
        values.put(MYLIST_ID, mylist_id);
        values.put(CHECKLIST_ID, checklist_id);

        long id = db.insert(TBL_MYCHECKLIST, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public void deleteMyChecklist(long checklist_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_MYCHECKLIST, CHECKLIST_ID + " = ?",
                new String[] { String.valueOf(checklist_id) });
    }

    public ArrayList<Checklist> getUserMyListChecklists(String mylist_title) {
        ArrayList<Checklist> checklists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TBL_MYCHECKLIST + " tmc, " +  MyList.TBL_MYLIST + " tml, " +  Checklist.TBL_NAME + " tc  WHERE tml." + MyList.TITLE  + " = '" + mylist_title +  "' AND tml."  +  MyList.ID + " = tmc." + MYLIST_ID + " AND tc." + Checklist.ID + " =  tmc." +  CHECKLIST_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Checklist checklist = new Checklist();
                checklist.setId(c.getInt((c.getColumnIndex(Checklist.ID))));
                checklist.setName((c.getString(c.getColumnIndex(Checklist.ITEM_NAME))));
                checklist.setUnitPrice(c.getDouble(c.getColumnIndex(Checklist.ITEM_UNIT_PRICE)));
                checklist.setisChecked(c.getInt(c.getColumnIndex(Checklist.IS_CHECKED)));
                checklist.setPrice(c.getDouble(c.getColumnIndex(Checklist.ITEM_PRICE)));
                checklist.setQuantity(c.getInt(c.getColumnIndex(Checklist.QUANTITY)));

                // adding to checklist
                checklists.add(checklist);
            } while (c.moveToNext());
        }

        return checklists;
    }
    public void deleteChecklist(long checklist_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Checklist.TBL_NAME, Checklist.ID + " = ?",
                new String[] { String.valueOf(checklist_id) });
    }

    public long insertAdtnlist(String category, String name, Double value, Double amount, int isChecked) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        // insert a new row
        ContentValues values = new ContentValues();
        values.put(Adtnlist.INFO_CAT, category);
        values.put(Adtnlist.INFO_NAME, name);
        values.put(Adtnlist.INFO_VALUE, value);
        values.put(Adtnlist.INFO_AMOUNT, amount);
        values.put(Adtnlist.IS_CHECKED, isChecked);

        long id = db.insert(Adtnlist.TBL_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public Adtnlist getAdtnlist (long adtnlist_id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        String alistQuery = "SELECT  * FROM " + Adtnlist.TBL_NAME + " WHERE "
                + Adtnlist.ID + " = " + adtnlist_id;
        Cursor cursor = db.rawQuery(alistQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare mylist
        Adtnlist adtnlist = new Adtnlist(
                cursor.getInt(cursor.getColumnIndex(Adtnlist.ID)),
                cursor.getString(cursor.getColumnIndex(Adtnlist.INFO_CAT)),
                cursor.getString(cursor.getColumnIndex(Adtnlist.INFO_NAME)),
                cursor.getDouble(cursor.getColumnIndex(Adtnlist.INFO_VALUE)),
                cursor.getInt(cursor.getColumnIndex(Adtnlist.IS_CHECKED)),
                cursor.getDouble(cursor.getColumnIndex(Adtnlist.INFO_AMOUNT))
        );

        // close the db connection
        cursor.close();

        return adtnlist;
    }

    public int updateAdtnlist(Adtnlist adtnlist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Adtnlist.INFO_CAT, adtnlist.getCategory());
        values.put(Adtnlist.INFO_NAME, adtnlist.getName());
        values.put(Adtnlist.INFO_VALUE, adtnlist.getValue());
        values.put(Adtnlist.INFO_AMOUNT, adtnlist.getAmount());
        values.put(Adtnlist.IS_CHECKED, adtnlist.getisChecked());

        // updating row
        return db.update(Adtnlist.TBL_NAME, values, Adtnlist.ID + " = ?",
                new String[]{String.valueOf(adtnlist.getId())});
    }

    public long insertMyAdtnlists(long mylist_id, long adtnlist_id){
        SQLiteDatabase db = this.getWritableDatabase();

        // insert a new row
        ContentValues values = new ContentValues();
        values.put(MYLIST_ID, mylist_id);
        values.put(ADTNLIST_ID, adtnlist_id);

        long id = db.insert(TBL_MYADTNLIST, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public void deleteMyAdtnlist(long adtnlist_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_MYADTNLIST, ADTNLIST_ID + " = ?",
                new String[] { String.valueOf(adtnlist_id) });
    }

    public ArrayList<Adtnlist> getUserMyListAdtnlists(String mylist_title) {
        ArrayList<Adtnlist> adtnlists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TBL_MYADTNLIST + " tma, " +  MyList.TBL_MYLIST + " tml, " +  Adtnlist.TBL_NAME + " ta  WHERE tml." + MyList.TITLE  + " = '" + mylist_title +  "' AND tml."  +  MyList.ID + " = tma." + MYLIST_ID + " AND ta." + Adtnlist.ID + " =  tma." +  ADTNLIST_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Adtnlist adtnlist = new Adtnlist ();
                adtnlist.setId(c.getInt((c.getColumnIndex(Adtnlist.ID))));
                adtnlist.setCategory((c.getString(c.getColumnIndex(Adtnlist.INFO_CAT))));
                adtnlist.setName((c.getString(c.getColumnIndex(Adtnlist.INFO_NAME))));
                adtnlist.setValue(c.getDouble(c.getColumnIndex(Adtnlist.INFO_VALUE)));
                adtnlist.setisChecked(c.getInt(c.getColumnIndex(Adtnlist.IS_CHECKED)));
                adtnlist.setAmount(c.getDouble(c.getColumnIndex(Adtnlist.INFO_AMOUNT)));

                // adding to checklist
                adtnlists.add(adtnlist);
            } while (c.moveToNext());
        }

        return adtnlists;
    }
    public void deleteAdtnlist(long adtnlist_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Adtnlist.TBL_NAME, Adtnlist.ID + " = ?",
                new String[] { String.valueOf(adtnlist_id) });
    }


}