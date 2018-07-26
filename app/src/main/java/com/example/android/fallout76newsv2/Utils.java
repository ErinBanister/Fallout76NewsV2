package com.example.android.fallout76newsv2;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();

    private Utils() {
    }

    public static List<News> fetchNews(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //get news array
        return extractFeatureFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "URL Problem ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* these are counted in milliseconds */);
            urlConnection.setConnectTimeout(15000 /* these are counted in milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Only continue if the http request was 200 (accepted)
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsRawJSON) {
        // If the JSON string is empty or null, then return early.

        String title;
        String author;
        String url;
        String date;
        String section;
        if (TextUtils.isEmpty(newsRawJSON)) {
            return null;
        }

        // Create an empty ArrayList
        List<News> newsList = new ArrayList<>();
        //add items to the array
        try {

            JSONObject baseJsonResponse = new JSONObject(newsRawJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject storyList = resultsArray.getJSONObject(i);
                title = storyList.getString("webTitle");
                date = storyList.getString("webPublicationDate");
                url = storyList.getString("webUrl");
                section = storyList.getString("sectionName");
                JSONArray authorList = storyList.getJSONArray("tags");
                if (authorList.length() != 0) {
                    JSONObject artAuthor = authorList.getJSONObject(0);
                    author = artAuthor.getString("webTitle");
                } else {
                    author = "No Contributor Listed";
                }
                News news = new News(title, author, date, url, section);
                newsList.add(news);
            }

        } catch (JSONException e) {

            e.printStackTrace();

        }
        if (!newsList.isEmpty()) {
            return newsList;
        } else {
            return null;
        }
    }

}