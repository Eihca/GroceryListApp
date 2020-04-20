package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {
    EditText email, password;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void togglePassVisibility(View view){
        password = findViewById(R.id.password);
        ImageView imgviewbtn = (ImageView) view;
        if(view.getId() == R.id.show_pass_btn){
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                imgviewbtn.setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                imgviewbtn.setImageResource(R.drawable.ic_visibility);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void logIn(View view){
        Intent logintent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(logintent);
    }

    public void gotoSignUp(View view){
        Intent logintent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(logintent);
    }
}
