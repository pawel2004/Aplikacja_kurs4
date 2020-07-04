package com.example.grybos.aplikacjakurs4.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AlertDialog;

import static com.example.grybos.aplikacjakurs4.Helpers.Settings.URL_SERWERA;

public class UploadFoto extends AsyncTask<String, Void, String> {

    private ProgressDialog pDialog;
    private byte[] bytes;
    private Bitmap originalBitmap;
    private String photo_path;
    private String result;
    private Context context;

    public UploadFoto(Context context, ProgressDialog pDialog, String photo_path) {

        this.pDialog = pDialog;
        this.photo_path = photo_path;
        this.context = context;

    }

    @Override
    protected String doInBackground(String... strings) {

        Log.d("xxx", strings[0]);

        originalBitmap = BitmapFactory.decodeFile(photo_path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        originalBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();

        HttpPost httpPost = new HttpPost(strings[0]);
        httpPost.setEntity(new ByteArrayEntity(bytes));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Komunikat serwera!");
        alert.setMessage(result);
        alert.setCancelable(false);
        alert.setNeutralButton("OK", null);
        alert.show();

        pDialog.dismiss();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog.show();

    }
}
