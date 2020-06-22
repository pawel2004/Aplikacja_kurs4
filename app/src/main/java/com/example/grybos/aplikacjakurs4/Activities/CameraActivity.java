package com.example.grybos.aplikacjakurs4.Activities;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.grybos.aplikacjakurs4.Helpers.CameraPreview;
import com.example.grybos.aplikacjakurs4.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.grybos.aplikacjakurs4.Activities.MainActivity.dir;


public class CameraActivity extends AppCompatActivity {

    //zmienne
    private ImageView take;
    private ImageView confirm;
    private Camera camera;
    private int cameraId = -1;
    private CameraPreview _cameraPreview;
    private FrameLayout _frameLayout;
    private int control = 0;
    private byte[] fdata;
    private File[] folders;
    private ArrayList<String> names = new ArrayList <String> ();
    String [] names_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportActionBar().hide();

        take = findViewById(R.id.take);
        confirm = findViewById(R.id.confirm);

        initCamera();

        initPreview();

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("take", "Robię zdjęcie");

                camera.takePicture(null, null, camPictureCallback);

                control = 1;

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (control == 1){

                    final AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Gdzie chcesz zapisać zdjęcie?");
                    folders = dir.listFiles();
                    for (File file : folders){

                        names.add(file.getName());

                    }

                    names_array = names.toArray(new String[0]);

                    names.clear();

                    Log.d("names", Arrays.toString(names_array));

                    alert.setItems(names_array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Log.d("path", "Path: " + folders[i]);

                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String d = df.format(new Date());

                            FileOutputStream fs = null;
                            try {
                                fs = new FileOutputStream(folders[i] + "/" + d + ".jpg");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            try {
                                fs.write(fdata);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                fs.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    alert.show();

                }

                else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Uwaga!");
                    alert.setCancelable(false);  //nie zamyka się po kliknięciu poza nim
                    alert.setMessage("Jeszcze nie zrobiłeś zdjęcia!");
                    alert.setNeutralButton("OK", null).show();  // null to pusty click

                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("cykl", "pause camera");

        // jeśli nie zwolnimy (release) kamery, inna aplikacje nie może jej używać

        if (camera != null) {
            camera.stopPreview();
            //linijka nieudokumentowana w API, bez niej jest crash przy wznawiamiu kamery
            _cameraPreview.getHolder().removeCallback(_cameraPreview);
            camera.release();
            camera = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("cykl", "resume camera");

        if (camera == null) {
            initCamera();
            initPreview();
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("cykl", "restart camera");

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("cykl", "stop camera");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("cykl", "start camera");

    }

    private void initCamera() {

        boolean cam = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

        if (!cam) {
            // uwaga - brak kamery

        } else {

            // wykorzystanie danych zwróconych przez kolejną funkcję getCameraId()

            cameraId = getCameraId();
            // jest jakaś kamera!
            if (cameraId < 0) {
                // brak kamery z przodu!
            } else {
                camera = Camera.open(cameraId);
            }

        }
    }

    private int getCameraId(){

        int cid = 0;
        int camerasCount = Camera.getNumberOfCameras(); // gdy więcej niż jedna kamera

        for (int i = 0; i < camerasCount; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cid = i;
            }

//            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                cid = i;
//            }

        }

        return cid;

    }

    private void initPreview(){

        _cameraPreview = new CameraPreview(CameraActivity.this, camera);
        _frameLayout = (FrameLayout) findViewById(R.id.frameLayout1);
        _frameLayout.addView(_cameraPreview);

    }

    private Camera.PictureCallback camPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            // zapisz dane zdjęcia w tablicy typu byte[]
            // do poźniejszego wykorzystania
            // ponieważ zapis zdjęcia w galerii powinien być dopiero po akceptacji butonem

            fdata = data;

            // odswież (lub nie) kamerę (zapobiega to przycięciu się kamery po zrobieniu zdjęcia)

            camera.startPreview();

        }
    };

}
