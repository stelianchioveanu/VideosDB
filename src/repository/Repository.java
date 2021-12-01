package repository;

import actor.Actor;
import commands.Command;
import commands.Query;
import commands.Recommendation;
import common.Action;
import common.Constants;
import user.User;
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
     * If the Repository instance is null, create a new one
     *
     * @return Instance of Repository class
     */
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    private void populateRepository(final Input input) {
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
            for (String title : user.getFavoriteVideos()) {
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
                Command command = new Command();
                return switch (action.getType()) {
                    case Constants.VIEW -> command.view(action, movieHashMap,
                            serialHashMap, this.userHashMap.get(action.getUsername()));
                    case Constants.FAVORITE -> command.favorite(action, movieHashMap,
                            serialHashMap, this.userHashMap.get(action.getUsername()));
                    case Constants.RATING -> command.rating(action, movieHashMap,
                            serialHashMap, this.userHashMap.get(action.getUsername()));
                    default -> Constants.DEFAULT_CASE_STRING;
                };
            case Constants.QUERY:
                Query query = new Query();
                switch (action.getObjectType()) {
                    case Constants.MOVIES:
                        return switch (action.getCriteria()) {
                            case Constants.MOST_VIEWED -> query.mostViewed(
                                    action, movieHashMap, null);
                            case Constants.LONGEST -> query.longest(
                                    action, movieHashMap, null);
                            case Constants.FAVORITE -> query.favorite(
                                    action, movieHashMap, null);
                            case Constants.RATINGS -> query.rating(
                                    action, movieHashMap, null);
                            default -> Constants.DEFAULT_CASE_STRING;
                        };
                    case Constants.SHOWS:
                        return switch (action.getCriteria()) {
                            case Constants.MOST_VIEWED -> query.mostViewed(
                                    action, null, serialHashMap);
                            case Constants.LONGEST -> query.longest(
                                    action, null, serialHashMap);
                            case Constants.FAVORITE -> query.favorite(
                                    action, null, serialHashMap);
                            case Constants.RATINGS -> query.rating(
                                    action, null, serialHashMap);
                            default -> Constants.DEFAULT_CASE_STRING;
                        };
                    case Constants.USERS:
                        if (action.getCriteria().equals(Constants.NUM_RATINGS)) {
                            return query.numberOfRatings(action, userHashMap);
                        }
                    case Constants.ACTORS:
                        return switch (action.getCriteria()) {
                            case Constants.AVERAGE -> query.average(action, movieHashMap,
                                    serialHashMap, actorHashMap);
                            case Constants.AWARDS -> query.awards(action, actorHashMap);
                            case Constants.FILTER_DESCRIPTIONS -> query.filterDescription(
                                    action, actorHashMap);
                            default -> Constants.DEFAULT_CASE_STRING;
                        };
                    default:
                        return Constants.DEFAULT_CASE_STRING;
                }
            case Constants.RECOMMENDATION:
                Recommendation recommendation = new Recommendation();
                return switch (action.getType()) {
                    case Constants.STANDARD -> recommendation.standard(
                            movieHashMap, serialHashMap, userHashMap.get(
                                    action.getUsername()));
                    case Constants.BEST_UNSEEN -> recommendation.bestUnseen(
                            movieHashMap, serialHashMap, userHashMap.get(
                                    action.getUsername()));
                    case Constants.POPULAR -> recommendation.popularPremium(
                            movieHashMap, serialHashMap, genreHashMap, userHashMap.get(
                                    action.getUsername()));
                    case Constants.FAVORITE -> recommendation.favoritePremium(
                            movieHashMap, serialHashMap, userHashMap.get(
                                    action.getUsername()));
                    case Constants.SEARCH -> recommendation.searchPremium(
                            movieHashMap, serialHashMap, action.getGenre(), userHashMap.get(
                                    action.getUsername()));
                    default -> Constants.DEFAULT_CASE_STRING;
                };
            default:
                return Constants.DEFAULT_CASE_STRING;
        }
    }

    /**
     * This is the entry point of the implementation
     * Firstly populate the repository, then add the results
     * of the actions to the arrayResult and finally release the repository.
     *
     * @param arrayResult Contain the result
     * @param fileWriter  Write in arrayResult
     * @param input       Contain the inputs
     */
    public void entryPoint(final Input input, final JSONArray arrayResult,
                           final Writer fileWriter) throws IOException {
        this.populateRepository(input);
        for (Integer i : actionArray.keySet()) {
            JSONObject object = fileWriter.writeFile(actionArray.get(i).getActionId(),
                    null, this.switchCasesAction(actionArray.get(i)));
            arrayResult.add(object);
        }
        this.clearRepository();
    }
}
