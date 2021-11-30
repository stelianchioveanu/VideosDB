package entertainment;

import common.Action;
import common.Constants;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.HashMap;

import static common.Output.outputSerialCommand;

public final class Serial extends Video {
    private final int numberSeasons;
    private final ArrayList<Season> seasons;
    private int totalDuration;

    public Serial(final SerialInputData serialInputData) {
        super(serialInputData.getYear(), serialInputData.getTitle(),
                serialInputData.getGenres(), serialInputData.getCast());
        this.numberSeasons = serialInputData.getNumberSeason();
        this.seasons = serialInputData.getSeasons();
        this.totalDuration = 0;
        for (Season i : serialInputData.getSeasons()) {
            this.totalDuration += i.getDuration();
        }
    }

    /**
     * com
     */
    public static ArrayList<Serial> makeSerialArray(final Action action, final HashMap<String,
            Serial> serialHashMap) {
        ArrayList<Serial> sortedArray = new ArrayList<>();
        for (Serial serial : serialHashMap.values()) {
            var aux = switch (action.getCriteria()) {
                case Constants.MOST_VIEWED -> serial.numberViews;
                case Constants.LONGEST -> serial.totalDuration;
                case Constants.FAVORITE -> serial.numberFavorite;
                case Constants.RATINGS -> serial.getRating();
                default -> Constants.DEFAULT_CASE_INT;
            };
            if (aux != 0
                    && (String.valueOf(serial.year).equals(action.getFilters().get(0).get(0))
                    || action.getFilters().get(0).get(0) == null)) {
                if (action.getFilters().get(1).get(0) == null) {
                    sortedArray.add(serial);
                } else {
                    for (String genre : serial.genres) {
                        if (genre.equals(action.getFilters().get(1).get(0))) {
                            sortedArray.add(serial);
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
    public static String mostViewedSerial(final Action action, final HashMap<String,
            Serial> serialHashMap) {
        ArrayList<Serial> sortedArray = makeSerialArray(action, serialHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.numberViews - o2.numberViews == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.numberViews - o2.numberViews;
        });
        return outputSerialCommand(sortedArray, action);
    }

    /**
     * com
     */
    public static String longestSerial(final Action action, final HashMap<String,
            Serial> serialHashMap) {
        ArrayList<Serial> sortedArray = makeSerialArray(action, serialHashMap);
        sortedArray.sort((o1, o2) -> {

            if (o1.totalDuration - o2.totalDuration == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.totalDuration - o2.totalDuration;
        });
        return outputSerialCommand(sortedArray, action);
    }

    /**
     * com
     */
    public static String favoriteSerial(final Action action, final HashMap<String,
            Serial> serialHashMap) {
        ArrayList<Serial> sortedArray = makeSerialArray(action, serialHashMap);
        sortedArray.sort((o1, o2) -> {

            if (o1.numberFavorite - o2.numberFavorite == 0) {
                return o1.title.compareTo(o2.title);
            }
            return o1.numberFavorite - o2.numberFavorite;
        });
        return outputSerialCommand(sortedArray, action);
    }

    /**
     * com
     */
    public static String ratingSerial(final Action action, final HashMap<String,
            Serial> serialHashMap) {
        ArrayList<Serial> sortedArray = makeSerialArray(action, serialHashMap);
        sortedArray.sort((o1, o2) -> {
            if (Double.compare(o1.getRating(), o2.getRating()) == 0) {
                return o1.title.compareTo(o2.title);
            }
            return Double.compare(o1.getRating(), o2.getRating());
        });
        return outputSerialCommand(sortedArray, action);
    }

    @Override
    public double getRating() {
        double rating = 0;
        for (Season i : this.seasons) {
            if (i.getRatings().size() != 0) {
                double r = 0;
                for (double j : i.getRatings()) {
                    r += j;
                }
                r /= i.getRatings().size();
                rating += r;
            }
        }
        if (rating == 0) {
            return 0;
        }
        rating /= this.numberSeasons;
        return rating;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}

