package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.TextUtils;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditibigger.backend.myApi.MyApi;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 * <a href="http://marksunghunpark.blogspot.com/2015/05/how-to-test-asynctask-in-android.html">How to Test AsyncTask in Android</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    // 10.0.2.2 is localhost's IP address in Android emulator
    private static final String ROOT_URL = "http://10.0.2.2:8080/_ah/api/";

    private CountDownLatch mSignal;
    private String mResult;
    private MyApi mApiService;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        mSignal = new CountDownLatch(1);

        // Configure API settings for local devappserver
        mApiService = new MyApi
                .Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl(ROOT_URL)
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        // Turn off compression when running against local devappserver
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                }).build();
    }

    @Override
    protected void tearDown() throws Exception {
        mSignal.countDown();
    }

    public void testEndpointsAsyncTask() throws InterruptedException {
        EndpointsAsyncTask task = new EndpointsAsyncTask(new EndpointsAsyncTask.EndpointsAsyncResponse() {
            @Override
            public void processFinish(String result) {
                mResult = result;
                mSignal.countDown();
            }
        }, mApiService);
        task.execute();
        mSignal.await();

        assertFalse(TextUtils.isEmpty(mResult));
    }
}