package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // activity_movie_details.xml -> ActivityMovieDetailsBinding
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        setContentView(binding.getRoot());

        //Unwrap intent parcel
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity",
                String.format("Showing details for %s", movie.getTitle()));

        //Bind data to layout fields
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());
        float voteAverage = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAverage / 2.0f);
        binding.tvVoteCount.setText(String.format("(%d reviews)", movie.getVotes()));

        RelativeLayout context = binding.getRoot();
        String imageURL;
        int loadImage;

        //If phone is in landscape, use backdrop; otherwise use poster
        if (context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
            loadImage = R.drawable.flicks_movie_placeholder;
        } else {
            imageURL = movie.getPosterPath();
            loadImage = R.drawable.flicks_backdrop_placeholder;
        }

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop

        //Placeholder image while poster data loads + corner rounding
        Glide.with(binding.getRoot())
                .load(imageURL)
                .placeholder(loadImage)
                .centerCrop()
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(binding.ivPoster);

        binding.ivPoster.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("MovieDetailsActivity", "Click recognized");

        //Create intent
        Intent intent = new Intent(this, MovieTrailerActivity.class);
        intent.putExtra("vidID", movie.getKey());

        //Show activity
        this.startActivity(intent);

    }
}