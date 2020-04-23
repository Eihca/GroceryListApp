package ph.appdev.grocerylistapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ph.appdev.grocerylistapp.AddtnlInfoDialog;
import ph.appdev.grocerylistapp.ItemsDialog;
import ph.appdev.grocerylistapp.R;
import ph.appdev.grocerylistapp.model.Adtnlist;
import ph.appdev.grocerylistapp.model.MyList;

public class AdtnllistAdapter extends RecyclerView.Adapter<AdtnllistAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Adtnlist> lists;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, value, category, amount;
        public CheckBox isChecked;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.info_name);
            value = view.findViewById(R.id.info_value);
            category = view.findViewById(R.id.info_categ);
            amount = view.findViewById(R.id.info_amount);
            isChecked = view.findViewById(R.id.chkbx);
        }
    }


    public AdtnllistAdapter(Context context, ArrayList<Adtnlist> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_infolist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Adtnlist list = lists.get(position);

        holder.name.setText(list.getName());
        holder.category.setText(list.getCategory());
        holder.value.setText(String.valueOf(list.getValue()));
        holder.amount.setText(String.valueOf(list.getAmount()));

        if(list.getisChecked() == 1){
            holder.isChecked.isChecked();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAddtnlInfoDialog = new Intent(context, AddtnlInfoDialog.class);
                gotoAddtnlInfoDialog.putExtra("listobj", list);
                gotoAddtnlInfoDialog.putExtra("action", "edit");
                context.startActivity(gotoAddtnlInfoDialog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

}