package main;

import actor.Actor;
import common.Action;
import common.Constants;
import common.User;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Serial;
import fileio.ActorInputData;
import fileio.ActionInputData;
import fileio.UserInputData;
import fileio.SerialInputData;
import fileio.MovieInputData;
import fileio.Input;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static entertainment.Movie.longestMovie;
import static entertainment.Movie.favoriteMovie;
import static entertainment.Movie.mostViewedMovies;
import static entertainment.Movie.ratingMovies;
import static entertainment.Serial.longestSerial;
import static entertainment.Serial.favoriteSerial;
import static entertainment.Serial.mostViewedSerial;
import static entertainment.Serial.ratingSerial;
import static actor.Actor.awardsActor;
import static actor.Actor.averageActor;
import static actor.Actor.filterDescriptionActor;
import static common.User.numberOfRatings;

public class Repository {
    private final HashSet<String> genreHashSet = new HashSet<>();
    private final HashMap<String, Actor> actorHashMap = new HashMap<>();
    private final HashMap<String, Movie> movieHashMap = new HashMap<>();
    private final HashMap<String, Serial> serialHashMap = new HashMap<>();
    private final HashMap<String, User> userHashMap = new HashMap<>();
    private final HashMap<Integer, Action> actionArray = new HashMap<>();

    /**
     * com
     */
    public void initialization(final Input input) {
        for (Genre i : Genre.values()) {
            this.genreHashSet.add(i.toString());
        }
        for (ActorInputData i : input.getActors()) {
            Actor actor = new Actor(i);
            this.actorHashMap.put(actor.getName(), actor);
        }
        for (MovieInputData i : input.getMovies()) {
            Movie movie = new Movie(i);
            this.movieHashMap.put(movie.getTitle(), movie);
        }
        for (SerialInputData i : input.getSerials()) {
            Serial serial = new Serial(i);
            this.serialHashMap.put(serial.getTitle(), serial);
        }
        for (ActionInputData i : input.getCommands()) {
            Action action = new Action(i);
            this.actionArray.put(action.getActionId(), action);
        }
        for (UserInputData i : input.getUsers()) {
            User user = new User(i);
            this.userHashMap.put(user.getUsername(), user);
            for (Map.Entry entry : user.getHistory().entrySet()) {
                String title = (String) entry.getKey();
                if (movieHashMap.get(title) != null) {
                    movieHashMap.get(title).setNumberViews(movieHashMap.get(title).getNumberViews()
                            + user.getHistory().get(title));
                } else {
                    serialHashMap.get(title).setNumberViews(serialHashMap.get(title).getNumberViews()
                            + user.getHistory().get(title));
                }
            }
            for (String title : user.getFavoriteMovies()) {
                if (movieHashMap.get(title) != null) {
                    movieHashMap.get(title).setNumberFavorite(movieHashMap.get(title).getNumberFavorite() + 1);
                } else {
                    serialHashMap.get(title).setNumberFavorite(serialHashMap.get(title).getNumberFavorite() + 1);
                }
            }
        }
    }

    /**
     * com
     */
    public String switchCasesAction(final Action action) {
        switch (action.getActionType()) {
            case Constants.COMMAND:
                return switch (action.getType()) {
                    case Constants.VIEW -> this.userHashMap.get(action.getUsername()).view(action,
                            movieHashMap, serialHashMap);
                    case Constants.FAVORITE -> this.userHashMap.get(action.getUsername()).favorite(action,
                            movieHashMap, serialHashMap);
                    case Constants.RATING -> this.userHashMap.get(action.getUsername()).rating(action,
                            movieHashMap, serialHashMap);
                    default -> Constants.DEFAULT_CASE_STRING;
                };
            case Constants.QUERY:
                switch (action.getObjectType()) {
                    case Constants.MOVIES:
                        return switch (action.getCriteria()) {
                            case Constants.MOST_VIEWED -> mostViewedMovies(action, movieHashMap);
                            case Constants.LONGEST -> longestMovie(action, movieHashMap);
                            case Constants.FAVORITE -> favoriteMovie(action, movieHashMap);
                            case Constants.RATINGS -> ratingMovies(action, movieHashMap);
                            default -> Constants.DEFAULT_CASE_STRING;
                        };
                    case Constants.SHOWS:
                        return switch (action.getCriteria()) {
                            case Constants.MOST_VIEWED -> mostViewedSerial(action, serialHashMap);
                            case Constants.LONGEST -> longestSerial(action, serialHashMap);
                            case Constants.FAVORITE -> favoriteSerial(action, serialHashMap);
                            case Constants.RATINGS -> ratingSerial(action, serialHashMap);
                            default -> Constants.DEFAULT_CASE_STRING;
                        };
                    case Constants.USERS:
                        if (action.getCriteria().equals(Constants.NUM_RATINGS)) {
                            return numberOfRatings(action, userHashMap);
                        }
                    case Constants.ACTORS:
                        return switch (action.getCriteria()) {
                            case Constants.AVERAGE -> averageActor(action, movieHashMap,
                                    serialHashMap, actorHashMap);
                            case Constants.AWARDS -> awardsActor(action, actorHashMap);
                            case Constants.FILTER_DESCRIPTIONS -> filterDescriptionActor(action, actorHashMap);
                            default -> Constants.DEFAULT_CASE_STRING;
                        };
                    default:
                        return Constants.DEFAULT_CASE_STRING;
                }
            case Constants.RECOMMENDATION:
                return "";
            default:
                return Constants.DEFAULT_CASE_STRING;
        }
    }

    /**
     * com
     */
    public void entryPoint(final Input input, final JSONArray arrayResult, final Writer fileWriter) throws IOException {
        this.initialization(input);
        JSONObject object;
        for (Integer i : actionArray.keySet()) {
            object = fileWriter.writeFile(actionArray.get(i).getActionId(), null, this.switchCasesAction(actionArray.get(i)));
            arrayResult.add(object);
        }
    }
}
