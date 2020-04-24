package ph.appdev.grocerylistapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import ph.appdev.grocerylistapp.adapter.AdtnllistAdapter;
import ph.appdev.grocerylistapp.adapter.ChecklistAdapter;
import ph.appdev.grocerylistapp.adapter.RecyclerAdapter;
import ph.appdev.grocerylistapp.model.Adtnlist;
import ph.appdev.grocerylistapp.model.Checklist;
import ph.appdev.grocerylistapp.model.MyList;
import ph.appdev.grocerylistapp.model.User;

public class CheckListActivity extends AppCompatActivity implements ChecklistAdapter.TotalListener, AdtnllistAdapter.TotalListener{
    private static final int ITEMS_DIALOG = 1;
    private static final int ADTNL_DIALOG = 2;
    private int selectedItem;
    private int selectedInfo;
    private double itemstotal = 0;
    private double adtnltotal = 0;
    private double finaltotal = 0;
    private Calendar calendar;
    private String date;
    private SimpleDateFormat customdateformat;
    DBHelper dbHelper;
    EditText title, notes;
    TextView timestamp, itemstotalprice, adtntotalprice, totalprice;
    RecyclerView rvChecklist, rvAdtnllist;
    ArrayList<Checklist> checklists = new ArrayList();
    ArrayList<Adtnlist> adtnlists = new ArrayList();
    ChecklistAdapter cadapter;
    AdtnllistAdapter aadapter;
    DecimalFormat df = new DecimalFormat("#.##");
    Bundle bundle = new Bundle();
    MyList myList = new MyList();
    ArrayList<Integer> tobedeletedcl = new ArrayList();
    ArrayList<Integer> tobedeletedal = new ArrayList();
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        dbHelper = new DBHelper(this);

        title = findViewById(R.id.list_title);
        timestamp = findViewById(R.id.list_lastmodified);
        notes = findViewById(R.id.list_notes);
        rvChecklist = findViewById(R.id.rvlistitems);
        rvAdtnllist = findViewById(R.id.rvaddtnlinfo);
        itemstotalprice = findViewById(R.id.itemstotalprice);
        adtntotalprice = findViewById(R.id.adtnltotalprice);
        totalprice = findViewById(R.id.totalprice);

        bundle = getIntent().getExtras();
        assert bundle != null;
        myList = bundle.getParcelable("mylistobj");
        if(bundle.getString("action").toLowerCase().equals("edit")){
            title.setText(myList.getTitle());
            timestamp.setText(myList.getTimestamp());
            notes.setText(bundle.getString("notes"));
            checklists = dbHelper.getUserMyListChecklists(myList.getId());
            adtnlists = dbHelper.getUserMyListAdtnlists(myList.getId());
        }

