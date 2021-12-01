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
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteMovies;
    private final HashMap<String, Movie> ratedMovies;
    private final HashMap<Season, Integer> ratedSeasons;

    public User(final UserInputData userInputData) {
        this.username = userInputData.getUsername();
        this.subscriptionType = userInputData.getSubscriptionType();
        this.history = userInputData.getHistory();
        this.favoriteMovies = userInputData.getFavoriteMovies();
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

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public HashMap<String, Movie> getRatedMovies() {
        return ratedMovies;
    }

    public HashMap<Season, Integer> getRatedSeasons() {
        return ratedSeasons;
    }
}
