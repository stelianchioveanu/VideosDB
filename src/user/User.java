package user;

import entertainment.Movie;
import entertainment.Season;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about a user
 */
public final class User {
    /**
     * Username of a user
     */
    private final String username;
    /**
     * Type of subscription of a user
     */
    private final String subscriptionType;
    /**
     * History of a user
     */
    private final Map<String, Integer> history;
    /**
     * Favorite videos of a user
     */
    private final ArrayList<String> favoriteVideos;
    /**
     * Rated movies of a user
     */
    private final HashMap<String, Movie> ratedMovies;
    /**
     * Rated seasons of a user
     */
    private final HashMap<Season, Integer> ratedSeasons;

    public User(final UserInputData userInputData) {
        this.username = userInputData.getUsername();
        this.subscriptionType = userInputData.getSubscriptionType();
        this.history = userInputData.getHistory();
        this.favoriteVideos = userInputData.getFavoriteMovies();
        this.ratedMovies = new HashMap<>();
        this.ratedSeasons = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<String> getFavoriteVideos() {
        return favoriteVideos;
    }

    public HashMap<String, Movie> getRatedMovies() {
        return ratedMovies;
    }

    public HashMap<Season, Integer> getRatedSeasons() {
        return ratedSeasons;
    }
}
