package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void backtoLogIn(View view){
        if(view.getId() == R.id.signupbtn){

        }
        Intent logintent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(logintent);
    }
}
