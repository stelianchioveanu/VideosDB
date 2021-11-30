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

    public Video(final int year, final String title,
                 final ArrayList<String> genres,
                 final ArrayList<String> cast) {
        this.cast = cast;
        this.genres = genres;
        this.year = year;
        this.title = title;
        this.numberViews = 0;
        this.numberFavorite = 0;
    }

    public int getNumberFavorite() {
        return numberFavorite;
    }

    public void setNumberFavorite(final int numberFavorite) {
        this.numberFavorite = numberFavorite;
    }

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public int getNumberViews() {
        return numberViews;
    }

    public void setNumberViews(final int numberViews) {
        this.numberViews = numberViews;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    /**
     * com
     */
    public abstract double getRating();
}
