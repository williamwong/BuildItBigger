package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.TextUtils;

import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 * <a href="http://marksunghunpark.blogspot.com/2015/05/how-to-test-asynctask-in-android.html">How to Test AsyncTask in Android</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private CountDownLatch mSignal;
    private String mResult;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        mSignal = new CountDownLatch(1);
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
        });
        task.execute();
        mSignal.await();

        assertFalse(TextUtils.isEmpty(mResult));
        assertEquals(mResult, "What do you call an Amish guy with his hand in a horse\'s mouth? A mechanic.");
    }
}