package entertainment;

import common.Action;
import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.HashMap;

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
        double aux;
        for (Movie movie : movieHashMap.values()) {
            switch (action.getCriteria()) {
                case "most_viewed" -> aux = movie.numberViews;
                case "longest" -> aux = movie.duration;
                case "favorite" -> aux = movie.numberFavorite;
                case "ratings" -> aux = movie.rating;
                default -> aux = 0;
            }
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

    /**
     * com
     */
    public static String outputMovieCommand(final ArrayList<Movie> sortedArray,
                                            final Action action) {
        String output;
        StringBuilder outputBuilder = new StringBuilder();
        if (action.getSortType().equals("asc")) {
            for (int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                outputBuilder.append(sortedArray.get(i).title);
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    outputBuilder.append(", ");
                }
            }
        } else {
            for (int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                outputBuilder.append(sortedArray.get(i).title);
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    outputBuilder.append(", ");
                }
            }
        }
        output = outputBuilder.toString();
        return "Query result: [" + output + "]";
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
