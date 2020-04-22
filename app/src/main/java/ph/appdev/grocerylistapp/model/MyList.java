package ph.appdev.grocerylistapp.model;

public class MyList {
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
}
