package actor;

import common.Action;
import common.Constants;
import entertainment.Movie;
import entertainment.Serial;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static utils.Utils.stringToAwards;

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
        int nr = 0;
        for (String title : this.filmography) {
            if (movieHashMap.get(title) != null
                    && movieHashMap.get(title).getRating() != 0) {
                rating += movieHashMap.get(title).getRating();
                nr++;
            }
            if (serialHashMap.get(title) != null
                    && serialHashMap.get(title).getRating() != 0) {
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

    /**
     * com
     */
    public int getActorNumberAwards() {
        int sum = 0;
        for (Integer i : this.awards.values()) {
            sum += i;
        }
        return sum;
    }

    /**
     * com
     */
    public static String averageActor(final Action action,
                                      final HashMap<String, Movie> movieHashMap,
                                      final HashMap<String, Serial> serialHashMap,
                                      final HashMap<String, Actor> actorHashMap) {
        ArrayList<Actor> sortedArray = new ArrayList<>();
        for (Actor actor : actorHashMap.values()) {
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

    /**
     * com
     */
    public static String awardsActor(final Action action,
                                     final HashMap<String, Actor> actorHashMap) {
        ArrayList<Actor> sortedArray = new ArrayList<>();
        for (Actor actor : actorHashMap.values()) {
            boolean aux = true;
            for (String award : action.getFilters().get(Constants.AWARD_INDEX)) {
                if (actor.awards.get(stringToAwards(award)) == null) {
                    aux = false;
                    break;
                }
            }
            if (aux) {
                sortedArray.add(actor);
            }
        }
        sortedArray.sort((o1, o2) -> {
            if (o1.getActorNumberAwards() == o2.getActorNumberAwards()) {
                return o1.name.compareTo(o2.name);
            }
            return o1.getActorNumberAwards() - o2.getActorNumberAwards();
        });
        return outputActorCommand(sortedArray, action);
    }

    /**
     * com
     */
    public static String filterDescriptionActor(final Action action,
                                                final HashMap<String, Actor> actorHashMap) {
        ArrayList<Actor> sortedArray = new ArrayList<>();
        for (Actor actor : actorHashMap.values()) {
            boolean aux = true;
            for (String description : action.getFilters().get(Constants.DESCRIPTION_INDEX)) {
                if (!actor.careerDescription.contains(description)) {
                    aux = false;
                    break;
                }
            }
            if (aux) {
                sortedArray.add(actor);
            }
        }
        sortedArray.sort(Comparator.comparing(o -> o.name));
        return outputActorCommand(sortedArray, action);
    }

    /**
     * com
     */
    public static String outputActorCommand(final ArrayList<Actor> sortedArray,
                                            final Action action) {
        String output;
        StringBuilder outputBuilder = new StringBuilder();
        if (action.getSortType().equals("asc")) {
            for (int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                outputBuilder.append(sortedArray.get(i).name);
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    outputBuilder.append(", ");
                }
            }
        } else {
            for (int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                outputBuilder.append(sortedArray.get(i).name);
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    outputBuilder.append(", ");
                }
            }
        }
        output = outputBuilder.toString();
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
