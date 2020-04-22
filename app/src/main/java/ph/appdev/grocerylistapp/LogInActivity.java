package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import ph.appdev.grocerylistapp.model.MyList;
import ph.appdev.grocerylistapp.model.User;

public class LogInActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ConstraintLayout cllogin;
    EditText et_email, et_password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cllogin = findViewById(R.id.cllogin);

        if(SavedSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            cllogin.setVisibility(View.VISIBLE);
        }

        dbHelper = new DBHelper(this);
        et_email = findViewById(R.id.emailad);
        et_password = findViewById(R.id.password);
    }

    public void togglePassVisibility(View view){
        et_password = findViewById(R.id.password);
        ImageView imgviewbtn = (ImageView) view;
        if(view.getId() == R.id.show_pass_btn){
            if (et_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                imgviewbtn.setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                imgviewbtn.setImageResource(R.drawable.ic_visibility);
                //Hide Password
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void logIn(View view){
        String email, pass;
        email = et_email.getText().toString();
        pass = et_password.getText().toString();

        if (!validateEditText(et_email, email)) {
            return;
        }

        Cursor cursor = dbHelper.getUser(email);
        if(cursor.getCount() == 0){
            et_email.setError("Email is not yet registered.");
        }
        else {
            cursor.moveToFirst();
            if (!pass.equals(cursor.getString(cursor.getColumnIndex(User.PASSWORD)))){
                et_password.setError("Wrong password");
            }
            else {
                SavedSharedPreference.setLoggedIn(getApplicationContext(), true, email);
                Intent logintent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(logintent);
                finish();
            }
        }
    }

    public void gotoSignUp(View view){
        Intent logintent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(logintent);
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



}
