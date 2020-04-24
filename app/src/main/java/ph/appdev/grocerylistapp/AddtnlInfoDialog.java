package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import ph.appdev.grocerylistapp.model.Adtnlist;
import ph.appdev.grocerylistapp.model.Checklist;

public class AddtnlInfoDialog extends AppCompatActivity {
    DBHelper dbHelper;
    Button addorsave, cancel;
    EditText name, value;
    Spinner category;
    Adtnlist gotIntent = new Adtnlist();
    DecimalFormat df = new DecimalFormat("#.##");
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

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Tax");
        arrayList.add("Discount");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedcategory = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + selectedcategory,
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        category.setSelection(0);

        if (Objects.requireNonNull(getIntent().getStringExtra("action")).toLowerCase().equals("edit")) {
            gotIntent = getIntent().getExtras().getParcelable("listobj");
            addorsave.setText("SAVE");
            name.setText(gotIntent.getName());
            value.setText(String.valueOf(gotIntent.getValue()));
            ArrayAdapter myAdap = (ArrayAdapter) category.getAdapter();
            category.setSelection(myAdap.getPosition(gotIntent.getCategory()));
        }
    }

    public void backtoChecklist(View view){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("action", getIntent().getStringExtra("action"));
        if (view.getId() == R.id.closedialog) {
            setResult(RESULT_CANCELED, resultIntent);
        } else {
            if(!validateEditText(name)){
                return;
            }
            if (!validateEditText(value)){
                return;
            }
    /*        if (!validateEditText(category)){
                return;
            }*/
            gotIntent.setCategory(category.getSelectedItem().toString());
            gotIntent.setName(name.getText().toString());
            gotIntent.setValue(Double.parseDouble(df.format(Double.parseDouble(value.getText().toString()))));
            if(Objects.requireNonNull(getIntent().getStringExtra("action")).toLowerCase().equals("add")){
                gotIntent.setisChecked(0);
            }
            resultIntent.putExtra("adtnlistobj", gotIntent);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }

    private boolean validateEditText(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("This is required.");
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
        }
        return true;
    }

}
