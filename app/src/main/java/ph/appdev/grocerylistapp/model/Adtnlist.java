package ph.appdev.grocerylistapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Adtnlist implements Parcelable {
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

    public Adtnlist (String category, String name, Double value, int isChecked, Double amount){
        this.category = category;
        this.name = name;
        this.value = value;
        this.isChecked = isChecked;
        this.value = amount;
    }

    protected Adtnlist(Parcel in) {
        id = in.readInt();
        category = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            value = null;
        } else {
            value = in.readDouble();
        }
        isChecked = in.readInt();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
    }

    public static final Creator<Adtnlist> CREATOR = new Creator<Adtnlist>() {
        @Override
        public Adtnlist createFromParcel(Parcel in) {
            return new Adtnlist(in);
        }

        @Override
        public Adtnlist[] newArray(int size) {
            return new Adtnlist[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(category);
        dest.writeString(name);
        if (value == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(value);
        }
        dest.writeInt(isChecked);
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(amount);
        }
    }
}
