package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.udacity.gradle.builditibigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * AsyncTask to get joke from API
 */
class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

    private MyApi mApiService = null;

    private EndpointsAsyncResponse mDelegate = null;

    public EndpointsAsyncTask(EndpointsAsyncResponse delegate, MyApi apiService) {
        mDelegate = delegate;
        mApiService = apiService;
    }

    @Override
    protected final String doInBackground(Void... params) {
        try {
            return mApiService.joke().execute().getData();
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
