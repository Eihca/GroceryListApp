package ph.appdev.grocerylistapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int PROF_SET = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoProfileSettings(View view){
        Intent gotoProfSet = new Intent(getApplicationContext(), ProfileSettings.class);
        gotoProfSet.putExtra("logged_user", "henberly");
        startActivity(gotoProfSet);
    }

    public void gotoChecklist(View view){
        Intent gotoChecklist = new Intent(getApplicationContext(),CheckListActivity.class);
        gotoChecklist.putExtra("list_selected", "henberly");
        gotoChecklist.putExtra("action", "add");
        startActivity(gotoChecklist);
    }


}
