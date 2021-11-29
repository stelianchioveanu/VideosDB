package main;

import actor.Actor;
import common.Action;
import common.Constants;
import common.User;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Serial;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static entertainment.Movie.*;
import static entertainment.Serial.*;
import static actor.Actor.*;
import static common.User.*;

public class Repository {
    private final HashSet<String> genreHashSet = new HashSet<>();
    private final HashMap<String, Actor> actorHashMap = new HashMap<>();
    private final HashMap<String, Movie> movieHashMap = new HashMap<>();
    private final HashMap<String, Serial> serialHashMap = new HashMap<>();
    private final HashMap<String, User> userHashMap = new HashMap<>();
    private final HashMap<Integer, Action> actionArray = new HashMap<>();

    public void initialization(Input input) {
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

    public String switchCasesAction(Action action) {
        switch (action.getActionType()) {
            case Constants.COMMAND:
                switch (action.getType()) {
                    case "view":
                        return this.userHashMap.get(action.getUsername()).view(action,
                                movieHashMap, serialHashMap);
                    case "favorite":
                        return this.userHashMap.get(action.getUsername()).favorite(action,
                                movieHashMap, serialHashMap);
                    case "rating":
                        return this.userHashMap.get(action.getUsername()).rating(action,
                                movieHashMap, serialHashMap);
                }
            case Constants.QUERY:
                switch (action.getObjectType()) {
                    case Constants.MOVIES:
                        switch (action.getCriteria()) {
                            case "most_viewed":
                                return mostViewedMovies(action, movieHashMap);
                            case "longest":
                                return longestMovie(action, movieHashMap);
                            case "favorite":
                                return favoriteMovie(action, movieHashMap);
                            case "ratings":
                                return ratingMovies(action, movieHashMap);
                        }
                    case Constants.SHOWS:
                        switch (action.getCriteria()) {
                            case "most_viewed":
                                return mostViewedSerial(action, serialHashMap);
                            case "longest":
                                return longestSerial(action, serialHashMap);
                            case "favorite":
                                return favoriteSerial(action, serialHashMap);
                            case "ratings":
                                return ratingSerial(action, serialHashMap);
                        }
                    case Constants.USERS:
                        if (action.getCriteria().equals(Constants.NUM_RATINGS)) {
                            return numberOfRatings(action, userHashMap);
                        }
                    case Constants.ACTORS:
                        switch (action.getCriteria()) {
                            case "average":
                                return averageActor(action, movieHashMap,
                                        serialHashMap, actorHashMap);
                            case Constants.AWARDS:
                                return awardsActor(action, actorHashMap);
                            case Constants.FILTER_DESCRIPTIONS:
                                return filterDescriptionActor(action, actorHashMap);
                        }
                }
            case Constants.RECOMMENDATION:
        }
        return "";
    }

    public void entryPoint(Input input, JSONArray arrayResult, Writer fileWriter) throws IOException {
        this.initialization(input);
        JSONObject object;
        for (Integer i : actionArray.keySet()) {
            object = fileWriter.writeFile(actionArray.get(i).getActionId(), null, this.switchCasesAction(actionArray.get(i)));
            arrayResult.add(object);
        }
    }
}
