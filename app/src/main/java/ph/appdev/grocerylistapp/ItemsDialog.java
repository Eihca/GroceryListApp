package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ph.appdev.grocerylistapp.model.Checklist;

public class ItemsDialog extends AppCompatActivity {
    DBHelper dbHelper;
    Button addorsave, cancel;
    EditText name, unit_price, quantity;
    Checklist gotIntent;
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
        quantity.setText("1");

        gotIntent = getIntent().getExtras().getParcelable("listobj");
        if(Objects.requireNonNull(getIntent().getStringExtra("action")).toLowerCase().equals("edit")){
            addorsave.setText("SAVE");
            name.setText(gotIntent.getName());
            unit_price.setText(String.valueOf(gotIntent.getUnitPrice()));
            quantity.setText(String.valueOf(gotIntent.getQuantity()));
        }


    }

    public void backtoChecklist(View view){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("action", getIntent().getStringExtra("action"));
        if(view.getId() == R.id.closedialog){
            setResult(RESULT_CANCELED, resultIntent);
        }
        else{
            if(Objects.requireNonNull(getIntent().getStringExtra("action")).toLowerCase().equals("add")) {
                resultIntent.putExtra("name", name.getText().toString());
                resultIntent.putExtra("unit_price", Double.parseDouble(unit_price.getText().toString()));
                resultIntent.putExtra("quantity", Integer.parseInt(quantity.getText().toString()));
            }
            else {
                gotIntent.setName(name.getText().toString());
                gotIntent.setUnitPrice(Double.parseDouble(unit_price.getText().toString()));
                gotIntent.setQuantity(Integer.parseInt(quantity.getText().toString()));
                gotIntent.setPrice(gotIntent.getUnitPrice() * gotIntent.getQuantity());
                resultIntent.putExtra("modlistobj", gotIntent);
            }
            setResult(RESULT_OK, resultIntent);
        }
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
