package ph.appdev.grocerylistapp;

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

import java.util.ArrayList;
import java.util.Objects;

import ph.appdev.grocerylistapp.adapter.AdtnllistAdapter;
import ph.appdev.grocerylistapp.adapter.ChecklistAdapter;
import ph.appdev.grocerylistapp.adapter.RecyclerAdapter;
import ph.appdev.grocerylistapp.model.Adtnlist;
import ph.appdev.grocerylistapp.model.Checklist;

public class CheckListActivity extends AppCompatActivity implements ChecklistAdapter.TotalListener{
    private static final int ITEMS_DIALOG = 1;
    private static final int ADTNL_DIALOG = 2;
    private int selectedItem;
    private double itemstotal;
    private double adtnltotal;
    private double finaltotal;
    DBHelper dbHelper;
    EditText title, notes;
    TextView timestamp, itemstotalprice, totalprice;
    RecyclerView rvChecklist, rvAdtnllist;
    ArrayList<Checklist> checklists = new ArrayList();
    ArrayList<Adtnlist> adtnlists = new ArrayList();
    ChecklistAdapter cadapter;
    AdtnllistAdapter aadapter;
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

        aadapter = new AdtnllistAdapter(this, adtnlists);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        double price_per_item, amount_per_info;
        Bundle bundle = data.getExtras();
        price_per_item = bundle.getDouble("unit_price", 0.0) * bundle.getInt("quantity", 0);
        amount_per_info = (bundle.getDouble("value", 0.0)/100) * itemstotal;

        if(requestCode == ITEMS_DIALOG){
            if(resultCode == Activity.RESULT_OK){
                if(bundle != null){
                    if(bundle.getString("action").equals("edit")){
                        Toast.makeText(getApplicationContext(), "position:" +String.valueOf(selectedItem), Toast.LENGTH_SHORT).show();
                        Checklist tempchklist = bundle.getParcelable("modlistobj");
                        checklists.set(selectedItem, tempchklist);

                    }
                    else{
                        checklists.add(new Checklist(bundle.getString("name"), bundle.getDouble("unit_price", 0.0), 0, price_per_item, bundle.getInt("quantity", 0)));
                    }

                    cadapter.notifyDataSetChanged();
                    itemstotalprice.setText(String.valueOf(cadapter.returnTotal()));
                }
                else{
                    Toast.makeText(getApplicationContext(), "position:" +String.valueOf(selectedItem), Toast.LENGTH_SHORT).show();

                    //Nothing Happens
                }
            }
            else {
                if(resultCode == Activity.RESULT_OK){
                    if(bundle != null){
                        if(bundle.getString("action").equals("edit")){
                            adtnlists.set(selectedItem, new Adtnlist(bundle.getString("category"), bundle.getString("name"), bundle.getDouble("value", 0.0), 0, amount_per_info));
                        }
                        else{
                            adtnlists.add(new Adtnlist(bundle.getString("category"), bundle.getString("name"), bundle.getDouble("value", 0.0), 0, amount_per_info));
                        }
                        aadapter.notifyDataSetChanged();
                    }
                    else{
                        //Nothing Happens
                    }

                }
            }
        }
    }

    @Override
    public void onTotalUpdate(double total) {
        itemstotalprice.setText(""+total);
        this.itemstotal = total;
    }

    @Override
    public void onSelectedItemUpdate(int selecteditem) {
        this.selectedItem = selecteditem;
    }
}