        cadapter = new ChecklistAdapter(this, checklists, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvChecklist.setLayoutManager(layoutManager);
        rvChecklist.setItemAnimator(new DefaultItemAnimator());
        /*        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));*/
        rvChecklist.setAdapter(cadapter);
        rvChecklist.addOnItemTouchListener(new RecyclerTouchListener(this,
                rvChecklist, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position, "rvchecklist");
            }
        }));

        aadapter = new AdtnllistAdapter(this, adtnlists, this);
        RecyclerView.LayoutManager alayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAdtnllist.setLayoutManager(alayoutManager);
        rvAdtnllist.setItemAnimator(new DefaultItemAnimator());
        /*        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));*/
        rvAdtnllist.setAdapter(aadapter);
        rvAdtnllist.addOnItemTouchListener(new RecyclerTouchListener(this,
                rvAdtnllist, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position, "rvadtnlist");
            }
        }));

        updateValueofViewsOutsideRV();
    }

    @SuppressLint("DefaultLocale")
    private void updateValueofViewsOutsideRV(){
        itemstotal = cadapter.returnTotal();
        adtnltotal = getTotalAmount();
        itemstotalprice.setText(String.format("%.2f",cadapter.returnTotal()));
        adtntotalprice.setText("- " + String.format("%.2f", aadapter.returnTotal()));
        totalprice.setText(String.format("%.2f",getFinalPrice()));
    }

    private boolean validateTitle() {

        if (title.getText().toString().isEmpty() || title.getText().toString().equals("")) {
            title.setError("Enter a title");
            title.requestFocus();
            return false;
        } else {
            title.setError(null);
        }
        return true;
    }

    private void showActionsDialog(final int position, final String fromwhere) {
        CharSequence colors[] = new CharSequence[]{"Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(fromwhere.equals("rvchecklist")){
                    deleteRow(position, "checklists");
                }else {
                    deleteRow(position, "adtnlists");
                }
            }
        });
        builder.show();
    }

    private void deleteRow(int position, String whatlist) {
        if(whatlist.equals("checklists")){
            if(checklists.get(position).getId()>0){
                tobedeletedcl.add(checklists.get(position).getId());
            }
            checklists.remove(position);
            cadapter.notifyItemRemoved(position);
        }else {
            if(adtnlists.get(position).getId()>0){
                tobedeletedal.add(adtnlists.get(position).getId());
            }
            adtnlists.remove(position);
            aadapter.notifyItemRemoved(position);
        }
        updateValueofViewsOutsideRV();
    }

    public void backtoMain(View view){
/*        Intent tomain = new Intent();
        setResult(RESULT_CANCELED, tomain);*/
        finish();
    }

    public void openDialog(View view){
        if(view.getId() == R.id.additembtn){
            Intent gotoItemsDialog = new Intent(getApplicationContext(),ItemsDialog.class);
            gotoItemsDialog.putExtra("action", "add");
            startActivityForResult(gotoItemsDialog, ITEMS_DIALOG);
        }
        else {
            Intent gotoAddtnlInfoDialog = new Intent(getApplicationContext(), AddtnlInfoDialog.class);
            gotoAddtnlInfoDialog.putExtra("action", "add");
            startActivityForResult(gotoAddtnlInfoDialog, ADTNL_DIALOG);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void saveMyList(View view){
        Intent backtoMain = new Intent(this, MainActivity.class);
        long itemid, mylistid, infoid, newmylistid;

        if(!validateTitle()){
            return;
        }

        assert bundle != null;
        String addoredit = bundle.getString("action");
        if (addoredit.equals("edit")){

            for (int tbdclid: tobedeletedcl){
                dbHelper.deleteChecklist(tbdclid);
                dbHelper.deleteMyChecklist(tbdclid);
            }
            for (int tbdalid: tobedeletedal){
                dbHelper.deleteAdtnlist(tbdalid);
                dbHelper.deleteMyAdtnlist(tbdalid);
            }

            mylistid = bundle.getInt("mylist_id", 0);
            for (Checklist item : checklists){
                if(item.getId() > 0){
                    dbHelper.updateChecklist(item);
                }else {
                    itemid = dbHelper.insertChecklist(item.getName(), item.getUnitPrice(), item.getisChecked(), item.getPrice(), item.getQuantity());
                    dbHelper.insertMyChecklists(mylistid, itemid);
                }
            }
            for (Adtnlist info: adtnlists){
                if(info.getId() > 0){
                    dbHelper.updateAdtnlist(info);
                }else {
                    infoid = dbHelper.insertAdtnlist(info.getCategory(), info.getName(), info.getValue(), info.getAmount(),  info.getisChecked());
                    dbHelper.insertMyAdtnlists(mylistid, infoid);
                }
            }
            myList.setTitle(title.getText().toString());
            myList.setNote(notes.getText().toString());

            calendar = Calendar.getInstance();
            customdateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = customdateformat.format(calendar.getTime());

            myList.setTimestamp(date);
            dbHelper.updateMList(myList);
//            backtoMain.putExtra("action", "save");

        }
        else {
            //new list
            mylistid = dbHelper.insertMList(title.getText().toString(), notes.getText().toString());
            for (Checklist item : checklists){
                itemid = dbHelper.insertChecklist(item.getName(), item.getUnitPrice(), item.getisChecked(), item.getPrice(), item.getQuantity());
                dbHelper.insertMyChecklists(mylistid, itemid);
            }
            for (Adtnlist info: adtnlists){
                infoid = dbHelper.insertAdtnlist(info.getCategory(), info.getName(), info.getValue(),info.getAmount(), info.getisChecked());
                dbHelper.insertMyAdtnlists(mylistid, infoid);
            }
            Cursor cursor = dbHelper.getUser(bundle.getString("logged_user"));
            if(cursor.getCount() != 0){
                cursor.moveToFirst();
                newmylistid = dbHelper.insertUserMylists(cursor.getInt(cursor.getColumnIndex(User.ID)), mylistid);
                MyList newmylist = new MyList();
                dbHelper.getMyList(newmylistid);
//                backtoMain.putExtra("newmylistobj", newmylist);

            }
//            setResult(RESULT_OK, backtoMain);
//            backtoMain.putExtra("action", "add");
        }

        startActivity(backtoMain);
        finish();


    }

    public double getTotalAmount(){
        double totalamount = 0;
        for (Adtnlist item : adtnlists){
            if(item.getisChecked() == 1){
                totalamount = totalamount + item.getAmount();
            }
        }
        return totalamount;
    }

    public void updateAmountPerInfo(){
        for (Adtnlist info: adtnlists){
            info.setAmount(Double.parseDouble(df.format((info.getValue() / 100) * itemstotal)));
        }
        aadapter.notifyDataSetChanged();
/*        this.adtnltotal = aadapter.returnTotal();
        adtntotalprice.setText(String.valueOf(adtnltotal));
        totalprice.setText(String.format("%.2f",getFinalPrice()));*/
        updateValueofViewsOutsideRV();
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();

            if(resultCode == Activity.RESULT_OK){
                if(bundle != null){
                    if(requestCode == ITEMS_DIALOG){
                        Checklist tempchklist = bundle.getParcelable("modlistobj");
                        if(bundle.getString("action").equals("edit")){
                            checklists.set(selectedItem, tempchklist);
                        }
                        else{
                            checklists.add(tempchklist);
                        }
                        Log.d("price", Double.toString(tempchklist.getPrice()));
                        cadapter.notifyDataSetChanged();
/*                        itemstotalprice.setText(String.format("%.2f", cadapter.returnTotal()));
                        itemstotal = cadapter.returnTotal();*/
                    }
                    else{
                        Adtnlist tempadtnlist = bundle.getParcelable("adtnlistobj");
                        double amount_per_info = Double.parseDouble(df.format((tempadtnlist.getValue()/100) * itemstotal));
                        tempadtnlist.setAmount(amount_per_info);
                        if(bundle.getString("action").equals("edit")){
                            adtnlists.set(selectedInfo, tempadtnlist);
                        }
                        else{
                            adtnlists.add(tempadtnlist);
                        }
                        aadapter.notifyDataSetChanged();
/*                        adtntotalprice.setText(String.format("%.2f",aadapter.returnTotal()));
                        adtnltotal = aadapter.returnTotal();*/
                    }
//                    totalprice.setText(String.format("%.2f",getFinalPrice()));

                    updateValueofViewsOutsideRV();
                    updateAmountPerInfo();
                }
            }
    }

    private double getFinalPrice() {
        double finalprice;
        finalprice = itemstotal - adtnltotal;
        return finalprice;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTotalUpdate(double total) {
      /*  itemstotalprice.setText(""+total);*/
/*        itemstotalprice.setText(String.format("%.2f",cadapter.returnTotal()));
        this.itemstotal = cadapter.returnTotal();
        totalprice.setText(String.format("%.2f", getFinalPrice()));
        this.finaltotal = getFinalPrice();*/
        updateValueofViewsOutsideRV();
        updateAmountPerInfo();
        /*this.itemstotal = total;*/
    }

    @Override
    public void onSelectedItemUpdate(int selecteditem) {
        this.selectedItem = selecteditem;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTotalAmountUpdate(double totalamount) {
/*        adtntotalprice.setText(""+totalamount);*/
/*        adtntotalprice.setText(String.format("%.2f", aadapter.returnTotal()));
        adtnltotal = aadapter.returnTotal();
        totalprice.setText(String.format("%.2f", getFinalPrice()));
        this.finaltotal = getFinalPrice();*/
        updateValueofViewsOutsideRV();
        /*this.adtnltotal = totalamount;*/

    }

    @Override
    public void onSelectedInfoUpdate(int selectedinfo) {
        this.selectedInfo = selectedinfo;
    }
}
