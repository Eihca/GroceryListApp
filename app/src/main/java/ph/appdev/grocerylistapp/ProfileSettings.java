package ph.appdev.grocerylistapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import ph.appdev.grocerylistapp.model.User;

public class ProfileSettings extends AppCompatActivity {
    DBHelper dbHelper;
    ConstraintLayout clcam, clgal;
    EditText name, email, password, confirm_pass, budget;
    KeyListener namelistener, emaillistener, budgetlistener;
    ImageView eye, eye1, user_pic;
    Button edit, logout, save, cancel;
    Uri file;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;
    byte imageInByte[];
    Bitmap imagebitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        clcam = findViewById(R.id.clcam);
        clgal = findViewById(R.id.clgal);
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
        user_pic = findViewById(R.id.user_pic);

        dbHelper = new DBHelper(this);

        displayUserInfo();

        clcam.setVisibility(View.GONE);
        clgal.setVisibility(View.GONE);
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



/*        clcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
  *//*              if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    clcam.setEnabled(false);
                    ActivityCompat.requestPermissions(ProfileSettings.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                }*//*
            }
        });*/

    }

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void openGallery(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_GALLERY_IMAGE);
    }

    public void onStop() {
        super.onStop();
        if (imagebitmap != null) {
            imagebitmap.recycle();
            imagebitmap = null;
            System.gc();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                clcam.setEnabled(true);
            }
        }
    }
/*    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }*/

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                user_pic.setImageURI(file);
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                user_pic.setImageBitmap(imageBitmap);

            }
            else if (requestCode == REQUEST_GALLERY_IMAGE){
                Uri photoUri = data.getData();
                if (photoUri != null) {
                    try {
                        imagebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        user_pic.setImageBitmap(imagebitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch picture from gallery."  , Toast.LENGTH_SHORT).show();
                }

            }
            bitmaptobyte();

        }
    }

    public void bitmaptobyte(){
        Bitmap bm=((BitmapDrawable)user_pic.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageInByte = stream.toByteArray();
    }

    public void logOut(View view){
        SavedSharedPreference.setLoggedIn(getApplicationContext(), false, SavedSharedPreference.getLoggedUser(getApplicationContext()));
        Intent logoutintent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(logoutintent);

    }

    public void cancelEdit(View view){
        clcam.setVisibility(View.GONE);
        clgal.setVisibility(View.GONE);
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
        clgal.setVisibility(View.VISIBLE);
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

        boolean result = dbHelper.saveUser(name.getText().toString(), email.getText().toString(), password.getText().toString(), Double.parseDouble(budget.getText().toString()), imageInByte);
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

            byte[] outImage = cursor.getBlob(cursor.getColumnIndex(User.PIC));
            if(outImage == null){
                user_pic.setImageResource(R.drawable.femaleplaceholder);
            }else {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                user_pic.setImageBitmap(theImage);
                bitmaptobyte();
            }

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
