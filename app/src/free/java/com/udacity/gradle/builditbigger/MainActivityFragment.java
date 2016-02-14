package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.jokeactivity.JokeActivity;
import com.udacity.gradle.builditibigger.backend.myApi.MyApi;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.EndpointsAsyncResponse {

    private ProgressBar mLoadingBar;
    private MyApi mApiService;
    private EndpointsAsyncTask mEndpointsAsyncTask = null;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mAdView = (AdView) root.findViewById(R.id.adView);
        mLoadingBar = (ProgressBar) root.findViewById(R.id.loading);
        Button tellJokeButton = (Button) root.findViewById(R.id.tell_joke_button);

        tellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    tellJoke();
                }
            }
        });

        mApiService = new MyApi
                .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl(MainActivity.ROOT_URL)
                .build();

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getActivity().getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewAds();
                tellJoke();
            }
        });

        requestNewAds();

        return root;
    }

    /**
     * Creates an ad request. Check logcat output for the hashed device ID to
     * get test ads on a physical device. e.g.
     * "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
     */
    private void requestNewAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        mInterstitialAd.loadAd(adRequest);
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
