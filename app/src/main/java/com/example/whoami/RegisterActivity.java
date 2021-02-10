package com.example.whoami;

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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputEditText;
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
    TextInputEditText inputFullname,inputEmail,inputPass,inputConfirmPass;
    Button signUp;
    TextView logIn;
    ProgressBar progress;
    String currentImagePath = null;
    String name = null;
    boolean finished = false;
    private static final int IMAGE_REQUEST = 1;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int GALLERY_REQUEST_CODE = 105;
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

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
                fullname = String.valueOf(inputFullname.getText());
                email = String.valueOf(inputEmail.getText());
                password = String.valueOf(inputPass.getText());
                confirmPassword = String.valueOf(inputConfirmPass);


                if(!fullname.equals("") && !email.equals("") && !password.equals("") && !confirmPassword.equals("")) {
                    if (emailValidator(email)){
                        Toast.makeText(getApplicationContext(), "Valid Email Address.", Toast.LENGTH_SHORT).show();
                        if(awesomeValidation.validate()){
                            progress.setVisibility(View.VISIBLE);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[3];
                                    field[0] = "fullname";
                                    field[1] = "email";
                                    field[2] = "password";
                                    //Creating array for data
                                    String[] data = new String[3];
                                    data[0] = fullname;
                                    data[1] = email;
                                    data[2] = password;
                                    PutData putData = new PutData("http://192.168.1.4/detapp/signup.php", "POST", field, data);
                                    name = fullname;
                                    if (putData.startPut()) {
                                        if (putData.onComplete()) {
                                            progress.setVisibility(View.GONE);
                                            String result = putData.getResult();
                                            if(result.equals("Sign Up Success")){
                                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                                //add photo
                                                boolean var = showAlert(v);


                                            }else {
                                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "Passwords don't match!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid Email Address!!", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"All Fields are required!",Toast.LENGTH_SHORT).show();
                }
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
        String imageName = name+"_"+timeStamp+"_";
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
                String imageName = name+"_"+timeStamp+".";
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