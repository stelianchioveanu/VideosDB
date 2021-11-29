package entertainment;

import common.Action;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.HashMap;

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
        double aux;
        for (Serial serial : serialHashMap.values()) {
            switch (action.getCriteria()) {
                case "most_viewed" -> aux = serial.numberViews;
                case "longest" -> aux = 1;
                case "favorite" -> aux = serial.numberFavorite;
                case "ratings" -> aux = serial.getRating();
                default -> aux = 0;
            }
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

    /**
     * com
     */
    public static String outputSerialCommand(final ArrayList<Serial> sortedArray,
                                             final Action action) {
        String output;
        if (action.getSortType().equals("asc")) {
            StringBuilder outputBuilder = new StringBuilder();
            for (int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                outputBuilder.append(sortedArray.get(i).title);
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    outputBuilder.append(", ");
                }
            }
            output = outputBuilder.toString();
        } else {
            StringBuilder outputBuilder = new StringBuilder();
            for (int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                outputBuilder.append(sortedArray.get(i).title);
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    outputBuilder.append(", ");
                }
            }
            output = outputBuilder.toString();
        }
        return "Query result: [" + output + "]";
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

