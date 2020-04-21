package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class SignUpActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText et_name, et_email, et_password, et_confirm_pass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DBHelper(this);
        et_name = findViewById(R.id.name);
        et_email = findViewById(R.id.emailad);
        et_password = findViewById(R.id.password);
        et_confirm_pass = findViewById(R.id.confirmpassword);
    }

    public void backtoLogIn(View view){
        if(view.getId() == R.id.signupbtn){
            save();
        }
        else{
            Intent logintent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(logintent);
        }

    }

    public void save() {
        String name, email, password, confirm_pass;
        name = et_name.getText().toString();
        email = et_email.getText().toString();
        password = et_password.getText().toString();
        confirm_pass = et_confirm_pass.getText().toString();

        if(!validateEditText(et_name, name)){
            return;
        }
        if (!validateEditText(et_email, email)) {
            return;
        }

        if (!validateEmail(et_email, email)) {
            return;
        }

        if (!validateLengthPassword(et_password, password)) {
            return;
        }

        if (!validateEditText(et_confirm_pass, confirm_pass)) {
            return;
        }

        if (!validatePassword(et_password, et_confirm_pass)) {
            return;
        }

        boolean result = dbHelper.saveUser(name, email, password);
        if (result){
            Toast.makeText(getApplicationContext(), "You may Log in to your account now.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to create the account.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateEditText(EditText editText, String text) {
        if (text.isEmpty()) {
            editText.setError("This is required.");
            editText.requestFocus();
            return false;
        } else {
            editText.setError(null);
        }
        return true;
    }

    private boolean validateEmail(EditText et_email, String email) {
        Cursor cursor = dbHelper.getUser(email);
        if(cursor.getCount() != 0){
            et_email.setError("Email already registered.");
            return false;
        }
        else {
            et_email.setError(null);
        }
        return true;
    }

    private boolean validateLengthPassword(EditText et_password, String password) {
        if(password.length() < 8){
            et_password.setError("At least 8 characters");
            return false;
        }
        else {
            et_password.setError(null);
        }
        return true;
    }

    private boolean validatePassword(EditText password, EditText et_confirm_pass){
        if(!et_confirm_pass.getText().toString().equals(et_password.getText().toString())){
            et_confirm_pass.setError("Doesn't match with Password");
            et_confirm_pass.requestFocus();
            return false;
        }
        else {
            et_confirm_pass.setError(null);
        }
        return true;
    }
}
