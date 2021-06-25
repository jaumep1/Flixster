package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import okhttp3.Headers;

@Parcel // annotation indicates class is Parcelable
public class Movie {
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    int id;
    String key;
    int votes;

    public static final String TAG = "Movie";
    public static final String VIDEOS = "https://api.themoviedb.org/3/movie/%d/videos?api_key=%s";




    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public Movie(JSONObject jsonObject, String apiKey) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        votes = jsonObject.getInt("vote_count");

        //Get video key for displaying trailer
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS, id, apiKey),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            Log.i(TAG, "Results: " + results.toString());

                            //Make sure link is a YouTube link
                            int index = 0;
                            while (!results.getJSONObject(index)
                                    .getString("site").equals("YouTube")) {
                                index++;
                            }
                            key = results.getJSONObject(index).getString("key");
                            Log.i(TAG, "Video Key: " + key);
                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                        }
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d("Movie", "onFailure");
                    }
                });

    }

    public int getVotes() { return votes; }

    public String getKey() {
        return key;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
