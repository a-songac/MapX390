package soen390.mapx.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

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
import soen390.mapx.ui.view.holder.SplashScreenActivityViewHolder;

public class SplashScreenActivity extends Activity{

    private final static int SPLASH_TIME_OUT = 3000;
    private DownloadJSON downloadJSONTask;
    private SplashScreenActivityViewHolder viewHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
            }else{
                continueOnCreate();
            }
        }else{
            continueOnCreate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    continueOnCreate();
                }else{
                    //App won't work
                }

                return;
            }
        }
    }

    /**
     * set connection retry button
     */
    private void setRetryButton() {
        viewHolder.getRetryButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateNetwork()) {
                    downloadJSONTask.execute();
                    viewHolder.getRetryButton().setVisibility(View.GONE);
                }
            }
        });
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

    private void continueOnCreate(){
        viewHolder = new SplashScreenActivityViewHolder(findViewById(R.id.root));

        PreferenceHelper.getInstance().init(this);
        downloadJSONTask = new DownloadJSON();
        setRetryButton();

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
                    viewHolder.getError().setVisibility(View.VISIBLE);

                }
            } else {

                UiUtils.displayToastLong(getString(R.string.no_connection_toast));
                viewHolder.getRetryButton().setVisibility(View.VISIBLE);
            }


        } else {
            quitOnDelay();
        }
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
            viewHolder.getProgressBar().setVisibility(View.VISIBLE);

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
            // No updates for now

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
            String dir = ContentManager.getMediaDirectoryPath() + DbContentManager.JSON_FILE_NAME;

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
            String destinationDirectory = ContentManager.getMediaDirectoryPath() + DbContentManager.EXTERNAL_STORAGE_MAPX_DIR;

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

            String path, destinationPath, adjustedPath;
            List<ExpositionContent> mediaContents = ContentManager.getAllExpositionMediaContents();
            for (ExpositionContent expositionContent: mediaContents) {

                path = destinationPath = adjustedPath = expositionContent.getContent();


                if (-1 != path.lastIndexOf(File.separator)) {
                    destinationPath = getFileNameOnly(path);
                    expositionContent.setContent(destinationPath);
                    expositionContent.save();
                }

                String destination =
                        Environment.getExternalStorageDirectory()
                                + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR
                                + File.separator + destinationPath;

                if (path.charAt(0) == File.separatorChar)
                    adjustedPath = path.substring(1);

                downloadUrl(MEDIA_URL + adjustedPath, destination);

                LogUtils.info(this.getClass(), "downloadMediaFiles", "Downloaded media content: " + path);

            }

        }

        /**
         * Download floor maps
         * @throws IOException
         */
        private void downloadFloorMaps() throws IOException {

            String path, destinationPath, adjustedPath;
            List<Floor> floors = Floor.listAll(Floor.class);
            for (Floor floor: floors) {

                path = destinationPath = adjustedPath = floor.getImageFilePath();

                if (-1 != path.lastIndexOf(File.separator)) {
                    destinationPath = getFileNameOnly(path);
                    floor.setImageFilePath(destinationPath);
                    floor.save();
                }

                String destination =
                        Environment.getExternalStorageDirectory()
                                + File.separator +DbContentManager.EXTERNAL_STORAGE_MAPX_DIR
                                + File.separator + destinationPath;

                if (path.charAt(0) == File.separatorChar)
                    adjustedPath = path.substring(1);

                downloadUrl(MEDIA_URL + adjustedPath, destination);

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
