package ph.appdev.grocerylistapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import ph.appdev.grocerylistapp.adapter.RecyclerAdapter;
import ph.appdev.grocerylistapp.model.MyList;
import ph.appdev.grocerylistapp.model.User;
import ph.appdev.grocerylistapp.touchhandlers.RecyclerItemTouchHelper;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    DBHelper dbHelper;
    private static final int PROF_SET = 1;
    TextView emptyrv;
    String email;
    Double budget;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ArrayList<MyList> myLists;
    ConstraintLayout clmain;
    ImageView prof_pic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prof_pic = findViewById(R.id.user_prof_pic);
        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.mainrv);
        emptyrv = findViewById(R.id.emptyrv);
        clmain = findViewById(R.id.clmain);
        Cursor cursor = dbHelper.getUser(SavedSharedPreference.getLoggedUser(getApplicationContext()));

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            email = cursor.getString(cursor.getColumnIndex(User.EMAIL));
            budget = cursor.getDouble(cursor.getColumnIndex(User.BUDGET));

            byte[] outImage = cursor.getBlob(cursor.getColumnIndex(User.PIC));
            if(outImage == null){
                prof_pic.setImageResource(R.drawable.femaleplaceholder);
            }else {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                prof_pic.setImageBitmap(theImage);
            }
        }


        myLists = dbHelper.getUserMyLists(email);
        adapter = new RecyclerAdapter(this, myLists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        toggleEmptyNotes();
    }

    private void showActionsDialog(final int position) {
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
                toggleEmptyNotes();
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = myLists.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final MyList deletedItem = myLists.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            showActionsDialog(viewHolder.getAdapterPosition());

            adapter.notifyItemChanged(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
/*            Snackbar snackbar = Snackbar
                    .make(clmain, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();*/
        }
    }
}
