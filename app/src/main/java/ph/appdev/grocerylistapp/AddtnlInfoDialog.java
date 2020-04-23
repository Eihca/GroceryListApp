package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ph.appdev.grocerylistapp.model.Adtnlist;
import ph.appdev.grocerylistapp.model.Checklist;

public class AddtnlInfoDialog extends AppCompatActivity {
    DBHelper dbHelper;
    Button addorsave, cancel;
    EditText name, value;
    Spinner category;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addtnlinfo);

        dbHelper = new DBHelper(this);
        addorsave = findViewById(R.id.addorsave);
        cancel = findViewById(R.id.closedialog);
        name = findViewById(R.id.infoname);
        value = findViewById(R.id.infovalue);
        category = findViewById(R.id.infocategory);

        Adtnlist gotIntent = getIntent().getExtras().getParcelable("listobj");
        if (Objects.requireNonNull(getIntent().getStringExtra("action")).toLowerCase().equals("edit")) {
            addorsave.setText("SAVE");
            name.setText(gotIntent.getName());
            value.setText(String.valueOf(gotIntent.getValue()));
            /* category.setText(gotIntent.getCategory());*/
        }
    }

    public void backtoChecklist(View view){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("action", getIntent().getStringExtra("action"));
        if (view.getId() == R.id.closedialog) {
            setResult(RESULT_CANCELED, resultIntent);
        } else {
            resultIntent.putExtra("category", "tax");
            resultIntent.putExtra("name", name.getText().toString());
            resultIntent.putExtra("value", value.getText().toString());
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }

}
