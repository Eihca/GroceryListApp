package ph.appdev.grocerylistapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ph.appdev.grocerylistapp.CheckListActivity;
import ph.appdev.grocerylistapp.R;
import ph.appdev.grocerylistapp.model.MyList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MyList> myLists;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.listtitle);
            timestamp = view.findViewById(R.id.lastmodified);
        }
    }


    public RecyclerAdapter(Context context, ArrayList<MyList> myLists) {
        this.context = context;
        this.myLists = myLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_mainlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MyList mylist = myLists.get(position);

        holder.title.setText(mylist.getTitle());
        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(mylist.getTimestamp()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoChecklist = new Intent(context, CheckListActivity.class);
                gotoChecklist.putExtra("action", "edit");
                gotoChecklist.putExtra("title", mylist.getTitle());
                gotoChecklist.putExtra("notes", mylist.getNote());
                gotoChecklist.putExtra("lastmodified", mylist.getTimestamp());
                gotoChecklist.putExtra("mylist_id", mylist.getId());
                gotoChecklist.putExtra("mylistobj", mylist);
                /*((Activity) context).startActivityForResult(gotoChecklist, 1);*/
                gotoChecklist.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(gotoChecklist);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myLists.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}