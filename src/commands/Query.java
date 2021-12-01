package commands;

import actor.Actor;
import common.Action;
import common.ArrayBuilder;
import common.Constants;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Video;
import user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import static common.Output.outputVideoCommand;
import static common.Output.outputUserCommand;
import static common.Output.outputActorCommand;
import static utils.Utils.stringToAwards;

public final class Query {

    public Query() {
    }

    /**
     * Add to a list the actors with best rated videos
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param actorHashMap  HashMap with actors
     * @param action        Action
     * @return Actors with best rated videos
     */
    public String average(final Action action,
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
                return o1.getName().compareTo(o2.getName());
            }
            return Double.compare(o1.getActorRating(movieHashMap, serialHashMap),
                    o2.getActorRating(movieHashMap, serialHashMap));
        });
        return outputActorCommand(sortedArray, action);
    }

    /**
     * Add to a list the actors with all awards
     *
     * @param actorHashMap HashMap with actors
     * @param action       Action
     * @return Actors with all awards
     */
    public String awards(final Action action,
                         final HashMap<String, Actor> actorHashMap) {
        ArrayList<Actor> sortedArray = new ArrayList<>();
        for (Actor actor : actorHashMap.values()) {
            boolean aux = true;
            for (String award : action.getFilters().get(Constants.AWARD_INDEX)) {
                if (actor.getAwards().get(stringToAwards(award)) == null) {
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
                return o1.getName().compareTo(o2.getName());
            }
            return o1.getActorNumberAwards() - o2.getActorNumberAwards();
        });
        return outputActorCommand(sortedArray, action);
    }

    /**
     * Add to a list the actors with all keywords in filter description
     *
     * @param actorHashMap HashMap with actors
     * @param action       Action
     * @return Actors with all keywords in filter description
     */
    public String filterDescription(final Action action,
                                    final HashMap<String, Actor> actorHashMap) {
        ArrayList<Actor> sortedArray = new ArrayList<>();
        for (Actor actor : actorHashMap.values()) {
            int aux = 0;
            for (String description : action.getFilters().get(Constants.DESCRIPTION_INDEX)) {
                Scanner scan = new Scanner(actor.getCareerDescription());
                scan.useDelimiter("\\W+");
                while (scan.hasNext()) {
                    if (scan.next().toLowerCase().equals(description)) {
                        aux++;
                        break;
                    }
                }
                scan.close();
            }
            if (aux == action.getFilters().get(Constants.DESCRIPTION_INDEX).size()) {
                sortedArray.add(actor);
            }
        }
        sortedArray.sort(Comparator.comparing(Actor::getName));
        return outputActorCommand(sortedArray, action);
    }

    /**
     * Add to a list the users with most rated videos
     *
     * @param userHashMap HashMap with users
     * @param action      Action
     * @return Users with most rated videos
     */
    public String numberOfRatings(final Action action,
                                  final HashMap<String, User> userHashMap) {
        ArrayList<User> sortedArray = new ArrayList<>();
        for (User user : userHashMap.values()) {
            if (user.getRatedMovies().size() + user.getRatedSeasons().size() != 0) {
                sortedArray.add(user);
            }
        }
        sortedArray.sort((o1, o2) -> {
            if (o1.getRatedMovies().size() + o1.getRatedSeasons().size()
                    - o2.getRatedMovies().size() - o2.getRatedSeasons().size() == 0) {
                return o1.getUsername().compareTo(o2.getUsername());
            }
            return (o1.getRatedMovies().size() + o1.getRatedSeasons().size()
                    - o2.getRatedMovies().size() - o2.getRatedSeasons().size());
        });
        return outputUserCommand(sortedArray, action);
    }

    /**
     * Add to a list the most viewed rated videos
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param action        Action
     * @return String with the most viewed rated videos
     */
    public String mostViewed(final Action action,
                             final HashMap<String, Movie> movieHashMap,
                             final HashMap<String, Serial> serialHashMap) {
        ArrayBuilder builder = new ArrayBuilder();
        ArrayList<Video> sortedArray = builder.makeArray(action, movieHashMap, serialHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.getNumberViews() - o2.getNumberViews() == 0) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
            return o1.getNumberViews() - o2.getNumberViews();
        });
        return outputVideoCommand(sortedArray, action);
    }

    /**
     * Add to a list the best rated videos
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param action        Action
     * @return String with the best rated videos
     */
    public String rating(final Action action,
                         final HashMap<String, Movie> movieHashMap,
                         final HashMap<String, Serial> serialHashMap) {
        ArrayBuilder builder = new ArrayBuilder();
        ArrayList<Video> sortedArray = builder.makeArray(action, movieHashMap, serialHashMap);
        sortedArray.sort((o1, o2) -> {
            if (Double.compare(o1.getRating(), o2.getRating()) == 0) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
            return Double.compare(o1.getRating(), o2.getRating());
        });
        return outputVideoCommand(sortedArray, action);
    }

    /**
     * Add to a list the most favorite videos
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param action        Action
     * @return String with the most favorite videos
     */
    public String favorite(final Action action,
                           final HashMap<String, Movie> movieHashMap,
                           final HashMap<String, Serial> serialHashMap) {
        ArrayBuilder builder = new ArrayBuilder();
        ArrayList<Video> sortedArray = builder.makeArray(action, movieHashMap, serialHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.getNumberFavorite() - o2.getNumberFavorite() == 0) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
            return o1.getNumberFavorite() - o2.getNumberFavorite();
        });
        return outputVideoCommand(sortedArray, action);
    }

    /**
     * Add to a list the longest videos
     *
     * @param movieHashMap  HashMap with movies
     * @param serialHashMap HashMap with serials
     * @param action        Action
     * @return String with the longest videos
     */
    public String longest(final Action action,
                          final HashMap<String, Movie> movieHashMap,
                          final HashMap<String, Serial> serialHashMap) {
        ArrayBuilder builder = new ArrayBuilder();
        ArrayList<Video> sortedArray = builder.makeArray(action, movieHashMap, serialHashMap);
        sortedArray.sort((o1, o2) -> {
            if (o1.getDuration() - o2.getDuration() == 0) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
            return o1.getDuration() - o2.getDuration();
        });
        return outputVideoCommand(sortedArray, action);
    }
}

