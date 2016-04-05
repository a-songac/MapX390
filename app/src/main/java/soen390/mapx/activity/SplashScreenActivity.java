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

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    private DownloadJSON downloadJSONTask;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        PreferenceHelper.getInstance().init(this);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        downloadJSONTask = new DownloadJSON();

        if (true || !PreferenceHelper.getInstance().isDbInitPreference()) { //TODO check if updates
            if ((validateNetwork())) {

                new DownloadJSON().execute();

            } else {
                UiUtils.displayToastLong("Could not connect to network");
                // TODO display button to re attempt download
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
    private class DownloadJSON extends AsyncTask<Void, Integer, Void> {

        public static final String JSON_URL = "http://users.encs.concordia.ca/~a_songac/mapx/demoData.json";
        public static final String MEDIA_URL = "http://users.encs.concordia.ca/~a_songac/mapx/media/";
        public static final String NB_MEDIA_FILES_SCRIPT_URL = MEDIA_URL + "nb_files.php";



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String dir = Environment.getExternalStorageDirectory()
                        + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR
                        + File.separator + DbContentManager.JSON_FILE_NAME;

                downloadUrl(JSON_URL, dir);

                JsonArray files = getListOfMediaFiles();
                downloadMediaFiles(files);


            } catch (IOException e) {
                LogUtils.error(this.getClass(), "doInBackground", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);


        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            DbContentManager.initDatabaseContent(DbContentManager.prepareJSONSeed());
            startNextActivity();

        }


        /**
         * Download content
         *
         * @param urlString
         * @return
         * @throws IOException
         */
        private void downloadUrl(String urlString, String downloadDestination) throws IOException {

            InputStream inputStream = null;
            HttpURLConnection connection = null;
            String dir = Environment.getExternalStorageDirectory()
                    + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR;

            try {

                connection = establishUrlConnection(urlString);

                int fileLength = connection.getContentLength();

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    File folder = new File(dir); //folder name

                    if (folder.mkdirs() || folder.isDirectory()) {

                        inputStream = connection.getInputStream();
                        downloadConnectionData(
                                inputStream,
                                downloadDestination,
                                fileLength);

                    } else {
                        // TODO impossible to create map x directory
                    }
                } else {
                    // TODO device does not have external storage
                }

            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }

        }

        /**
         * Get the list of media files to download
         * @return
         */
        public JsonArray getListOfMediaFiles() throws IOException{

            HttpURLConnection connection = null;
            JsonArray filesJson;
            JsonParser jsonParser = new JsonParser();
            InputStream inputStream = null;

            try {

                connection = establishUrlConnection(DownloadJSON.NB_MEDIA_FILES_SCRIPT_URL);
                inputStream = connection.getInputStream();
                String files = readConnectionContent(inputStream);

                LogUtils.info(this.getClass(), "getListOfMediaFiles", "Content: " + files);
                filesJson = jsonParser.parse(files).getAsJsonArray();

            } finally {

                if (inputStream != null)
                    inputStream.close();

                if (connection != null)
                    connection.disconnect();
            }

            return filesJson;
        }

        /**
         * Establish the connection
         * @param urlStr
         * @throws IOException
         */
        public HttpURLConnection establishUrlConnection(String urlStr) throws IOException{

            HttpURLConnection connection;
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return connection;
        }

        /**
         * Get string of connection inputstream
         * @param inputStream
         * @return
         * @throws IOException
         */
        public String readConnectionContent(InputStream inputStream) throws IOException{

            BufferedReader reader = null;

            try {
                String line;
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(
                        inputStream));

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                return sb.toString();
            } finally {
                if (null != reader)
                    reader.close();
            }
        }

        /**
         * Download connection content
         */
        public void downloadConnectionData(InputStream inputStream, String path, int fileLength)
                throws IOException{

            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path);

                byte data[] = new byte[4096];
                long total = 0;
                int count;


                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));

                    outputStream.write(data, 0, count);
                }
            } finally {

                if (null != outputStream)
                    outputStream.close();
            }


        }

        /**
         * Download all media files
         */
        public void downloadMediaFiles(JsonArray filesJsonArray) throws IOException {

            String fileName;

            for(int i = 0; i < filesJsonArray.size(); i++) {

                fileName = filesJsonArray.get(i).getAsString();

                String destination =
                        Environment.getExternalStorageDirectory()
                        + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR
                        + File.separator + fileName;

                downloadUrl(MEDIA_URL + fileName, destination);
                LogUtils.info(this.getClass(), "downloadMediaFiles", "Downloaded " + fileName);

            }


        }

    }

}
