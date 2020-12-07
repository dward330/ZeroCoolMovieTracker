package derrick.ward.movietracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MovieDetailsFragment extends Fragment {
    public static final String ARG_MOVIE_TITLE = "movieTitle";
    public static final String ARG_MOVIE_YEAR = "movieYear";
    public static final String ARG_MOVIE_DIRECTOR = "movieDirector";
    public static final String ARG_MOVIE_RATING = "movieRating";
    public static final String ARG_MOVIE_DESCRIPTION = "movieDescription";
    public static final String ARG_MOVIE_IMAGE_ID = "movieImageId";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View movieDetailsView = inflater.inflate(R.layout.movie_details, container, false);
        ImageView moviePoster = movieDetailsView.findViewById(R.id.detailViewMoviePoster);
        TextView movieTitle = movieDetailsView.findViewById(R.id.detailViewMovieTitle);
        TextView movieYear = movieDetailsView.findViewById(R.id.detailViewMovieYear);
        TextView movieDirector = movieDetailsView.findViewById(R.id.detailViewMovieDirector);
        RatingBar movieRating = movieDetailsView.findViewById(R.id.detailViewMovieRating);
        TextView movieDescription = movieDetailsView.findViewById(R.id.detailViewMovieDescription);

        Bundle movieDetailsArguments = getArguments();
        moviePoster.setImageResource(movieDetailsArguments.getInt(ARG_MOVIE_IMAGE_ID));
        movieTitle.setText(movieDetailsArguments.getString(ARG_MOVIE_TITLE));
        movieYear.setText(movieDetailsArguments.getString(ARG_MOVIE_YEAR));
        movieDirector.setText(movieDetailsArguments.getString(ARG_MOVIE_DIRECTOR));
        movieRating.setRating((float)movieDetailsArguments.getDouble(ARG_MOVIE_RATING));
        movieDescription.setText(movieDetailsArguments.getString(ARG_MOVIE_DESCRIPTION));

        return movieDetailsView;
    }
}
