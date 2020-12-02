package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smap_app_project_grp_13_carlog.Constants.Constants;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.ViewModels.RegisterVehicleActivityVM;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RegisterVehicleActivity extends AppCompatActivity {

    //UI widgets
    private Button  registerVHAddBtn, registerVHCancelBtn, registerVHUploadBtn;
    private EditText registerInput;
    private ImageView registerVHImageView;

    //Viewmodel
    private RegisterVehicleActivityVM registerVehicleActivityVM;

    //Internal
    private Boolean sendInputToVM = false;
    private String Document_img1 = "";
    private int imgMaxSize = 400; //Change in MaxSize here to fit the imageview on the page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vehicle);
        overridePendingTransition(R.anim.slide_in, R.anim.fade_out);

        //Setup viewmodel
        registerVehicleActivityVM = new ViewModelProvider(this).get(RegisterVehicleActivityVM.class);

        //Setup ImageView
        registerVHImageView = findViewById(R.id.RegisterVehicleImage);

        //Setup Edittext
        registerInput = findViewById(R.id.RegisterVehicleInputField);

        //Setup Upload Image button
        registerVHUploadBtn = findViewById(R.id.RegisterVehicleUploadBtn);
        registerVHUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AskforPermisson();
            }
        });

        //Setup add Vehicle button
        registerVHAddBtn = findViewById(R.id.RegisterVHAddBtn);
        registerVHAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddVehicle();
            }
        });

        //Setup Cancel button
        registerVHCancelBtn = findViewById(R.id.RegisterVehicleCancelBtn);
        registerVHCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cancel();
            }
        });
    }

    //Inspiration taken from https://stackoverflow.com/questions/42571558/bitmapfactory-unable-to-decode-stream-java-io-filenotfoundexception
    private void AskforPermisson() {
        final int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1){
            //If permission is not given, ask for permission
            if(!checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REGISTERVHACTREQUESTPERMISSON);
            }
            //If permission is already given
            else {
                SelectImage();
            }
        }
    }

    //Ask for permission and return result.
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //Method for handling the return from permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.REGISTERVHACTREQUESTPERMISSON){
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage();
            }
            //If permission is not granted
            else {
                Toast.makeText(this, "Please give your permisson", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //What happens when add vehicle btn is pressed
    private void AddVehicle() {
        //Input from Edittext field
        String vehicleInput = registerInput.getText().toString();
        //Validation
        if(vehicleInput.length() == 0){
            registerInput.setError(getString(R.string.RegisterVechicleNoInput));
            sendInputToVM = false;
        }
        else if (vehicleInput.length() < 7){
            registerInput.setError(getString(R.string.RegisterVehicleTooLongInput));
            sendInputToVM = false;
        }
        else if(vehicleInput.length() > 7){
            registerInput.setError(getString(R.string.RegisterVehicleTooLongInput));
            sendInputToVM = false;
        }
        else {
            //if no error occur, set boolean to send data.
            sendInputToVM = true;
        }
        //        Extra Validation               //
        // Can we make any more validation?
        // Like make sure it is 2 letters, then 5 numbers etc
        // Anything else?
        ///////////////////////////////////////////

        //Only send the data to viewmodel in case the error is not set on the input
        if(sendInputToVM){
            registerVehicleActivityVM.addVehicletoFirebase(vehicleInput);
        }
    }

    //What happens when cancel btn
    private void Cancel() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
    }

    //Inspiration for image upload: http://www.codeplayon.com/2018/11/android-image-upload-to-server-from-camera-and-gallery/
    private void SelectImage() {
        final CharSequence[] options = {getString(R.string.RegisterVehicleSelectImageADOption1), getString(R.string.RegisterVehicleSelectImageADOption2), getString(R.string.RegisterVehicleSelectImageADOption3)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.RegisterVehicleSelectImageTitle));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                //Take a Photo with the camera
                if(options[item].equals(getString(R.string.RegisterVehicleSelectImageADOption1))){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, Constants.REGISTERVHACTSELECTIMGOPTION1);
                }
                //Find a picture from phone gallery
                else if(options[item].equals(getString(R.string.RegisterVehicleSelectImageADOption2))){
                    Intent intent = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Constants.REGISTERVHACTSELECTIMGOPTION2);
                }
                //Cancel
                else if(options[item].equals(getString(R.string.RegisterVehicleSelectImageADOption3))){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK){
            //Return from option 1 = Take Photo!
            if(requestCode == Constants.REGISTERVHACTSELECTIMGOPTION1){
                File file = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : file.listFiles()){
                    if(temp.getName().equals("temp.jpg")){
                        file = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmapOptions);
                    bitmap = getResizedBitmap(bitmap, imgMaxSize);
                    registerVHImageView.setImageBitmap(bitmap);
                    BitmapToString(bitmap);
                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
                    file.delete();
                    OutputStream outFile = null;
                    File newfile = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try{
                        outFile = new FileOutputStream(newfile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            //Return from option 2 = find image in gallery
            else if(requestCode == Constants.REGISTERVHACTSELECTIMGOPTION2) {
                Uri selectedImage = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filepath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filepath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = getResizedBitmap(thumbnail, imgMaxSize);
                Log.w(Constants.REGISTERVHACTTAG, picturePath + "");
                registerVHImageView.setImageBitmap(thumbnail);
                BitmapToString(thumbnail);
            }
        }
    }

    //Convert bitmap to a string
    private String BitmapToString(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    //Method for resizing the bitmap image in case it is wider or higher than necessary.
    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float)height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}