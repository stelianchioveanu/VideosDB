package actor;

import entertainment.Movie;
import entertainment.Serial;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public final class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final ActorInputData actorInputData) {
        this.name = actorInputData.getName();
        this.careerDescription = actorInputData.getCareerDescription();
        this.filmography = actorInputData.getFilmography();
        this.awards = actorInputData.getAwards();
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    /**
     * com
     */
    public double getActorRating(final HashMap<String, Movie> movieHashMap,
                                 final HashMap<String, Serial> serialHashMap) {
        double rating = 0;
        int numberRatings = 0;
        for (String title : this.filmography) {
            if (movieHashMap.get(title) != null
                    && movieHashMap.get(title).getRating() != 0) {
                rating += movieHashMap.get(title).getRating();
                numberRatings++;
            }
            if (serialHashMap.get(title) != null
                    && serialHashMap.get(title).getRating() != 0) {
                rating += serialHashMap.get(title).getRating();
                numberRatings++;
            }
        }
        if (numberRatings == 0) {
            return 0;
        }
        return rating / numberRatings;
    }

    /**
     * com
     */
    public int getActorNumberAwards() {
        return this.awards.values().stream().reduce(0, Integer::sum);
    }
}
