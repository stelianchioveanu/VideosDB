package common;

import entertainment.*;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class User {
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

    public String getSubscriptionType() {
        return subscriptionType;
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

    public String view(Action action, HashMap<String, Movie> movieHashMap, HashMap<String, Serial> serialHashMap) {
        this.history.merge(action.getTitle(), 1, Integer::sum);
        if (movieHashMap.get(action.getTitle()) != null) {
            movieHashMap.get(action.getTitle()).setNumberViews(movieHashMap.get(action.getTitle()).getNumberViews() + 1);
        } else {
            serialHashMap.get(action.getTitle()).setNumberViews(serialHashMap.get(action.getTitle()).getNumberViews() + 1);
        }
        return "success -> " + action.getTitle() + " was viewed with total views of " + this.history.get(action.getTitle());
    }

    public String favorite(Action action, HashMap<String, Movie> movieHashMap, HashMap<String, Serial> serialHashMap) {
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

    public String rating(Action action, HashMap<String, Movie> movieHashMap, HashMap<String, Serial> serialHashMap) {
        if (action.getSeasonNumber() == 0) {
            return ratingMovie(action.getTitle(), movieHashMap, action.getGrade());
        }
        return ratingSerial(action.getTitle(), serialHashMap, action.getSeasonNumber(), action.getGrade());
    }

    public String ratingMovie(String title, HashMap<String, Movie> movieHashMap, double grade) {
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

    public String ratingSerial(String title, HashMap<String, Serial> serialHashMap, int seasonNumber, double grade) {
        if (this.history.get(title) != null) {
            if (this.ratedSeasons.get(serialHashMap.get(title).getSeasons().get(seasonNumber - 1)) != null) {
                return "error -> " + title + " has been already rated";
            }
            this.ratedSeasons.put(serialHashMap.get(title).getSeasons().get(seasonNumber - 1), 1);
            serialHashMap.get(title).getSeasons().get(seasonNumber - 1).getRatings().add(grade);
            return "success -> " + title + " was rated with " + grade + " by " + this.username;
        }
        return "error -> " + title + " is not seen";
    }

    public static String numberOfRatings(Action action, HashMap<String, User> userHashMap) {
        ArrayList<User> sortedArray = new ArrayList<>();
        for (User user : userHashMap.values()) {
            if (user.ratedMovies.size() + user.ratedSeasons.size() != 0) {
                sortedArray.add(user);
            }
        }
        sortedArray.sort(new Comparator<>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.ratedSeasons.size() + o1.ratedMovies.size()
                        - o2.ratedSeasons.size() - o2.ratedMovies.size() == 0) {
                    return o1.username.compareTo(o2.username);
                }
                return (o1.ratedSeasons.size() + o1.ratedMovies.size()
                        - o2.ratedSeasons.size() - o2.ratedMovies.size());
            }
        });
        return outputUserCommand(sortedArray, action);
    }

    public static String outputUserCommand(ArrayList<User> sortedArray, Action action) {
        String output = "";
        if (action.getSortType().equals("asc")) {
            for (int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                output += sortedArray.get(i).username;
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    output += ", ";
                }
            }
        } else {
            for (int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                output += sortedArray.get(i).username;
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    output += ", ";
                }
            }
        }
        return "Query result: [" + output + "]";
    }
}
