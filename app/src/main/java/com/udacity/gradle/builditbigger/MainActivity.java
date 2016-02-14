package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.gradle.builditbigger.jokeactivity.JokeActivity;


public class MainActivity extends AppCompatActivity implements EndpointsAsyncTask.EndpointsAsyncResponse {

    private EndpointsAsyncTask mEndpointsAsyncTask;
    private ProgressBar mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingBar = (ProgressBar) findViewById(R.id.loading);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endAsyncTask();
    }

    public void tellJoke(View view) {
        endAsyncTask();
        mLoadingBar.setVisibility(View.VISIBLE);
        mEndpointsAsyncTask = new EndpointsAsyncTask(this);
        mEndpointsAsyncTask.execute(this);
    }

    @Override
    public void processFinish(String result) {
        mLoadingBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, JokeActivity.class);
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
