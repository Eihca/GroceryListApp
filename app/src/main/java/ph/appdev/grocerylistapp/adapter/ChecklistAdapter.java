package ph.appdev.grocerylistapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ph.appdev.grocerylistapp.CheckListActivity;
import ph.appdev.grocerylistapp.ItemsDialog;
import ph.appdev.grocerylistapp.R;
import ph.appdev.grocerylistapp.model.Checklist;
import ph.appdev.grocerylistapp.model.MyList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Checklist> lists;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, unit_price, price;
        public CheckBox isChecked;
        public EditText quantity;
        private Button btninc, btndec;

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


    public ChecklistAdapter(Context context, ArrayList<Checklist> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_itemlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Checklist list = lists.get(position);

        holder.name.setText(list.getName());
        // Formatting and displaying timestamp
        holder.unit_price.setText(String.valueOf(list.getUnitPrice()));
        holder.price.setText(String.valueOf(list.getPrice()));

        if(list.getisChecked() == 1){
            holder.isChecked.isChecked();
        }

        holder.quantity.setText(list.getQuantity());
        holder.price.setText(String.valueOf(list.getPrice()));

        holder.btndec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(holder.quantity.getText().toString());
                quant = quant - 1;
                if(quant<1){

                }else{
                    holder.quantity.setText(Integer.toString(quant));
                }
            }
        });

        holder.btninc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quant = Integer.parseInt(holder.quantity.getText().toString());
                quant = quant + 1;
                holder.quantity.setText(Integer.toString(quant));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoItemsDialog = new Intent(context, ItemsDialog.class);
                gotoItemsDialog.putExtra("checklist_id", list.getId());
                gotoItemsDialog.putExtra("action", "edit");
                context.startActivity(gotoItemsDialog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}