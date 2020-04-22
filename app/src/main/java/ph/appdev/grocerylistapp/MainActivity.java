package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ph.appdev.grocerylistapp.adapter.RecyclerAdapter;
import ph.appdev.grocerylistapp.model.MyList;
import ph.appdev.grocerylistapp.model.User;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    private static final int PROF_SET = 1;
    TextView emptyrv;
    String email;
    Double budget;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ArrayList<MyList> myLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.mainrv);
        emptyrv = findViewById(R.id.emptyrv);

        Cursor cursor = dbHelper.getUser(SavedSharedPreference.getLoggedUser(getApplicationContext()));

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            email = cursor.getString(cursor.getColumnIndex(User.EMAIL));
            budget = cursor.getDouble(cursor.getColumnIndex(User.BUDGET));
        }

        myLists = dbHelper.getUserMyLists(email);
        adapter = new RecyclerAdapter(this, myLists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
/*        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));*/
        recyclerView.setAdapter(adapter);

        toggleEmptyNotes();
    }


    public void gotoProfileSettings(View view){
        Intent gotoProfSet = new Intent(getApplicationContext(), ProfileSettings.class);
        startActivity(gotoProfSet);
    }

    public void gotoChecklist(View view){
        Intent gotoChecklist = new Intent(getApplicationContext(),CheckListActivity.class);
        gotoChecklist.putExtra("logged_user", email);
        gotoChecklist.putExtra("budget", budget);
        gotoChecklist.putExtra("action", "add");
        startActivity(gotoChecklist);
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0
        if (dbHelper.getUserMyListsCount(email) > 0) {
            emptyrv.setVisibility(View.GONE);
        } else {
            emptyrv.setVisibility(View.VISIBLE);
        }
    }

}
