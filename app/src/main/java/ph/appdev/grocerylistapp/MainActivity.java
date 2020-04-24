package ph.appdev.grocerylistapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

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
        adapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position, "rvchecklist");
            }
        }));

        toggleEmptyNotes();
    }

    private void showActionsDialog(final int position, final String fromwhere) {
        CharSequence colors[] = new CharSequence[]{"Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteMyList(myLists.get(position));
                dbHelper.deleteUserMyList(myLists.get(position).getId());
                myLists.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        builder.show();
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
        gotoChecklist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(gotoChecklist);
        /*startActivityForResult(gotoChecklist, 1);*/
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0
        if (dbHelper.getUserMyListsCount(email) > 0) {
            emptyrv.setVisibility(View.GONE);
        } else {
            emptyrv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                if (bundle.getString("action") == "add") {
                    MyList myList = bundle.getParcelable("newmylistobj");
                    if(myList != null){
                        myLists.add(myList);
                        Log.d("time", myList.getTimestamp());
                        adapter.notifyDataSetChanged();
                    }
                }
                else {

                }

            }
        }
    }
}
