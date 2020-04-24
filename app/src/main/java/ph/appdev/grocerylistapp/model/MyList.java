package ph.appdev.grocerylistapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyList implements Parcelable {
    public static final String TBL_MYLIST= "mylist_table";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TIMESTAMP = "timestamp";
    public static final String NOTE = "note";

    private int id;
    private String title;
    private String timestamp;
    private String note;

    public static final String DROP_TBL_MYLIST = "DROP TABLE IF EXISTS "  + TBL_MYLIST;
    public static final String CREATE_TBL_MYLIST = "create table " + TBL_MYLIST +  " (" +
        ID  + " integer primary key AUTOINCREMENT," +
        TITLE + " text," +
        TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
        NOTE + " text)";

    public MyList () {

    }

    public MyList (int id, String title, String timestamp, String note){
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.note = note;
    }

    protected MyList(Parcel in) {
        id = in.readInt();
        title = in.readString();
        timestamp = in.readString();
        note = in.readString();
    }

    public static final Creator<MyList> CREATOR = new Creator<MyList>() {
        @Override
        public MyList createFromParcel(Parcel in) {
            return new MyList(in);
        }

        @Override
        public MyList[] newArray(int size) {
            return new MyList[size];
        }
    };

    public int getId(){
        return id;
    }

    public void setId (int id){
        this.id =  id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle (String title){
        this.title =  title;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(timestamp);
        dest.writeString(note);
    }
}
