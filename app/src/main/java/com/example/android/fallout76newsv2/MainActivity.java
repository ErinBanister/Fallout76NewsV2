package com.example.android.fallout76newsv2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final int LOADER_ID = 1;
    private static final String GUARDIAN_URL = "https://content.guardianapis.com/search";
    private NewsAdapter mAdapter;
    private TextView emptyState;
    private View progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsListView = findViewById(R.id.list);
        RelativeLayout main = findViewById(R.id.main);
        progress = main.findViewById(R.id.progress);
        emptyState = main.findViewById(R.id.empty_view);
        progress = main.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        newsListView.setEmptyView(emptyState);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            progress.setVisibility(View.GONE);
            emptyState.setText(R.string.noInternet);
        }

        LoaderManager lManager = null;
        lManager = getLoaderManager();
        lManager.initLoader(LOADER_ID, null, this);
        progress.setVisibility(View.GONE);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //get news item, open website with story
                News newsItem = (News) mAdapter.getItem(position);
                Uri newsItemURI = Uri.parse(newsItem.getUrl());
                Intent newsLink = new Intent(Intent.ACTION_VIEW, newsItemURI);
                startActivity(newsLink);

            }
        });

    }

    @Override
    // This method initialize the contents of the Activity's options main.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settingsbutton) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.orderDefault));
        String section = sharedPrefs.getString(getString(R.string.settings_section_key), getString(R.string.gamesDefault));


        Uri baseUri = null;
        baseUri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.sectionSearch), section);
        uriBuilder.appendQueryParameter(getString(R.string.orderBySearch), orderBy);
        uriBuilder.appendQueryParameter(getString(R.string.showTagsSearch), getString(R.string.tagNames));
        uriBuilder.appendQueryParameter(getString(R.string.query), getString(R.string.search_q));
        uriBuilder.appendQueryParameter(getString(R.string.apiKey), getString(R.string.apiValue));
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        progress.setVisibility(View.GONE);
        if (mAdapter != null) {
            mAdapter.clear();
            progress.setVisibility(View.GONE);
            emptyState.setText(R.string.noNews);
        }

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            mAdapter.clear();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {


    }

}
