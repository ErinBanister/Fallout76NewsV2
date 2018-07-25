package com.example.android.fallout76news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final int LOADER_ID = 1;
    private static final String GUARDIAN_URL = "https://content.guardianapis.com/search?order-by=newest&show-tags=contributor&q=fallout%20AND%2076&api-key=aed1eafc-57ec-4c3e-be76-75a969408ea1";
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
            emptyState.setText(R.string.noNews);
        }

        LoaderManager lManager = getLoaderManager();
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
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, GUARDIAN_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        progress.setVisibility(View.GONE);
        if (mAdapter != null) {
            mAdapter.clear();
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
