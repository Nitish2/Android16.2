package com.example.hp.parallel_asynctask;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declaring variables
    Button button;
    ProgressBar progressBar1, progressBar2, progressBar3;
    Download download1, download2, download3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating and initializing objects by ID.
        progressBar1 = (ProgressBar) findViewById(R.id.progressbar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressbar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressbar3);
        button=(Button)findViewById(R.id.download);

        button.setOnClickListener(new View.OnClickListener() { // Onclick Method
            @Override
            public void onClick(View view) {

                /*
                 The first ProgressBar updated by AsyncTask execute in normal approach by calling
                 execute(), the last two ProgressBars are updated by AsyncTask execute in parallel.
                */
                download1 = new Download(progressBar1);  //Creating objects
                download1.execute();
                download2 = new Download(progressBar2);
                StartAsyncTaskInParallel(download2);
                download3 = new Download(progressBar3);
                StartAsyncTaskInParallel(download3);


            }
        });

    }

    private void StartAsyncTaskInParallel(Download download) { //Creating method
        // THREAD_POOL_EXECUTOR an Executor that can be used to execute tasks in parallel.
        /*
          You need to change your build target to be API Level 11 or higher if you wish to directly
         call API Level 11 or higher methods.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            download.execute();
    }

    public class Download extends AsyncTask<Void, Integer, Void> { //Creating method
        ProgressBar progressbar;
        public Download(ProgressBar progressbar) { //Creating constructor
            this.progressbar = progressbar;
        }

        @Override
        //Override this method to perform a computation on a background thread.
        protected Void doInBackground(Void... voids) {
            //Applying loop to execute progressbar till 100 percent
            for(int i=0; i<100; i++){
                try {  //Try statement
                    Thread.sleep(200); // put thread on sleep

                } catch (InterruptedException e) { //Catch Statement
                    e.printStackTrace();
                }
                publishProgress(i); //Displaying the progress in the progressbar

            }
            return null;

        }

        @Override
        //onPreExecute(), invoked on the UI thread before the task is executed.
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        //onPostExecute(Result), invoked on the UI thread after the background computation finishes.
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Download Complete",Toast.LENGTH_LONG).show();
        }

        @Override
        /*
         * onProgressUpdate is invoked on the UI thread after a call to publishProgress.
         * onProgressUpdate method is used to display any form of progress in the user interface while
         the background computation is still executing.
          */
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressbar.setProgress(progress);
            super.onProgressUpdate(values[0]);
        }
    }
}