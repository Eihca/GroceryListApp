package ph.appdev.grocerylistapp.model;

public class User {
    public static final String TBL_USER = "user_table";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String USER_NAME = "name";
    public static final String BUDGET = "budget";

    private int id;
    private String email;
    private String password;
    private String name;
    private Double budget;

    public static final String DROP_TBL_USER = "DROP TABLE IF EXISTS "  + TBL_USER;
    public static final String CREATE_TBL_USER = "create table " + TBL_USER +  " (" +
        ID  + " integer primary key AUTOINCREMENT," +
        EMAIL + " text UNIQUE," +
        PASSWORD + " text," +
        USER_NAME + " text, " +
        BUDGET + " DECIMAL(4,2))";



    public User (int id, String email, String password, String name, Double budget){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.budget = budget;
    }

    public int getId(){
        return id;
    }

    public void setId (int id){
        this.id =  id;
    }

    public String getEmail (){
        return email;
    }

    public void setEmail (String email){
        this.email =  email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}
