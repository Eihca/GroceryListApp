package ph.appdev.grocerylistapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import ph.appdev.grocerylistapp.CheckListActivity;
import ph.appdev.grocerylistapp.ItemsDialog;
import ph.appdev.grocerylistapp.R;
import ph.appdev.grocerylistapp.model.Checklist;
import ph.appdev.grocerylistapp.model.MyList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Checklist> lists;
    TotalListener totalListener;
    private int selectedItem = -1;
    DecimalFormat df = new DecimalFormat("#.##");

    public interface TotalListener {
        void onTotalUpdate(double total);
        void onSelectedItemUpdate(int selecteditem);
    }

    public void getSelectedItem(){
        totalListener.onSelectedItemUpdate(selectedItem);
    }

    public void getTotalUpdate(){
        double total = 0;
        Iterator<Checklist> itr = lists.iterator();
        while (itr.hasNext()) {
            Checklist item = itr.next();
            if(item.getisChecked() == 1){
                total = total + item.getPrice();
            }
        }
//        total = Double.parseDouble(df.format(total));
        totalListener.onTotalUpdate(total);
    }

    public double returnTotal(){
        double total = 0;
        Iterator<Checklist> itr = lists.iterator();
        while (itr.hasNext()) {
            Checklist item = itr.next();
            if(item.getisChecked() == 1){
                total = total + item.getPrice();
            }
        }
//        total = Double.parseDouble(df.format(total));

        return total;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, unit_price, price;
        public CheckBox isChecked;
        public EditText quantity;
        private ImageView btninc, btndec;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            unit_price = view.findViewById(R.id.item_price);
            price = view.findViewById(R.id.price);
            isChecked = view.findViewById(R.id.chkbx);
            quantity = view.findViewById(R.id.quantitytxt);
            btninc = view.findViewById(R.id.btninc);
            btndec = view.findViewById(R.id.btndec);
        }
    }


    public ChecklistAdapter(Context context, ArrayList<Checklist> lists, TotalListener totalListener ) {
        this.context = context;
        this.lists = lists;
        this.totalListener = totalListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_itemlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Checklist list = lists.get(position);

        holder.name.setText(list.getName());
        // Formatting and displaying timestamp
        holder.unit_price.setText(String.valueOf(list.getUnitPrice()));

        if(list.getisChecked() == 1){
            holder.isChecked.setChecked(true);
        }

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

        holder.quantity.setKeyListener(null);
        holder.quantity.setText(String.valueOf(list.getQuantity()));
        holder.price.setText(String.valueOf(list.getPrice()));

        holder.btndec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(holder.quantity.getText().toString());
                quant = quant - 1;
                if(quant<1){

                }else{
                    holder.quantity.setText(Integer.toString(quant));
                    list.setQuantity(quant);
                    list.setPrice(quant * list.getUnitPrice());
                    notifyDataSetChanged();
                    getTotalUpdate();
                }
            }
        });

        holder.btninc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(holder.quantity.getText().toString());
                quant = quant + 1;
                holder.quantity.setText(Integer.toString(quant));
                list.setQuantity(quant);
                list.setPrice(quant*list.getUnitPrice());
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
                Intent gotoItemsDialog = new Intent(context, ItemsDialog.class);
                gotoItemsDialog.putExtra("listobj", list);
                gotoItemsDialog.putExtra("action", "edit");
                ((Activity) context).startActivityForResult(gotoItemsDialog, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}