package commands;

import entertainment.Genre;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utils.Utils.stringToGenre;

public class Recommendation {

    public Recommendation() {
    }

    /**
     * Search for first unseen video
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param user          User
     * @return Message
     */
    public String standard(final HashMap<String, Movie> movieHashMap,
                           final HashMap<String, Serial> serialHashMap,
                           final User user) {
        for (String key : movieHashMap.keySet()) {
            if (!user.getHistory().containsKey(key)) {
                return "StandardRecommendation result: " + key;
            }
        }
        for (String key : serialHashMap.keySet()) {
            if (!user.getHistory().containsKey(key)) {
                return "StandardRecommendation result: " + key;
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    /**
     * Search for best rated unseen video
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param user          User
     * @return Message
     */
    public String bestUnseen(final HashMap<String, Movie> movieHashMap,
                             final HashMap<String, Serial> serialHashMap,
                             final User user) {
        String title = "";
        double bestRating = -1;
        for (String key : movieHashMap.keySet()) {
            if (!user.getHistory().containsKey(key)
                    && (Double.compare(bestRating, movieHashMap.get(key).getRating()) < 0)) {
                title = key;
                bestRating = movieHashMap.get(key).getRating();
            }
        }
        for (String key : serialHashMap.keySet()) {
            if (!user.getHistory().containsKey(key)
                    && (Double.compare(bestRating, serialHashMap.get(key).getRating()) < 0)) {
                title = key;
                bestRating = serialHashMap.get(key).getRating();
            }
        }
        if (title.equals("")) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }
        return "BestRatedUnseenRecommendation result: " + title;
    }

    /**
     * Search for the first unseen video of the most popular genre
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param genreHashMap  HashMap with genres
     * @param user          User
     * @return Message
     */
    public String popularPremium(final HashMap<String, Movie> movieHashMap,
                                 final HashMap<String, Serial> serialHashMap,
                                 final HashMap<Genre, Integer> genreHashMap,
                                 final User user) {
        if (user.getSubscriptionType().equals("BASIC")) {
            return "PopularRecommendation cannot be applied!";
        }
        ArrayList<Map.Entry<Genre, Integer>> genreEntrySetSorted
                = new ArrayList<>(genreHashMap.entrySet());
        genreEntrySetSorted.sort((o1, o2) -> o2.getValue() - o1.getValue());
        for (var entry : genreEntrySetSorted) {
            for (String key : movieHashMap.keySet()) {
                if (!user.getHistory().containsKey(key)) {
                    for (var genre : movieHashMap.get(key).getGenres()) {
                        if (stringToGenre(genre).equals(entry.getKey())) {
                            return "PopularRecommendation result: "
                                    + movieHashMap.get(key).getTitle();
                        }
                    }
                }
            }
            for (String key : serialHashMap.keySet()) {
                if (!user.getHistory().containsKey(key)) {
                    for (var genre : serialHashMap.get(key).getGenres()) {
                        if (stringToGenre(genre).equals(entry.getKey())) {
                            return "PopularRecommendation result: "
                                    + serialHashMap.get(key).getTitle();
                        }
                    }
                }
            }
        }
        return "PopularRecommendation cannot be applied!";
    }

    /**
     * Search for unseen videos of the genre from input
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param genre         Genre
     * @param user          User
     * @return Message
     */
    public String searchPremium(final HashMap<String, Movie> movieHashMap,
                                final HashMap<String, Serial> serialHashMap,
                                final String genre, final User user) {
        if (user.getSubscriptionType().equals("BASIC")) {
            return "SearchRecommendation cannot be applied!";
        }
        ArrayList<Video> sortedArrayVideo = new ArrayList<>();
        for (var movie : movieHashMap.values()) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                for (var genres : movie.getGenres()) {
                    if (genres.equals(genre)) {
                        sortedArrayVideo.add(movie);
                    }
                }
            }
        }
        for (var serial : serialHashMap.values()) {
            if (!user.getHistory().containsKey(serial.getTitle())) {
                for (var genres : serial.getGenres()) {
                    if (genres.equals(genre)) {
                        sortedArrayVideo.add(serial);
                    }
                }
            }
        }
        sortedArrayVideo.sort((o1, o2) -> {
            if (Double.compare(o1.getRating(), o2.getRating()) == 0) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
            return Double.compare(o1.getRating(), o2.getRating());
        });
        StringBuilder outputBuilder = new StringBuilder();
        for (var video : sortedArrayVideo) {
            outputBuilder.append(video.getTitle());
            if (!sortedArrayVideo.get(sortedArrayVideo.size() - 1).equals(video)) {
                outputBuilder.append(", ");
            }
        }
        if (sortedArrayVideo.size() != 0) {
            return "SearchRecommendation result: [" + outputBuilder + "]";
        }
        return "SearchRecommendation cannot be applied!";
    }

    /**
     * Search for the first unseen video with the largest number of favorites.
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param user          User
     * @return Message
     */
    public String favoritePremium(final HashMap<String, Movie> movieHashMap,
                                  final HashMap<String, Serial> serialHashMap,
                                  final User user) {
        if (user.getSubscriptionType().equals("BASIC")) {
            return "FavoriteRecommendation cannot be applied!";
        }
        int numberFavoriteMax = 0;
        String title = "";
        for (var movie : movieHashMap.values()) {
            if (!user.getHistory().containsKey(movie.getTitle())
                    && (movie.getNumberFavorite() > numberFavoriteMax)) {
                numberFavoriteMax = movie.getNumberFavorite();
                title = movie.getTitle();
            }
        }
        for (var serial : serialHashMap.values()) {
            if (!user.getHistory().containsKey(serial.getTitle())
                    && (serial.getNumberFavorite() > numberFavoriteMax)) {
                numberFavoriteMax = serial.getNumberFavorite();
                title = serial.getTitle();
            }
        }
        if (numberFavoriteMax == 0) {
            return "FavoriteRecommendation cannot be applied!";
        }
        return "FavoriteRecommendation result: " + title;
    }
}
