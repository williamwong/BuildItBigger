package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.jokeactivity.JokeActivity;
import com.udacity.gradle.builditibigger.backend.myApi.MyApi;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.EndpointsAsyncResponse {

    private ProgressBar mLoadingBar;
    private EndpointsAsyncTask mEndpointsAsyncTask = null;
    private MyApi mApiService;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mLoadingBar = (ProgressBar) root.findViewById(R.id.loading);
        Button tellJokeButton = (Button) root.findViewById(R.id.tell_joke_button);

        tellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tellJoke();
            }
        });

        mApiService = new MyApi
                .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl(MainActivity.ROOT_URL)
                .build();

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        endAsyncTask();
    }

    public void tellJoke() {
        endAsyncTask();
        mLoadingBar.setVisibility(View.VISIBLE);
        mEndpointsAsyncTask = new EndpointsAsyncTask(this, mApiService);
        mEndpointsAsyncTask.execute();
    }

    @Override
    public void processFinish(String result) {
        mLoadingBar.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), JokeActivity.class);
        intent.putExtra(JokeActivity.JOKE_KEY, result);
        startActivity(intent);
    }

    private void endAsyncTask() {
        if (mEndpointsAsyncTask != null) {
            mEndpointsAsyncTask.onDelegateDestroyed();
            mEndpointsAsyncTask = null;
        }
    }
}
