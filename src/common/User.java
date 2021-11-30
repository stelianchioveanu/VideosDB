package common;

import entertainment.Genre;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Season;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static common.Output.outputUserCommand;

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

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    /**
     * com
     */
    public String view(final Action action, final HashMap<String, Movie> movieHashMap,
                       final HashMap<String, Serial> serialHashMap) {
        this.history.merge(action.getTitle(), 1, Integer::sum);
        if (movieHashMap.get(action.getTitle()) != null) {
            movieHashMap.get(action.getTitle()).setNumberViews(movieHashMap.get(action.getTitle()).getNumberViews() + 1);
        } else {
            serialHashMap.get(action.getTitle()).setNumberViews(serialHashMap.get(action.getTitle()).getNumberViews() + 1);
        }
        return "success -> " + action.getTitle() + " was viewed with total views of " + this.history.get(action.getTitle());
    }

    /**
     * com
     */
    public String favorite(final Action action, final HashMap<String, Movie> movieHashMap,
                           final HashMap<String, Serial> serialHashMap) {
        if (this.history.get(action.getTitle()) != null) {
            for (String favoriteMovie : this.favoriteMovies) {
                if (favoriteMovie.equals(action.getTitle())) {
                    return "error -> " + action.getTitle() + " is already in favourite list";
                }
            }
            this.favoriteMovies.add(action.getTitle());
            if (movieHashMap.get(action.getTitle()) != null) {
                movieHashMap.get(action.getTitle()).setNumberFavorite(movieHashMap.get(action.getTitle()).getNumberFavorite() + 1);
            } else {
                serialHashMap.get(action.getTitle()).setNumberFavorite(serialHashMap.get(action.getTitle()).getNumberFavorite() + 1);
            }
            return "success -> " + action.getTitle() + " was added as favourite";
        }
        return "error -> " + action.getTitle() + " is not seen";
    }

    /**
     * com
     */
    public String rating(final Action action, final HashMap<String, Movie> movieHashMap,
                         final HashMap<String, Serial> serialHashMap) {
        if (action.getSeasonNumber() == 0) {
            return ratingMovie(action.getTitle(), movieHashMap, action.getGrade());
        }
        return ratingSerial(action.getTitle(), serialHashMap,
                action.getSeasonNumber(), action.getGrade());
    }

    /**
     * com
     */
    public String ratingMovie(final String title, final HashMap<String, Movie> movieHashMap,
                              final double grade) {
        if (this.history.get(title) != null) {
            if (this.ratedMovies.get(title) != null) {
                return "error -> " + title + " has been already rated";
            }
            this.ratedMovies.put(title, movieHashMap.get(title));
            movieHashMap.get(title).setNumberRatings(movieHashMap.get(title).getNumberRatings() + 1);
            movieHashMap.get(title).setRating((movieHashMap.get(title).getRating() * (movieHashMap.get(title).getNumberRatings() - 1) + grade) / movieHashMap.get(title).getNumberRatings());
            return "success -> " + title + " was rated with " + grade + " by " + this.username;
        }
        return "error -> " + title + " is not seen";
    }

    /**
     * com
     */
    public String ratingSerial(final String title, final HashMap<String, Serial> serialHashMap,
                               final int seasonNumber, final double grade) {
        if (this.history.get(title) != null) {
            if (this.ratedSeasons.get(serialHashMap.get(title).getSeasons().get(seasonNumber - 1))
                    != null) {
                return "error -> " + title + " has been already rated";
            }
            this.ratedSeasons.put(serialHashMap.get(title).getSeasons().get(seasonNumber - 1), 1);
            serialHashMap.get(title).getSeasons().get(seasonNumber - 1).getRatings().add(grade);
            return "success -> " + title + " was rated with " + grade + " by " + this.username;
        }
        return "error -> " + title + " is not seen";
    }

    /**
     * com
     */
    public static String numberOfRatings(final Action action,
                                         final HashMap<String, User> userHashMap) {
        ArrayList<User> sortedArray = new ArrayList<>();
        for (User user : userHashMap.values()) {
            if (user.ratedMovies.size() + user.ratedSeasons.size() != 0) {
                sortedArray.add(user);
            }
        }
        sortedArray.sort((o1, o2) -> {
            if (o1.ratedSeasons.size() + o1.ratedMovies.size()
                    - o2.ratedSeasons.size() - o2.ratedMovies.size() == 0) {
                return o1.username.compareTo(o2.username);
            }
            return (o1.ratedSeasons.size() + o1.ratedMovies.size()
                    - o2.ratedSeasons.size() - o2.ratedMovies.size());
        });
        return outputUserCommand(sortedArray, action);
    }

    public String standard(final HashMap<String, Movie> movieHashMap,
                           final HashMap<String, Serial> serialHashMap) {
        for (String key : movieHashMap.keySet()) {
            if (!this.getHistory().containsKey(key)) {
                return "StandardRecommendation result: " + key;
            }
        }
        for (String key : movieHashMap.keySet()) {
            if (!this.getHistory().containsKey(key)) {
                return "StandardRecommendation result: " + key;
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    public String bestUnseen(final HashMap<String, Movie> movieHashMap,
                             final HashMap<String, Serial> serialHashMap) {
        String title = "";
        double bestRating = -1;
        for (String key : movieHashMap.keySet()) {
            if (!this.getHistory().containsKey(key)
                    && (Double.compare(bestRating, movieHashMap.get(key).getRating()) < 0)) {
                title = key;
                bestRating = movieHashMap.get(key).getRating();
            }
        }
        for (String key : serialHashMap.keySet()) {
            if (!this.getHistory().containsKey(key)
                    && (Double.compare(bestRating, serialHashMap.get(key).getRating()) < 0)) {
                title = key;
                bestRating = serialHashMap.get(key).getRating();
            }
        }
        if(title.equals("")){
            return "BestRatedUnseenRecommendation cannot be applied!";
        }
        return "BestRatedUnseenRecommendation result: " + title;
    }

    public String popularPremium(final HashMap<String, Movie> movieHashMap,
                                 final HashMap<String, Serial> serialHashMap,
                                 final HashMap<Genre, Integer> genreHashMap){
        if(this.subscriptionType.equals("BASIC"))
            return "PopularRecommendation cannot be applied!";
        ArrayList<Map.Entry<Genre, Integer>> genreEntrySetSorted = new ArrayList<>(genreHashMap.entrySet());
        genreEntrySetSorted.sort((o1, o2) -> o1.getValue() - o2.getValue());

        for (var entry : genreEntrySetSorted){
            for (String key : movieHashMap.keySet()) {
                if (!this.getHistory().containsKey(key)) {
                    for (var genre : movieHashMap.get(key).getGenres()) {
                        if (genre.toUpperCase(Locale.ROOT).equals(entry.getKey().toString())) {
                            return "PopularRecommendation result: " + movieHashMap.get(key).getTitle();
                        }
                    }
                }
            }
            for (String key : serialHashMap.keySet()) {
                if (!this.getHistory().containsKey(key)) {
                    for (var genre : serialHashMap.get(key).getGenres()) {
                        if (genre.toUpperCase(Locale.ROOT).equals(entry.getKey().toString())) {
                            return "PopularRecommendation result: " + serialHashMap.get(key).getTitle();
                        }
                    }
                }
            }
        }
        return "PopularRecommendation cannot be applied!";
    }
}
