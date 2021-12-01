package entertainment;

import java.util.ArrayList;

/**
 * Information about a video
 */
public abstract class Video {
    /**
     * Year of a video
     */
    protected int year;
    /**
     * Title of a video
     */
    protected String title;
    /**
     * Genres of a video
     */
    protected ArrayList<String> genres;
    /**
     * Number of views of a video
     */
    protected int numberViews;
    /**
     * Number of favorites of a video
     */
    protected int numberFavorite;
    /**
     * Cast of a video
     */
    protected ArrayList<String> cast;
    /**
     * Duration of a video
     */
    protected int duration;

    public Video(final int year, final String title,
                 final ArrayList<String> genres,
                 final ArrayList<String> cast) {
        this.cast = cast;
        this.genres = genres;
        this.year = year;
        this.title = title;
        this.duration = 0;
        this.numberViews = 0;
        this.numberFavorite = 0;
    }

    public final int getYear() {
        return year;
    }

    public final String getTitle() {
        return title;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final int getNumberViews() {
        return numberViews;
    }

    public final int getNumberFavorite() {
        return numberFavorite;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final void setNumberViews(final int numberViews) {
        this.numberViews = numberViews;
    }

    public final void setNumberFavorite(final int numberFavorite) {
        this.numberFavorite = numberFavorite;
    }

    /**
     * Calculate the video's rating
     *
     * @return Video's rating
     */
    public abstract double getRating();

    /**
     * Calculate the video's duration
     *
     * @return Video's duration
     */
    public int getDuration() {
        return duration;
    }
}
