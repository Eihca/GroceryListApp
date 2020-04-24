package ph.appdev.grocerylistapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import ph.appdev.grocerylistapp.adapter.AdtnllistAdapter;
import ph.appdev.grocerylistapp.adapter.ChecklistAdapter;
import ph.appdev.grocerylistapp.adapter.RecyclerAdapter;
import ph.appdev.grocerylistapp.model.Adtnlist;
import ph.appdev.grocerylistapp.model.Checklist;

public class CheckListActivity extends AppCompatActivity implements ChecklistAdapter.TotalListener, AdtnllistAdapter.TotalListener{
    private static final int ITEMS_DIALOG = 1;
    private static final int ADTNL_DIALOG = 2;
    private int selectedItem;
    private int selectedInfo;
    private double itemstotal = 0;
    private double adtnltotal = 0;
    private double finaltotal = 0;
    DBHelper dbHelper;
    EditText title, notes;
    TextView timestamp, itemstotalprice, adtntotalprice, totalprice;
    RecyclerView rvChecklist, rvAdtnllist;
    ArrayList<Checklist> checklists = new ArrayList();
    ArrayList<Adtnlist> adtnlists = new ArrayList();
    ChecklistAdapter cadapter;
    AdtnllistAdapter aadapter;
    DecimalFormat df = new DecimalFormat("#.##");
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

        if(Objects.requireNonNull(getIntent().getStringExtra("action")).toLowerCase().equals("edit")){
            title.setText(getIntent().getStringExtra("title"));
            timestamp.setText(getIntent().getStringExtra("lastmodified"));
            notes.setText(getIntent().getStringExtra("notes"));
            checklists = dbHelper.getUserMyListChecklists(getIntent().getStringExtra("title"));
            adtnlists = dbHelper.getUserMyListAdtnlists(getIntent().getStringExtra("title"));
        }

        cadapter = new ChecklistAdapter(this, checklists, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvChecklist.setLayoutManager(layoutManager);
        rvChecklist.setItemAnimator(new DefaultItemAnimator());
        /*        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));*/
        rvChecklist.setAdapter(cadapter);

        aadapter = new AdtnllistAdapter(this, adtnlists, this);
        RecyclerView.LayoutManager alayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAdtnllist.setLayoutManager(alayoutManager);
        rvAdtnllist.setItemAnimator(new DefaultItemAnimator());
        /*        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));*/
        rvAdtnllist.setAdapter(aadapter);

        adtnltotal = getTotalAmount();
        finaltotal = itemstotal + adtnltotal;
        totalprice.setText(String.valueOf(finaltotal));

    }

    public void backtoMain(View view){
        Intent tomain = new Intent();
        setResult(RESULT_CANCELED, tomain);
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

    public void saveMyList(View view){

    }

    public double getTotalAmount(){
        double totalamount = 0;
        for (Adtnlist item : adtnlists){
            if(item.getisChecked() == 1){
                if(item.getCategory().equals("tax")){
                    totalamount = totalamount + item.getAmount();
                }
                else{
                    totalamount = totalamount - item.getAmount();
                }
            }
        }
        return totalamount;
    }

    public void updateAmountPerInfo(){
        for (Adtnlist info: adtnlists){
            info.setAmount((info.getValue() / 100) * itemstotal);
        }
        aadapter.notifyDataSetChanged();
        this.adtnltotal = aadapter.returnTotal();
        adtntotalprice.setText(String.valueOf(adtnltotal));
        totalprice.setText(String.valueOf(getFinalPrice()));
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        double price_per_item;
        Bundle bundle = data.getExtras();
        price_per_item = bundle.getDouble("unit_price", 0.0) * bundle.getInt("quantity", 0);
        price_per_item = Double.parseDouble(df.format(price_per_item));


            if(resultCode == Activity.RESULT_OK){
                if(bundle != null){
                    if(requestCode == ITEMS_DIALOG){
                        if(bundle.getString("action").equals("edit")){
                            Checklist tempchklist = bundle.getParcelable("modlistobj");
                            checklists.set(selectedItem, tempchklist);

                        }
                        else{
                            checklists.add(new Checklist(bundle.getString("name"), bundle.getDouble("unit_price", 0.0), 0, price_per_item, bundle.getInt("quantity", 0)));
                        }
                        cadapter.notifyDataSetChanged();
                        itemstotalprice.setText(String.format("%.2f", cadapter.returnTotal()));
                        itemstotal = cadapter.returnTotal();
                        updateAmountPerInfo();

                    }
                    else{
                        Adtnlist tempadtnlist = bundle.getParcelable("adtnlistobj");
                        double amount_per_info = Double.parseDouble(df.format(tempadtnlist.getValue()/100)) * itemstotal;
                        tempadtnlist.setAmount(amount_per_info);
                        if(bundle.getString("action").equals("edit")){
                            adtnlists.set(selectedInfo, tempadtnlist);
                        }
                        else{
                            adtnlists.add(tempadtnlist);
                        }
                        aadapter.notifyDataSetChanged();
                        adtntotalprice.setText(String.format("%.2f",aadapter.returnTotal()));
                        adtnltotal = aadapter.returnTotal();
                    }
                    totalprice.setText(String.format("%.2f", getFinalPrice()));
                }
            }
    }

    private double getFinalPrice() {
        double finalprice;
        finalprice = itemstotal + adtnltotal;
        return finalprice;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTotalUpdate(double total) {
      /*  itemstotalprice.setText(""+total);*/
        itemstotalprice.setText(String.format("%.2f",cadapter.returnTotal()));
        this.itemstotal = cadapter.returnTotal();
        totalprice.setText(String.format("%.2f", getFinalPrice()));
        this.finaltotal = Double.parseDouble(df.format(Double.parseDouble(totalprice.getText().toString())));
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
        adtntotalprice.setText(String.format("%.2f", aadapter.returnTotal()));
        adtnltotal = aadapter.returnTotal();
        totalprice.setText(String.format("%.2f", getFinalPrice()));
        this.finaltotal = Double.parseDouble(df.format(Double.parseDouble(totalprice.getText().toString())));
        /*this.adtnltotal = totalamount;*/

    }

    @Override
    public void onSelectedInfoUpdate(int selectedinfo) {
        this.selectedInfo = selectedinfo;
    }
}
