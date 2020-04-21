package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProfileSettings extends AppCompatActivity {
    ConstraintLayout clcam;
    EditText name, email, password, confirm_pass, budget;
    KeyListener namelistener, emaillistener, budgetlistener;
    ImageView eye, eye1;
    Button edit, logout;

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
        Intent logoutintent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(logoutintent);
    }

    public void editProfile(View view){
        edit = findViewById(R.id.edit_profile);
        if(edit.getText().toString().toUpperCase() == "EDIT"){
            edit.setText("SAVE");
            clcam.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            confirm_pass.setVisibility(View.VISIBLE);
            eye.setVisibility(View.VISIBLE);
            eye1.setVisibility(View.VISIBLE);

            name.setKeyListener(namelistener);
            email.setKeyListener(emaillistener);
            budget.setKeyListener(budgetlistener);

            logout.setVisibility(View.GONE);
        }
        else {
            edit.setText("EDIT");
            clcam.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            confirm_pass.setVisibility(View.GONE);

            password.setText("");
            confirm_pass.setText("");

            eye.setVisibility(View.GONE);
            eye1.setVisibility(View.GONE);

            name.setKeyListener(null);
            email.setKeyListener(null);
            budget.setKeyListener(null);

            logout.setVisibility(View.VISIBLE);
        }


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
