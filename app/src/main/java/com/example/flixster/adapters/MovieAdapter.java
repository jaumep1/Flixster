package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;
    ItemMovieBinding binding;


    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Inflate layout from XML and return the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie,
                parent, false);
        return new ViewHolder(movieView);
    }

    //Populate data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        //Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the View Holder
        holder.bind(movie);
    }

    //Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            binding = ItemMovieBinding.bind(this.itemView);
        }

        //Bind data into View Holder
        public void bind(Movie movie) {

            //Bindings
            binding = ItemMovieBinding.bind(this.itemView);
            binding.tvTitle.setText(movie.getTitle());
            binding.tvOverview.setText(movie.getOverview());
            float voteAverage = movie.getVoteAverage().floatValue();
            binding.rbVoteAverage.setRating(voteAverage / 2.0f);
            binding.tvVoteCount.setText(String.format("(%d reviews)", movie.getVotes()));

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
            Glide.with(context)
                    .load(imageURL)
                    .placeholder(loadImage)
                    .centerCrop()
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(binding.ivPoster);
        }

        //Load item details when clicked
        @Override
        public void onClick(View v) {

            Log.d("MovieAdapter", "Click recognized.");

            //Get position
            int position = getAdapterPosition();

            //Validate position
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = movies.get(position);

                //Create intent
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));

                //Show activity
                context.startActivity(intent);
            }
        }
    }
}
