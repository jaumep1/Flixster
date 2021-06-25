package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING =
            "https://api.themoviedb.org/3/movie/now_playing?api_key=%s";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // activity_main.xml -> ActivityMainBinding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        setContentView(binding.getRoot());

        movies = new ArrayList<>();

        //Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //Set the adapter on the recycler view
        binding.rvMovies.setAdapter(movieAdapter);

        //Set a Layout Manager on the recycler view
        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //Get movie JSON data
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(NOW_PLAYING, getString(R.string.theMovieDBKey)),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            Log.i(TAG, "Results: " + results.toString());
                            movies.addAll(fromJSONArray(results, getString(R.string.theMovieDBKey)));
                            movieAdapter.notifyDataSetChanged();
                            Log.i(TAG, "Movies: " + movies.size());
                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                        }
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });
    }

    //Convert JSON array of movies to ArrayList
    public static List<Movie> fromJSONArray(JSONArray movieJsonArray, String apiKey)
            throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i), apiKey));
        }
        return movies;
    }
}