package ph.appdev.grocerylistapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import ph.appdev.grocerylistapp.AddtnlInfoDialog;
import ph.appdev.grocerylistapp.ItemsDialog;
import ph.appdev.grocerylistapp.R;
import ph.appdev.grocerylistapp.model.Adtnlist;
import ph.appdev.grocerylistapp.model.Checklist;
import ph.appdev.grocerylistapp.model.MyList;

public class AdtnllistAdapter extends RecyclerView.Adapter<AdtnllistAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Adtnlist> lists;
    TotalListener totalListener;
    private int selectedItem = -1;
    DecimalFormat df = new DecimalFormat("#.##");


    public interface TotalListener {
        void onTotalAmountUpdate(double total);
        void onSelectedInfoUpdate(int selecteditem);
    }

    public void getSelectedItem(){
        totalListener.onSelectedInfoUpdate(selectedItem);
    }

    public void getTotalUpdate(){
        double total = 0;
        Iterator<Adtnlist> itr = lists.iterator();
        while (itr.hasNext()) {
            Adtnlist item = itr.next();
            if(item.getisChecked() == 1){
                total = total + item.getAmount();
            }
        }
        total = Double.parseDouble(df.format(total));
        totalListener.onTotalAmountUpdate(total);
    }

    public double returnTotal(){
        double total = 0;
        Iterator<Adtnlist> itr = lists.iterator();
        while (itr.hasNext()) {
            Adtnlist item = itr.next();
            if(item.getisChecked() == 1){
                total = total + item.getAmount();
            }
        }
        total = Double.parseDouble(df.format(total));
        return total;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, value, category, amount;
        public CheckBox isChecked;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.info_name);
            value = view.findViewById(R.id.info_value);
            category = view.findViewById(R.id.info_categ);
            amount = view.findViewById(R.id.info_amount);
            isChecked = view.findViewById(R.id.chkbx);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public AdtnllistAdapter(Context context, ArrayList<Adtnlist> lists, TotalListener totalListener) {
        this.context = context;
        this.lists = lists;
        this.totalListener = totalListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_infolist, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Adtnlist list = lists.get(position);

        holder.name.setText(list.getName());
        holder.category.setText(list.getCategory());
        holder.value.setText(String.valueOf(list.getValue()));
        holder.amount.setText(String.valueOf(list.getAmount()));

        if(list.getisChecked() == 1){
            holder.isChecked.setChecked(true);
        }

        holder.category.setText(list.getCategory());

        holder.isChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.getisChecked() == 1){
                    holder.isChecked.setChecked(false);
                    list.setisChecked(0);
                }else{
                    holder.isChecked.setChecked(true);
                    list.setisChecked(1);
                }
                notifyDataSetChanged();
                getTotalUpdate();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = position;
                notifyDataSetChanged();
                getSelectedItem();
                Intent gotoAddtnlInfoDialog = new Intent(context, AddtnlInfoDialog.class);
                gotoAddtnlInfoDialog.putExtra("listobj", list);
                gotoAddtnlInfoDialog.putExtra("action", "edit");
                ((Activity) context).startActivityForResult(gotoAddtnlInfoDialog, 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


}