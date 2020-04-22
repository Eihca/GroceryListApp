package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ItemsDialog extends AppCompatActivity {
    DBHelper dbHelper;
    Button addorsave, cancel;
    EditText name, unit_price, quantity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_items);

        dbHelper = new DBHelper(this);

        addorsave = findViewById(R.id.addorsave);
        cancel = findViewById(R.id.closedialog);
        name = findViewById(R.id.itemname);
        unit_price = findViewById(R.id.itemprice);
        quantity = findViewById(R.id.quantity);

        int gotIntent = getIntent().getIntExtra("checklist_id", 0);
        if(Objects.requireNonNull(getIntent().getStringExtra("action")).toLowerCase().equals("edit")){
            name.setText(dbHelper.getChecklist(gotIntent).getName());
            unit_price.setText(String.valueOf(dbHelper.getChecklist(gotIntent).getUnitPrice()));
            quantity.setText(String.valueOf(dbHelper.getChecklist(gotIntent).getQuantity()));
        }


    }

    public void backtoChecklist(View view){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("action", getIntent().getStringExtra("action"));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void decQuant(View view){
        int quant = Integer.parseInt(quantity.getText().toString());
        quant = quant - 1;
        if(quant<1){

        }else{
            quantity.setText(Integer.toString(quant));
        }
    }

    public void incQuant(View view){
        int quant = Integer.parseInt(quantity.getText().toString());
        quant = quant + 1;
        quantity.setText(Integer.toString(quant));
    }
}
