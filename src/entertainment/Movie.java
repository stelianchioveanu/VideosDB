package entertainment;

import common.Action;
import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.HashMap;

public class Movie extends Video{
    private final int duration;
    private int numberRatings;
    private double rating;

    public Movie(final MovieInputData movieInputData){
        super(movieInputData.getYear(), movieInputData.getTitle(), movieInputData.getGenres(), movieInputData.getCast());
        this.duration = movieInputData.getDuration();
        this.numberRatings = 0;
        this.rating = 0;
    }

    public static ArrayList<Movie> makeMovieArray(Action action, HashMap<String, Movie> movieHashMap){
        ArrayList<Movie> sortedArray = new ArrayList<>();
        double aux = 0;
        for (Movie movie : movieHashMap.values()){
            switch (action.getCriteria()) {
                case "most_viewed" -> aux = movie.numberViews;
                case "longest" -> aux = 1;
                case "favorite" -> aux = movie.numberFavorite;
                case "ratings" -> aux = movie.rating;
            }
            if (aux != 0 && (String.valueOf(movie.year).equals(action.getFilters().get(0).get(0)) || action.getFilters().get(0).get(0) == null)){
                if (action.getFilters().get(1).get(0) == null){
                    sortedArray.add(movie);
                } else {
                    for (String genre : movie.genres){
                        if (genre.equals(action.getFilters().get(1).get(0))){
                            sortedArray.add(movie);
                        }
                    }
                }
            }
        }
        return sortedArray;
    }

    public static String mostViewedMovies(Action action, HashMap<String, Movie> movieHashMap){
        ArrayList<Movie> sortedArray = makeMovieArray(action, movieHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.numberViews - o2.numberViews == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.numberViews - o2.numberViews;
        });
        return outputMovieCommand(sortedArray, action);
    }

    public static String ratingMovies(Action action, HashMap<String, Movie> movieHashMap){
        ArrayList<Movie> sortedArray = makeMovieArray(action, movieHashMap);
        //
        sortedArray.sort((o1, o2) -> Double.compare(o1.rating, o2.rating));
        return outputMovieCommand(sortedArray, action);
    }

    public static String favoriteMovie(Action action, HashMap<String, Movie> movieHashMap){
        ArrayList<Movie> sortedArray = makeMovieArray(action, movieHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.numberFavorite - o2.numberFavorite == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.numberFavorite - o2.numberFavorite;
        });
        return outputMovieCommand(sortedArray, action);
    }

    public static String longestMovie(Action action, HashMap<String, Movie> movieHashMap){
        ArrayList<Movie> sortedArray = makeMovieArray(action, movieHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.duration - o2.duration == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.duration - o2.duration;
        });
        return outputMovieCommand(sortedArray, action);
    }

    public static String outputMovieCommand(ArrayList<Movie> sortedArray, Action action){
        String output;
        if (action.getSortType().equals("asc")){
            StringBuilder outputBuilder = new StringBuilder();
            for(int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++){
                outputBuilder.append(sortedArray.get(i).title);
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1){
                    outputBuilder.append(", ");
                }
            }
            output = outputBuilder.toString();
        } else {
            StringBuilder outputBuilder = new StringBuilder();
            for(int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--){
                outputBuilder.append(sortedArray.get(i).title);
                if (i != Math.max(0, sortedArray.size() - action.getNumber())){
                    outputBuilder.append(", ");
                }
            }
            output = outputBuilder.toString();
        }
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

    public void setNumberRatings(int numberRatings) {
        this.numberRatings = numberRatings;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
