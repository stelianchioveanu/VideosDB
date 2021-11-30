package entertainment;

import common.Action;
import common.Constants;
import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.HashMap;

import static common.Output.outputMovieCommand;

/**
 * Information about a movie
 */
public final class Movie extends Video {
    private final int duration;
    private int numberRatings;
    private double rating;

    public Movie(final MovieInputData movieInputData) {
        super(movieInputData.getYear(), movieInputData.getTitle(),
                movieInputData.getGenres(), movieInputData.getCast());
        this.duration = movieInputData.getDuration();
        this.numberRatings = 0;
        this.rating = 0;
    }

    /**
     * com
     */
    public static ArrayList<Movie> makeMovieArray(final Action action,
                                                  final HashMap<String, Movie> movieHashMap) {
        ArrayList<Movie> sortedArray = new ArrayList<>();
        for (Movie movie : movieHashMap.values()) {
            var aux = switch (action.getCriteria()) {
                case Constants.MOST_VIEWED -> movie.numberViews;
                case Constants.LONGEST -> movie.duration;
                case Constants.FAVORITE -> movie.numberFavorite;
                case Constants.RATINGS -> movie.rating;
                default -> Constants.DEFAULT_CASE_INT;
            };
            if (aux != 0
                    && (String.valueOf(movie.year).equals(action.getFilters().get(0).get(0))
                    || action.getFilters().get(0).get(0) == null)) {
                if (action.getFilters().get(1).get(0) == null) {
                    sortedArray.add(movie);
                } else {
                    for (String genre : movie.genres) {
                        if (genre.equals(action.getFilters().get(1).get(0))) {
                            sortedArray.add(movie);
                        }
                    }
                }
            }
        }
        return sortedArray;
    }

    /**
     * com
     */
    public static String mostViewedMovies(final Action action,
                                          final HashMap<String, Movie> movieHashMap) {
        ArrayList<Movie> sortedArray = makeMovieArray(action, movieHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.numberViews - o2.numberViews == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.numberViews - o2.numberViews;
        });
        return outputMovieCommand(sortedArray, action);
    }

    /**
     * com
     */
    public static String ratingMovies(final Action action,
                                      final HashMap<String, Movie> movieHashMap) {
        ArrayList<Movie> sortedArray = makeMovieArray(action, movieHashMap);
        sortedArray.sort((o1, o2) -> {
            if (Double.compare(o1.rating, o2.rating) == 0) {
                return o1.title.compareTo(o2.title);
            }
            return Double.compare(o1.rating, o2.rating);
        });
        return outputMovieCommand(sortedArray, action);
    }

    /**
     * com
     */
    public static String favoriteMovie(final Action action,
                                       final HashMap<String, Movie> movieHashMap) {
        ArrayList<Movie> sortedArray = makeMovieArray(action, movieHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.numberFavorite - o2.numberFavorite == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.numberFavorite - o2.numberFavorite;
        });
        return outputMovieCommand(sortedArray, action);
    }

    /**
     * com
     */
    public static String longestMovie(final Action action,
                                      final HashMap<String, Movie> movieHashMap) {
        ArrayList<Movie> sortedArray = makeMovieArray(action, movieHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.duration - o2.duration == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.duration - o2.duration;
        });
        return outputMovieCommand(sortedArray, action);
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
