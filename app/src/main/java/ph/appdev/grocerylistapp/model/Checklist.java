package ph.appdev.grocerylistapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Checklist implements Parcelable {
    public static final String TBL_NAME= "checklist_table";
    public static final String ID = "id";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_UNIT_PRICE = "item_unit_price";
    public static final String IS_CHECKED = "is_checked";
    public static final String ITEM_PRICE = "item_price";
    public static final String QUANTITY = "quantity";

    private int id;
    private String name;
    private Double unit_price;
    private int isChecked;
    private Double price;
    private int quantity;

    public static final String DROP_TBL_NAME = "DROP TABLE IF EXISTS "  + TBL_NAME;
    public static final String CREATE_TBL_NAME = "create table " + TBL_NAME +  " (" +
        ID  + " integer primary key AUTOINCREMENT," +
        ITEM_NAME + " text," +
        ITEM_UNIT_PRICE + " DECIMAL(4,2)," +
        IS_CHECKED + " integer," +
        ITEM_PRICE + " DECIMAL(4,2)," +
        QUANTITY + " integer)";

    public Checklist () {

    }

    public Checklist (int id, String name, Double unit_price, int isChecked, Double price, int quantity){
        this.id = id;
        this.name = name;
        this.unit_price = unit_price;
        this.isChecked = isChecked;
        this.price = price;
        this.quantity = quantity;
    }

    public Checklist (String name, Double unit_price, int isChecked, Double price, int quantity){
        this.name = name;
        this.unit_price = unit_price;
        this.isChecked = isChecked;
        this.price = price;
        this.quantity = quantity;
    }

    protected Checklist(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0) {
            unit_price = null;
        } else {
            unit_price = in.readDouble();
        }
        isChecked = in.readInt();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        quantity = in.readInt();
    }

    public static final Creator<Checklist> CREATOR = new Creator<Checklist>() {
        @Override
        public Checklist createFromParcel(Parcel in) {
            return new Checklist(in);
        }

        @Override
        public Checklist[] newArray(int size) {
            return new Checklist[size];
        }
    };

    public int getId(){
        return id;
    }

    public void setId (int id){
        this.id =  id;
    }

    public String getName(){
        return name;
    }

    public void setName (String name){
        this.name =  name;
    }

    public Double getUnitPrice(){
        return unit_price;
    }

    public void setUnitPrice(Double unit_price){
        this.unit_price  = unit_price;
    }

    public int getisChecked() {
        return isChecked;
    }

    public void setisChecked (int isChecked){
        this.isChecked = isChecked;
    }

    public Double getPrice(){
        return price;
    }

    public void setPrice(Double price){
        this.price  = price;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity (int quantity){
        this.quantity =  quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (unit_price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(unit_price);
        }
        dest.writeInt(isChecked);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        dest.writeInt(quantity);
    }
}
