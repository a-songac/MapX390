package soen390.mapx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import soen390.mapx.LogUtils;
import soen390.mapx.R;
import soen390.mapx.UiUtils;
import soen390.mapx.database.DbContentManager;
import soen390.mapx.helper.PreferenceHelper;

public class SplashScreenActivity extends Activity{

    private final static int SPLASH_TIME_OUT = 3000;
    private static boolean isDownloadingContent = true;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        PreferenceHelper.getInstance().init(this);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        if (isDownloadingContent) {
            if ((validateNetwork())) {

                new DownloadJSON().execute();

            } else {
                UiUtils.displayToastLong("Could not connect to network");
            }


        } else {
            quitOnDelay();
        }


    }

    /**
     * Finish splash screen activity after delay
     */
    private void quitOnDelay() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                startNextActivity();
            }
        }, SPLASH_TIME_OUT);
    }

    /**
     * Launch next activity
     */
    private void startNextActivity() {
        Intent intent = PreferenceHelper.getInstance().isLanguagePreferenceInit()?
                new Intent(SplashScreenActivity.this, MainActivity.class):
                new Intent(SplashScreenActivity.this, InitLanguageActivity.class);

        startActivity(intent);

        finish();
    }

    /**
     * Validate that there is network available
     * @return
     */
    private boolean validateNetwork(){

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Async task to download json
     */
    private class DownloadJSON extends AsyncTask<Void, Integer, String> {

        private final String JSON_URL = "http://users.encs.concordia.ca/~a_songac/dummyData.json";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            
        }

        @Override
        protected String doInBackground(Void... params) {
            String data = null;
            try {

                data = downloadUrl(JSON_URL);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            quitOnDelay();

        }
    }

    private String downloadUrl(String urlString) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;
        String dir = "";
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            connection.connect();
            int response = connection.getResponseCode();
            LogUtils.info(this.getClass(), "downloadUrl", "The response is: " + response);
            int fileLength = connection.getContentLength();
            LogUtils.info(this.getClass(), "downloadUrl", "Content length: " + fileLength);


            dir = "";
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //handle case of no SDCARD present
            } else {
                dir = Environment.getExternalStorageDirectory()+ File.separator + "mapx";
                //create folder
                File folder = new File(dir); //folder name
                folder.mkdirs();

                //create file
                File file = new File(dir, "mapx.json");
            }


            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(dir + File.separator + "mapx.json");

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = inputStream.read(data)) != -1) {
                // allow canceling with back button
//                if (isCancelled()) {
//                    input.close();
//                    return null;
//                }
                total += count;

                // publishing the progress....
                if (fileLength > 0) // only if total length is known
//                    publishProgress((int) (total * 100 / fileLength));

                outputStream.write(data, 0, count);
            }

//            // Convert the InputStream into a string
//            String contentAsString = readIt(inputStream, len);
//            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.

        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }


        FileInputStream is= new FileInputStream(dir + File.separator + "mapx.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(br).getAsJsonObject();
        DbContentManager.initDatabaseContent(jsonElement);

        return "";
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
