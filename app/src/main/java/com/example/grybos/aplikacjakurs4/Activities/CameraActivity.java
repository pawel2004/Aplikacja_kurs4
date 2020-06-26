package com.example.grybos.aplikacjakurs4.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
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
    private Camera.Parameters camParams;
    private ImageView color_effects;
    private ImageView white_balance;
    private ImageView exposure;
    private ImageView size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportActionBar().hide();

        final Bundle bundle = getIntent().getExtras();

        Log.d("control", "Klucz: " + bundle.getInt("key"));

        take = findViewById(R.id.take);
        confirm = findViewById(R.id.confirm);
        color_effects = findViewById(R.id.color_effects);
        white_balance = findViewById(R.id.white_balance);
        exposure = findViewById(R.id.exposure);
        size = findViewById(R.id.sizes);

        initCamera();

        initPreview();

        cameraParams();

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

                    if (bundle.getInt("key") == 1){

                        Log.d("1", "Koniec!");

                        Intent intent2 = new Intent();
                        intent2.putExtra("fotoData", fdata);
                        setResult(888, intent2);
                        finish();

                    }
                    else {

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

    private void cameraParams(){

        camParams = camera.getParameters();

        Log.d("Parametry", Arrays.toString(camParams.getSupportedWhiteBalance().toArray()));

        Log.d("Parametry", Arrays.toString(camParams.getSupportedPictureSizes().toArray()));

        Log.d("Parametry", Arrays.toString(camParams.getSupportedColorEffects().toArray()));

        Log.d("Parametry", "Minimalna kompensacja naświetlania: " + camParams.getMinExposureCompensation());

        Log.d("Parametry", "Maksymalna kompensacja naświetlania: " + camParams.getMaxExposureCompensation());

        Log.d("Parametry", "bieżąca kompensacja naświetlania: " + camParams.getExposureCompensation());

        color_effects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (camParams.getSupportedColorEffects() == null){

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Uwaga!");
                    alert.setMessage("Twoja kamera nie obsługuje efektów!");
                    alert.setCancelable(false);
                    alert.setNeutralButton("OK", null).show();

                }else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Efekty kolorów:");
                    //nie może mieć setMessage!!!

                    final String[] opcje = new String[camParams.getSupportedColorEffects().toArray().length];

                    for (String tmp: camParams.getSupportedColorEffects()){

                        opcje[camParams.getSupportedColorEffects().indexOf(tmp)] = tmp;

                    }

                    alert.setItems(opcje, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // wyswietl opcje[which]);

                            camParams.setColorEffect(opcje[which]);
                            camera.setParameters(camParams);

                        }
                    });
                    //
                    alert.show();


                }

            }
        });

        white_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (camParams.getSupportedColorEffects() == null){

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Uwaga!");
                    alert.setMessage("Twoja kamera nie obsługuje efektów!");
                    alert.setCancelable(false);
                    alert.setNeutralButton("OK", null).show();

                }else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Balans bieli:");
                    //nie może mieć setMessage!!!

                    final String[] opcje = new String[camParams.getSupportedWhiteBalance().toArray().length];

                    for (String tmp: camParams.getSupportedWhiteBalance()){

                        opcje[camParams.getSupportedWhiteBalance().indexOf(tmp)] = tmp;

                    }

                    alert.setItems(opcje, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // wyswietl opcje[which]);

                            camParams.setWhiteBalance((opcje[which]));
                            camera.setParameters(camParams);

                        }
                    });
                    //
                    alert.show();


                }

            }
        });

        exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (camParams.getSupportedColorEffects() == null){

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Uwaga!");
                    alert.setMessage("Twoja kamera nie obsługuje efektów!");
                    alert.setCancelable(false);
                    alert.setNeutralButton("OK", null).show();

                }else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Ekspozycja:");
                    //nie może mieć setMessage!!!

                    int max = camParams.getMaxExposureCompensation();
                    int min = camParams.getMinExposureCompensation();

                    final String[] opcje = new String[max - min + 1];

                    for (int i = 0; i < opcje.length; i++){

                        opcje[i] = String.valueOf(max);

                        max = max - 1;

                    }

                    Log.d("tablica", Arrays.toString(opcje));

                    alert.setItems(opcje, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // wyswietl opcje[which]);

                            camParams.setExposureCompensation(Integer.parseInt(opcje[which]));
                            camera.setParameters(camParams);

                        }
                    });
                    //
                    alert.show();


                }

            }
        });

        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (camParams.getSupportedColorEffects() == null){

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Uwaga!");
                    alert.setMessage("Twoja kamera nie obsługuje wielkości!");
                    alert.setCancelable(false);
                    alert.setNeutralButton("OK", null).show();

                }else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                    alert.setTitle("Wielkość zdjęcia: ");
                    //nie może mieć setMessage!!!

                    final String[] opcje = new String[camParams.getSupportedPictureSizes().toArray().length];

                    for (Camera.Size tmp: camParams.getSupportedPictureSizes()){

                        Point size = new Point(tmp.width, tmp.height);

                        opcje[camParams.getSupportedPictureSizes().indexOf(tmp)] = size.x + "x" + size.y;

                    }

                    Log.d("tablica", Arrays.toString(opcje));

                    alert.setItems(opcje, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // wyswietl opcje[which]);

                            String[] size = opcje[which].split("x");

                            camParams.setPictureSize(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                            camera.setParameters(camParams);

                        }
                    });
                    //
                    alert.show();


                }

            }
        });

    }

}
