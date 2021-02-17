package com.example.whoami;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    TextInputEditText inputFullname,inputEmail,inputPass,inputConfirmPass;
    Button signUp;
    TextView logIn;
    ProgressBar progress;
    String currentImagePath = null;
    String name = null;
    boolean finished = false;
    FirebaseUser currentUser;
    private static final int IMAGE_REQUEST = 1;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int GALLERY_REQUEST_CODE = 105;


    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullname = findViewById(R.id.fullname);
        inputEmail = findViewById(R.id.email);
        inputPass = findViewById(R.id.password);
        inputConfirmPass = findViewById(R.id.confirm_password);
        signUp = findViewById(R.id.buttonSignUp);
        logIn = findViewById(R.id.loginText);
        progress = findViewById(R.id.progress);

        fAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);

        awesomeValidation.addValidation(this,R.id.password,R.id.confirm_password,R.string.nomatch);

        logIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                String fullname,email,password,confirmPassword;
                fullname = inputFullname.getText().toString().trim();
                email = inputEmail.getText().toString().trim();
                password = inputPass.getText().toString().trim();
                confirmPassword = inputConfirmPass.getText().toString().trim();

                if(fullname.isEmpty()){
                    inputFullname.setError("Fullname is required!");
                    inputFullname.requestFocus();
                    return;
                }

                if(email.isEmpty()){
                    inputEmail.setError("Email is required!");
                    inputEmail.requestFocus();
                    return;
                }

                if(password.isEmpty()){
                    inputPass.setError("Password is required!");
                    inputPass.requestFocus();
                    return;
                }

                if(confirmPassword.isEmpty()){
                    inputConfirmPass.setError("Confirm Password is required!");
                    inputConfirmPass.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    inputEmail.setError("Invalid Email Address!");
                    inputEmail.requestFocus();
                    return;
                }

                if(!awesomeValidation.validate()){
                    inputConfirmPass.setError("Passwords don't match!!");
                    inputConfirmPass.requestFocus();
                    return;
                }

                progress.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullname,email);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        currentUser =FirebaseAuth.getInstance().getCurrentUser();
                                        Toast.makeText(getApplicationContext(),"You've been successfully registered!",Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.GONE);
                                        //add photo
                                        showAlert(v);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Failed to be registered! Try again.",Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),"Failed to be registered!",Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

    }
    public boolean showAlert(View view){
        final AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
        View photoView = getLayoutInflater().inflate(R.layout.take_picture,null);

        Button dialogBtn_cancel = (Button) photoView.findViewById(R.id.cancel_button);
        Button dialogBtn_take_pic = (Button) photoView.findViewById(R.id.take_pic);
        Button dialogBtn_import_pic = (Button) photoView.findViewById(R.id.import_pic);

        alert.setView(photoView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        dialogBtn_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask for camera permissions
                ActivityCompat.requestPermissions(RegisterActivity.this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
                captureImage(v);

            }

        });

        dialogBtn_import_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Okay" ,Toast.LENGTH_SHORT).show(); khass nch3l cam wnkhd pic
                //displayImage(v);
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);



            }

        });
        alertDialog.show();
        return finished;
    }
    public void captureImage(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraIntent.resolveActivity(getPackageManager())!=null){
            File imageFile = null;
            imageFile = getImageFile();
            if(imageFile!=null){
                Uri imageUri = FileProvider.getUriForFile(this,"com.example.android.provider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(cameraIntent,IMAGE_REQUEST);
                finished = true;
            }
        }
    }

    private File getImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = currentUser.getUid();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageName,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = null;
                Uri imageUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageName = currentUser.getUid()+".";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File fileImage = new File(storageDir,imageName+getFileExt(imageUri));

                OutputStream out = null;
                try {
                    out = new FileOutputStream(fileImage);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finished = true;
            }

        }
        Toast.makeText(getApplicationContext(), "Your Picture is Saved!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();


    }
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

}