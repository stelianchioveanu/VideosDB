package common;

import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;

import java.util.ArrayList;
import java.util.HashMap;

public final class ArrayBuilder {

    public ArrayBuilder() {
    }

    /**
     * Add to sorted array the videos for each case
     *
     * @param action        Action
     * @param serialHashMap HashMap with serials
     * @param movieHashMap  HashMap with movies
     * @return Array with videos
     */
    public ArrayList<Video> makeArray(final Action action,
                                      final HashMap<String, Movie> movieHashMap,
                                      final HashMap<String, Serial> serialHashMap) {
        ArrayList<Video> sortedArray = new ArrayList<>();
        ArrayList<Video> videoArray;
        if (movieHashMap != null) {
            videoArray = new ArrayList<>(movieHashMap.values());
        } else {
            videoArray = new ArrayList<>(serialHashMap.values());
        }
        for (Video video : videoArray.stream().toList()) {
            var aux = switch (action.getCriteria()) {
                case Constants.MOST_VIEWED -> video.getNumberViews();
                case Constants.LONGEST -> video.getDuration();
                case Constants.FAVORITE -> video.getNumberFavorite();
                case Constants.RATINGS -> video.getRating();
                default -> Constants.DEFAULT_CASE_INT;
            };
            if (aux != 0
                    && (String.valueOf(video.getYear()).equals(action.getFilters().get(0).get(0))
                    || action.getFilters().get(0).get(0) == null)) {
                if (action.getFilters().get(1).get(0) == null) {
                    sortedArray.add(video);
                } else {
                    for (String genre : video.getGenres()) {
                        if (genre.equals(action.getFilters().get(1).get(0))) {
                            sortedArray.add(video);
                        }
                    }
                }
            }
        }
        return sortedArray;
    }

}
