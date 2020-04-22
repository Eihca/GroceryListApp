package ph.appdev.grocerylistapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class CheckListActivity extends AppCompatActivity {
    private static final int ITEMS_DIALOG = 1;
    private static final int ADTNL_DIALOG = 2;
    DBHelper dbHelper;
    EditText title, notes;
    TextView timestamp;
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

        if(Objects.requireNonNull(getIntent().getStringExtra("action")).toLowerCase().equals("edit")){
            title.setText(getIntent().getStringExtra("title"));
            timestamp.setText(getIntent().getStringExtra("lastmodified"));
            notes.setText(getIntent().getStringExtra("notes"));
            checklists = dbHelper.getUserMyListChecklists(getIntent().getStringExtra("title"));
            adtnlists = dbHelper.getUserMyListAdtnlists(getIntent().getStringExtra("title"));
        }

        cadapter = new ChecklistAdapter(this, checklists);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ITEMS_DIALOG){
            if(requestCode == Activity.RESULT_OK){

            }
            else{

            }
        }
        else {
            if(requestCode == Activity.RESULT_OK){

            }
            else{

            }
        }
    }
}
