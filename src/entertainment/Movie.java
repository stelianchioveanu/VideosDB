package entertainment;

import fileio.MovieInputData;

/**
 * Information about a movie
 */
public final class Movie extends Video {
    private int numberRatings;
    private double rating;

    public Movie(final MovieInputData movieInputData) {
        super(movieInputData.getYear(), movieInputData.getTitle(),
                movieInputData.getGenres(), movieInputData.getCast());
        this.duration = movieInputData.getDuration();
        this.numberRatings = 0;
        this.rating = 0;
    }

    public int getDuration() {
        return duration;
    }

    public int getNumberRatings() {
        return numberRatings;
    }

    @Override
    public double getRating() {
        return this.rating;
    }

    public void setNumberRatings(final int numberRatings) {
        this.numberRatings = numberRatings;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }
}
