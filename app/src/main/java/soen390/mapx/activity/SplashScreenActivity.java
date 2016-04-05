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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import soen390.mapx.LogUtils;
import soen390.mapx.R;
import soen390.mapx.UiUtils;
import soen390.mapx.database.DbContentManager;
import soen390.mapx.helper.PreferenceHelper;
import soen390.mapx.manager.ContentManager;
import soen390.mapx.model.ExpositionContent;
import soen390.mapx.model.Floor;

public class SplashScreenActivity extends Activity{

    private final static int SPLASH_TIME_OUT = 3000;
    private static boolean isDownloadingContent = true;
    private DownloadJSON downloadJSONTask;
    private ProgressBar progressBar;
    private TextView status;
    private Button retry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        PreferenceHelper.getInstance().init(this);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        downloadJSONTask = new DownloadJSON();

        if (true || !PreferenceHelper.getInstance().isDbInitPreference()) { //TODO check if updates

            if ((validateNetwork())) {

                String destinationDirectory = Environment.getExternalStorageDirectory()
                        + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR;
                File folder = new File(destinationDirectory);

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && (folder.mkdirs() || folder.isDirectory())) {

                    new DownloadJSON().execute();

                } else {
                    LogUtils.error(this.getClass(), "downloadUrl", "Device does not have or cannot access external storage");
                    UiUtils.displayToastLong(getString(R.string.no_external_storage));
                    //TODO show error status in UI

                }
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

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                downloadJson();
                DbContentManager.initDatabaseContent(DbContentManager.prepareJSONSeed());
                downloadExpositionContentMediaFiles();
                downloadFloorMaps();


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
            startNextActivity();

        }

        /**
         * Download json
         * @throws IOException
         */
        private void downloadJson() throws IOException{
            String dir = Environment.getExternalStorageDirectory()
                    + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR
                    + File.separator + DbContentManager.JSON_FILE_NAME;

            downloadUrl(JSON_URL, dir);
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
            String destinationDirectory = Environment.getExternalStorageDirectory()
                    + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR;

            try {

                connection = establishUrlConnection(urlString);

                int fileLength = connection.getContentLength();

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    File folder = new File(destinationDirectory); //folder name

                    if (folder.mkdirs() || folder.isDirectory()) {

                        inputStream = connection.getInputStream();
                        downloadConnectionData(
                                inputStream,
                                downloadDestination,
                                fileLength);

                    } else {
                        LogUtils.error(this.getClass(), "downloadUrl", "Impossible to create mapx directory");
                    }
                } else {
                    LogUtils.error(this.getClass(), "downloadUrl", "Device does not have external storage");
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
         * Download connection content
         */
        public void downloadConnectionData(InputStream inputStream, String path, int fileLength)
                throws IOException{

            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path);

                byte data[] = new byte[4096];
                int count;

                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }
            } finally {

                if (null != outputStream)
                    outputStream.close();
            }
        }


        /**
         * Download Exposition content media files
         * @throws IOException
         */
        private void downloadExpositionContentMediaFiles() throws IOException {

            String path, destinationPath;
            List<ExpositionContent> mediaContents = ContentManager.getAllExpositionMediaContents();
            for (ExpositionContent expositionContent: mediaContents) {

                path = destinationPath = expositionContent.getContent();


                if (-1 != path.lastIndexOf(File.separator)) {
                    destinationPath = getFileNameOnly(path);
                    expositionContent.setContent(destinationPath);
                    expositionContent.save();
                }

                String destination =
                        Environment.getExternalStorageDirectory()
                                + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR
                                + File.separator + destinationPath;
                downloadUrl(MEDIA_URL + path, destination);

                LogUtils.info(this.getClass(), "downloadMediaFiles", "Downloaded media content: " + path);

            }

        }

        /**
         * Download floor maps
         * @throws IOException
         */
        private void downloadFloorMaps() throws IOException {

            String path, destinationPath;
            List<Floor> floors = Floor.listAll(Floor.class);
            for (Floor floor: floors) {

                path = destinationPath = floor.getImageFilePath();

                if (-1 != path.lastIndexOf(File.separator)) {
                    destinationPath = getFileNameOnly(path);
                    floor.setImageFilePath(destinationPath);
                    floor.save();
                }

                String destination =
                        Environment.getExternalStorageDirectory()
                                + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR
                                + File.separator + destinationPath;
                downloadUrl(MEDIA_URL + path, destination);

                LogUtils.info(this.getClass(), "downloadFloorMaps", "Downloaded floor map: " + path);
            }
        }

        /**
         * Get only the name of the file
         * @param path
         * @return
         */
        private String getFileNameOnly(String path) {
            return path.substring(path.lastIndexOf(File.separator) + 1);
        }

    }

}
