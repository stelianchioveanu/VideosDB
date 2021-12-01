package entertainment;

import java.util.ArrayList;

/**
 * Information about a video
 */
public abstract class Video {
    protected int year;
    protected String title;
    protected ArrayList<String> genres;
    protected int numberViews;
    protected int numberFavorite;
    protected ArrayList<String> cast;
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
     * com
     */
    public abstract double getRating();

    /**
     * com
     */
    public int getDuration() {
        return duration;
    }
}
