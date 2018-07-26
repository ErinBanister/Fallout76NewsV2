package com.example.android.fallout76newsv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter {
    private Context mContext;

    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            try {
                listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

            } catch (InflateException e) {
                e.printStackTrace();
            }
        }

        final News currentNews = (News) getItem(position);

        //get IDs for TextViews
        TextView title = listItemView.findViewById(R.id.title);
        TextView author = listItemView.findViewById(R.id.author);
        TextView date = listItemView.findViewById(R.id.date);
        TextView section = listItemView.findViewById(R.id.section);

        //set Title
        title.setText(currentNews.getTitle());

        //set author
        author.setText(currentNews.getAuthor());

        //set section
        section.setText(currentNews.getSection());

        //set date

        String shortDate = currentNews.getDate().substring(0, 10);
        date.setText(shortDate);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = currentNews.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URL));
                mContext.startActivity(i);
            }
        });

        return listItemView;

    }
}
