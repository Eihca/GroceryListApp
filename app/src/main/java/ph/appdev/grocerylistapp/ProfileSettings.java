package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import ph.appdev.grocerylistapp.model.User;

public class ProfileSettings extends AppCompatActivity {
    DBHelper dbHelper;
    ConstraintLayout clcam;
    EditText name, email, password, confirm_pass, budget;
    KeyListener namelistener, emaillistener, budgetlistener;
    ImageView eye, eye1;
    Button edit, logout, save, cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        clcam = findViewById(R.id.clcam);
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        budget = findViewById(R.id.user_budget);
        password = findViewById(R.id.user_password);
        eye = findViewById(R.id.show_pass_btn);
        confirm_pass = findViewById(R.id.user_confirm_password);
        eye1 = findViewById(R.id.show_confpass_btn);
        logout = findViewById(R.id.logoutbtn);
        edit = findViewById(R.id.edit_profile);
        cancel = findViewById(R.id.cancelbtn);
        save = findViewById(R.id.save_profile);

        dbHelper = new DBHelper(this);

        displayUserInfo();

        clcam.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        confirm_pass.setVisibility(View.GONE);
        eye.setVisibility(View.GONE);
        eye1.setVisibility(View.GONE);

        namelistener = name.getKeyListener();
        name.setKeyListener(null);
        emaillistener = email.getKeyListener();
        email.setKeyListener(null);
        budgetlistener = budget.getKeyListener();
        budget.setKeyListener(null);

    }

    public void logOut(View view){
        SavedSharedPreference.setLoggedIn(getApplicationContext(), false, SavedSharedPreference.getLoggedUser(getApplicationContext()));
        Intent logoutintent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(logoutintent);

    }

    public void cancelEdit(View view){
        clcam.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        confirm_pass.setVisibility(View.GONE);

        password.setText("");
        confirm_pass.setText("");

        eye.setVisibility(View.GONE);
        eye1.setVisibility(View.GONE);

        name.setKeyListener(null);
        budget.setKeyListener(null);

        displayUserInfo();

        edit.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

    }

    public void editProfile(View view){
        clcam.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        confirm_pass.setVisibility(View.VISIBLE);
        eye.setVisibility(View.VISIBLE);
        eye1.setVisibility(View.VISIBLE);

        name.setKeyListener(namelistener);
        budget.setKeyListener(budgetlistener);

        view.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
    }

    public void saveChanges(View view){
        if(!validateName()){
            return;
        }

        if (!validateLengthPassword()){
            return;
        }
        if (!validatePassword()){
            return;
        }
        if(!validateBudget()){
            return;
        }

        boolean result = dbHelper.saveUser(name.getText().toString(), email.getText().toString(), password.getText().toString(), Double.parseDouble(budget.getText().toString()));
        if (result){
            Toast.makeText(getApplicationContext(), "Saved Changes", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to save changes.", Toast.LENGTH_LONG).show();
        }
    }

    private void displayUserInfo(){
        Cursor cursor = dbHelper.getUser(SavedSharedPreference.getLoggedUser(getApplicationContext()));

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            name.setText(cursor.getString(cursor.getColumnIndex(User.USER_NAME)));
            email.setText(cursor.getString(cursor.getColumnIndex(User.EMAIL)));
            budget.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex(User.BUDGET))));
        }
    }


    private boolean validateName() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Enter a name");
            name.requestFocus();
            return false;
        } else {
            name.setError(null);
        }
        return true;
    }

    private boolean validateBudget() {
        if (budget.getText().toString().isEmpty()) {
            budget.setError("Enter a budget");
            budget.requestFocus();
            return false;
        } else {
            budget.setError(null);
        }
        return true;
    }

    private boolean validateEmail() {
        Cursor cursor = dbHelper.getUser(SavedSharedPreference.getLoggedUser(getApplicationContext()));
        if(cursor.getCount() != 0){
            email.setError("Email already registered.");
            return false;
        } else {
            name.setError(null);
        }
        return true;
    }

    private boolean validateLengthPassword() {
        if(password.length() < 8){
            password.setError("At least 8 characters");
            return false;
        }
        else {
            password.setError(null);
        }
        return true;
    }

    private boolean validatePassword(){
        if(!confirm_pass.getText().toString().equals(password.getText().toString())){
            confirm_pass.setError("Doesn't match with Password");
            confirm_pass.requestFocus();
            return false;
        }
        else {
            confirm_pass.setError(null);
        }
        return true;
    }

    public void togglePassVisibility(View view){
        ImageView imgviewbtn = (ImageView) view;
        if(view.getId() == R.id.show_pass_btn) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                imgviewbtn.setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                imgviewbtn.setImageResource(R.drawable.ic_visibility);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        else {
            if (confirm_pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                imgviewbtn.setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                imgviewbtn.setImageResource(R.drawable.ic_visibility);
                confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void backtoMain(View view){
        Intent tomain = new Intent();
        setResult(RESULT_CANCELED, tomain);
        finish();
    }
}
