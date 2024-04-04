package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;

/**
 * Information about a serial
 */
public final class Serial extends Video {
    /**
     * Number of seasons of a serial
     */
    private final int numberSeasons;
    /**
     * Seasons of a serial
     */
    private final ArrayList<Season> seasons;

    public Serial(final SerialInputData serialInputData) {
        super(serialInputData.getYear(), serialInputData.getTitle(),
                serialInputData.getGenres(), serialInputData.getCast());
        this.numberSeasons = serialInputData.getNumberSeason();
        this.seasons = serialInputData.getSeasons();
        int duration = 0;
        for (Season i : serialInputData.getSeasons()) {
            duration += i.getDuration();
        }
        this.duration = duration;
    }

    @Override
    public double getRating() {
        double serialRating = 0;
        for (Season season : this.seasons) {
            if (season.getRatings().size() != 0) {
                serialRating += season.getRatings().stream().reduce(0.0, Double::sum)
                        / season.getRatings().size();
            }
        }
        return serialRating / this.numberSeasons;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}

