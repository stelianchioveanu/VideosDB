package commands;

import common.Action;
import entertainment.Movie;
import entertainment.Serial;
import user.User;

import java.util.HashMap;

public final class Command {

    public Command() {
    }

    /**
     * Increases the number of views of a video from the user's history
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param action        Action
     * @param user          User
     * @return Message
     */
    public String view(final Action action, final HashMap<String, Movie> movieHashMap,
                       final HashMap<String, Serial> serialHashMap,
                       final User user) {
        user.getHistory().merge(action.getTitle(), 1, Integer::sum);
        if (movieHashMap.get(action.getTitle()) != null) {
            movieHashMap.get(action.getTitle()).setNumberViews(
                    movieHashMap.get(action.getTitle()).getNumberViews() + 1);
        } else {
            serialHashMap.get(action.getTitle()).setNumberViews(
                    serialHashMap.get(action.getTitle()).getNumberViews() + 1);
        }
        return "success -> "
                + action.getTitle()
                + " was viewed with total views of "
                + user.getHistory().get(action.getTitle());
    }

    /**
     * Add a video to the user's favorite list
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param action        Action
     * @param user          User
     * @return Message
     */
    public String favorite(final Action action, final HashMap<String, Movie> movieHashMap,
                           final HashMap<String, Serial> serialHashMap, final User user) {
        if (user.getHistory().get(action.getTitle()) != null) {
            for (String favoriteMovie : user.getFavoriteVideos()) {
                if (favoriteMovie.equals(action.getTitle())) {
                    return "error -> " + action.getTitle() + " is already in favourite list";
                }
            }
            user.getFavoriteVideos().add(action.getTitle());
            if (movieHashMap.get(action.getTitle()) != null) {
                movieHashMap.get(action.getTitle()).setNumberFavorite(
                        movieHashMap.get(action.getTitle()).getNumberFavorite() + 1);
            } else {
                serialHashMap.get(action.getTitle()).setNumberFavorite(
                        serialHashMap.get(action.getTitle()).getNumberFavorite() + 1);
            }
            return "success -> " + action.getTitle() + " was added as favourite";
        }
        return "error -> " + action.getTitle() + " is not seen";
    }

    /**
     * Rate a movie/serial
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param action        Action
     * @param user          User
     * @return Message
     */
    public String rating(final Action action, final HashMap<String, Movie> movieHashMap,
                         final HashMap<String, Serial> serialHashMap, final User user) {
        if (action.getSeasonNumber() == 0) {
            return ratingMovie(action.getTitle(), movieHashMap, action.getGrade(), user);
        }
        return ratingSerial(action.getTitle(), serialHashMap,
                action.getSeasonNumber(), action.getGrade(), user);
    }

    /**
     * Rate a movie
     *
     * @param movieHashMap HashMap with movies
     * @param user         User
     * @param grade        User's Grade
     * @param title        Movie's title
     * @return Message
     */
    private static String ratingMovie(final String title,
                                      final HashMap<String, Movie> movieHashMap,
                                      final double grade, final User user) {
        if (user.getHistory().get(title) != null) {
            if (user.getRatedMovies().get(title) != null) {
                return "error -> " + title + " has been already rated";
            }
            user.getRatedMovies().put(title, movieHashMap.get(title));
            movieHashMap.get(title).setNumberRatings(movieHashMap.get(title).getNumberRatings()
                    + 1);
            movieHashMap.get(title).setRating((movieHashMap.get(title).getRating()
                    * (movieHashMap.get(title).getNumberRatings() - 1) + grade)
                    / movieHashMap.get(title).getNumberRatings());
            return "success -> " + title + " was rated with " + grade + " by " + user.getUsername();
        }
        return "error -> " + title + " is not seen";
    }

    /**
     * Rate a serial
     *
     * @param seasonNumber  Season's number
     * @param serialHashMap HashMap with serials
     * @param user          User
     * @param grade         User's Grade
     * @param title         Movie's title
     * @return Message
     */
    private static String ratingSerial(final String title,
                                       final HashMap<String, Serial> serialHashMap,
                                       final int seasonNumber,
                                       final double grade,
                                       final User user) {
        if (user.getHistory().get(title) != null) {
            if (user.getRatedSeasons().get(serialHashMap.get(title).getSeasons().get(seasonNumber
                    - 1)) != null) {
                return "error -> " + title + " has been already rated";
            }
            user.getRatedSeasons().put(serialHashMap.get(title).getSeasons().get(seasonNumber
                    - 1), 1);
            serialHashMap.get(title).getSeasons().get(seasonNumber - 1).getRatings().add(grade);
            return "success -> "
                    + title
                    + " was rated with "
                    + grade
                    + " by " + user.getUsername();
        }
        return "error -> " + title + " is not seen";

    }
}
