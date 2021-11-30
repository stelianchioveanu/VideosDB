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
import java.util.LinkedHashMap;

import static entertainment.Movie.longestMovie;
import static entertainment.Movie.favoriteMovie;
import static entertainment.Movie.mostViewedMovies;
import static entertainment.Movie.ratingMovies;
import static entertainment.Serial.longestSerial;
import static entertainment.Serial.favoriteSerial;
import static entertainment.Serial.mostViewedSerial;
import static entertainment.Serial.ratingSerial;
import static actor.Actor.awards;
import static actor.Actor.average;
import static actor.Actor.filterDescription;
import static common.User.numberOfRatings;
import static utils.Utils.stringToGenre;

public final class Repository {
    private final HashMap<Genre, Integer> genreHashMap = new HashMap<>();
    private final HashMap<String, Actor> actorHashMap = new HashMap<>();
    private final HashMap<String, Movie> movieHashMap = new LinkedHashMap<>();
    private final HashMap<String, Serial> serialHashMap = new LinkedHashMap<>();
    private final HashMap<String, User> userHashMap = new HashMap<>();
    private final HashMap<Integer, Action> actionArray = new HashMap<>();

    private static Repository instance = null;

    private Repository() {
    }

    /**
     * com
     */
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    private void initialization(final Input input) {
        for (Genre genre : Genre.values()) {
            this.genreHashMap.put(genre, Constants.ZERO);
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
            for (var entry : user.getHistory().entrySet()) {
                String title = entry.getKey();
                if (movieHashMap.get(title) != null) {
                    movieHashMap.get(title).setNumberViews(
                            movieHashMap.get(title).getNumberViews()
                                    + user.getHistory().get(title));
                    for (String genre : movieHashMap.get(title).getGenres()) {
                        genreHashMap.put(stringToGenre(genre), user.getHistory().get(title)
                                + genreHashMap.get(stringToGenre(genre)));
                    }
                } else {
                    serialHashMap.get(title).setNumberViews(
                            serialHashMap.get(title).getNumberViews()
                                    + user.getHistory().get(title));
                    for (String genre : serialHashMap.get(title).getGenres()) {
                        genreHashMap.put(stringToGenre(genre), user.getHistory().get(title)
                                + genreHashMap.get(stringToGenre(genre)));
                    }
                }

            }
            for (String title : user.getFavoriteMovies()) {
                if (movieHashMap.get(title) != null) {
                    movieHashMap.get(title).setNumberFavorite(
                            movieHashMap.get(title).getNumberFavorite() + 1);
                } else {
                    serialHashMap.get(title).setNumberFavorite(
                            serialHashMap.get(title).getNumberFavorite() + 1);
                }
            }
        }
    }

    private void clearRepository() {
        this.serialHashMap.clear();
        this.userHashMap.clear();
        this.movieHashMap.clear();
        this.actionArray.clear();
        this.actorHashMap.clear();
        this.genreHashMap.clear();
    }

    private String switchCasesAction(final Action action) {
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
                            case Constants.AVERAGE -> average(action, movieHashMap,
                                    serialHashMap, actorHashMap);
                            case Constants.AWARDS -> awards(action, actorHashMap);
                            case Constants.FILTER_DESCRIPTIONS -> filterDescription(action,
                                    actorHashMap);
                            default -> Constants.DEFAULT_CASE_STRING;
                        };
                    default:
                        return Constants.DEFAULT_CASE_STRING;
                }
            case Constants.RECOMMENDATION:
                return switch (action.getType()) {
                    case Constants.STANDARD -> userHashMap.get(action.getUsername()).standard(movieHashMap,
                            serialHashMap);
                    case Constants.BESTUNSEEN -> userHashMap.get(action.getUsername()).bestUnseen(movieHashMap,
                            serialHashMap);
                    case Constants.POPULAR -> userHashMap.get(action.getUsername()).popularPremium(movieHashMap,
                            serialHashMap, genreHashMap);
                    case Constants.FAVORITE -> userHashMap.get(action.getUsername()).favoritePremium(movieHashMap,
                            serialHashMap);
                    case Constants.SEARCH -> userHashMap.get(action.getUsername()).searchPremium(movieHashMap,
                            serialHashMap, action.getGenre());
                    default -> Constants.DEFAULT_CASE_STRING;
                };
            default:
                return Constants.DEFAULT_CASE_STRING;
        }
    }

    /**
     * com
     */
    public void entryPoint(final Input input, final JSONArray arrayResult,
                           final Writer fileWriter) throws IOException {
        this.initialization(input);
        for (Integer i : actionArray.keySet()) {
            JSONObject object = fileWriter.writeFile(actionArray.get(i).getActionId(),
                    null, this.switchCasesAction(actionArray.get(i)));
            arrayResult.add(object);
        }
        this.clearRepository();
    }
}
