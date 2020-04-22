package ph.appdev.grocerylistapp.model;

public class Adtnlist {
    public static final String TBL_NAME= "adtnlist_table";
    public static final String ID = "id";
    public static final String INFO_CAT = "category";
    public static final String INFO_NAME = "info_name";
    public static final String INFO_VALUE = "info_value";
    public static final String IS_CHECKED = "is_checked";
    public static final String INFO_AMOUNT = "amount";

    private int id;
    private String category;
    private String name;
    private Double value;
    private int isChecked;
    private Double amount;

    public static final String DROP_TBL_NAME = "DROP TABLE IF EXISTS "  + TBL_NAME;
    public static final String CREATE_TBL_NAME = "create table " + TBL_NAME +  " (" +
        ID  + " integer primary key AUTOINCREMENT," +
        INFO_CAT + " text," +
        INFO_NAME + " text," +
        INFO_VALUE + " DECIMAL(4,2)," +
        IS_CHECKED + " integer," +
        INFO_AMOUNT + " DECIMAL(4,2))";

    public Adtnlist () {}
    public Adtnlist (int id, String category, String name, Double value, int isChecked, Double amount){
        this.id = id;
        this.category = category;
        this.name = name;
        this.value = value;
        this.isChecked = isChecked;
        this.value = amount;
    }
    public int getId(){
        return id;
    }
    public void setId (int id){
        this.id =  id;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory (String category){
        this.category =  category;
    }

    public String getName(){
        return name;
    }
    public void setName (String name){
        this.name =  name;
    }
    public Double getValue(){
        return value;
    }
    public void setValue(Double value){
        this.value  = value;
    }
    public int getisChecked() {
        return isChecked;
    }
    public void setisChecked (int isChecked){
        this.isChecked = isChecked;
    }
    public Double getAmount(){
        return amount;
    }
    public void setAmount (Double amount){
        this.amount  = amount;
    }
}
