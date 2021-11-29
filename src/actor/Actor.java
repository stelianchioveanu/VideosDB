package actor;

import common.Action;
import entertainment.Movie;
import entertainment.Serial;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public double getActorRating(HashMap<String, Movie> movieHashMap,
                                 HashMap<String, Serial> serialHashMap) {
        double rating = 0;
        int nr = 0;
        for (String title : this.filmography) {
            if (movieHashMap.get(title) != null && movieHashMap.get(title).getRating() != 0) {
                rating += movieHashMap.get(title).getRating();
                nr++;
            }
            if (serialHashMap.get(title) != null && serialHashMap.get(title).getRating() != 0) {
                rating += serialHashMap.get(title).getRating();
                nr++;
            }
        }
        if (nr == 0) {
            return 0;
        }
        rating /= nr;
        return rating;
    }

    public static String averageActor(Action action, HashMap<String, Movie> movieHashMap,
                                      HashMap<String, Serial> serialHashMap,
                                      HashMap<String,Actor> actorHashMap) {
        ArrayList<Actor> sortedArray = new ArrayList<>();
        for (Actor actor : actorHashMap.values()){
            if (actor.getActorRating(movieHashMap, serialHashMap) != 0) {
                sortedArray.add(actor);
            }
        }
        sortedArray.sort((o1, o2) -> {
            if (Double.compare(o1.getActorRating(movieHashMap, serialHashMap),
                    o2.getActorRating(movieHashMap, serialHashMap)) == 0) {
                return o1.name.compareTo(o2.name);
            }
            return Double.compare(o1.getActorRating(movieHashMap, serialHashMap),
                    o2.getActorRating(movieHashMap, serialHashMap));
        });
        return outputActorCommand(sortedArray, action);
    }

    public static String outputActorCommand(ArrayList<Actor> sortedArray, Action action) {
        String output;
        if (action.getSortType().equals("asc")){
            StringBuilder outputBuilder = new StringBuilder();
            for(int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                outputBuilder.append(sortedArray.get(i).name);
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    outputBuilder.append(", ");
                }
            }
            output = outputBuilder.toString();
        } else {
            StringBuilder outputBuilder = new StringBuilder();
            for(int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                outputBuilder.append(sortedArray.get(i).name);
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    outputBuilder.append(", ");
                }
            }
            output = outputBuilder.toString();
        }
        return "Query result: [" + output + "]";
    }

    @Override
    public String toString() {
        return "Actor{"
                + "name='"
                + name
                + '\''
                + ", careerDescription='"
                + careerDescription
                + '\''
                + ", filmography="
                + filmography
                + ", awards="
                + awards
                + '}';
    }
}
