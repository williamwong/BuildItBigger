package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditibigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * AsyncTask to get joke from API
 */
class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

    // 10.0.2.2 is localhost's IP address in Android emulator
    private static final String ROOT_URL = "http://10.0.2.2:8080/_ah/api/";

    private static MyApi myApiService = null;

    private EndpointsAsyncResponse mDelegate = null;

    public EndpointsAsyncTask(EndpointsAsyncResponse delegate) {
        mDelegate = delegate;
    }

    @Override
    protected final String doInBackground(Void... params) {

        // Initialize service once if it hasn't been initialized yet
        if (myApiService == null) {
            myApiService = new MyApi
                    .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(ROOT_URL)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            // Turn off compression when running against local devappserver
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    })
                    .build();
        }

        try {
            return myApiService.joke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (mDelegate != null) {
            mDelegate.processFinish(result);
        }
    }

    public void onDelegateDestroyed() {
        mDelegate = null;
    }

    public interface EndpointsAsyncResponse {
        void processFinish(String result);
    }
}
