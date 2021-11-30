package common;

import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;

import java.util.ArrayList;

public class Output {
    /**
     * com
     */
    public static String outputMovieCommand(final ArrayList<Movie> sortedArray,
                                            final Action action) {
        StringBuilder outputBuilder = new StringBuilder();
        if (action.getSortType().equals("asc")) {
            for (int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                outputBuilder.append(sortedArray.get(i).getTitle());
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    outputBuilder.append(", ");
                }
            }
        } else {
            for (int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                outputBuilder.append(sortedArray.get(i).getTitle());
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    outputBuilder.append(", ");
                }
            }
        }
        return "Query result: [" + outputBuilder + "]";
    }

    /**
     * com
     */
    public static String outputSerialCommand(final ArrayList<Serial> sortedArray,
                                             final Action action) {
        StringBuilder outputBuilder = new StringBuilder();
        if (action.getSortType().equals("asc")) {
            for (int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                outputBuilder.append(sortedArray.get(i).getTitle());
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    outputBuilder.append(", ");
                }
            }
        } else {
            for (int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                outputBuilder.append(sortedArray.get(i).getTitle());
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    outputBuilder.append(", ");
                }
            }
        }
        return "Query result: [" + outputBuilder + "]";
    }

    /**
     * com
     */
    public static String outputActorCommand(final ArrayList<Actor> sortedArray,
                                            final Action action) {
        StringBuilder outputBuilder = new StringBuilder();
        if (action.getSortType().equals("asc")) {
            for (int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                outputBuilder.append(sortedArray.get(i).getName());
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    outputBuilder.append(", ");
                }
            }
        } else {
            for (int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                outputBuilder.append(sortedArray.get(i).getName());
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    outputBuilder.append(", ");
                }
            }
        }
        return "Query result: [" + outputBuilder + "]";
    }

    /**
     * com
     */
    public static String outputUserCommand(final ArrayList<User> sortedArray,
                                           final Action action) {
        StringBuilder outputBuilder = new StringBuilder();
        if (action.getSortType().equals("asc")) {
            for (int i = 0; i < Math.min(action.getNumber(), sortedArray.size()); i++) {
                outputBuilder.append(sortedArray.get(i).getUsername());
                if (i != Math.min(action.getNumber(), sortedArray.size()) - 1) {
                    outputBuilder.append(", ");
                }
            }
        } else {
            for (int i = sortedArray.size() - 1; i >= Math.max(sortedArray.size() - action.getNumber(), 0); i--) {
                outputBuilder.append(sortedArray.get(i).getUsername());
                if (i != Math.max(0, sortedArray.size() - action.getNumber())) {
                    outputBuilder.append(", ");
                }
            }
        }
        return "Query result: [" + outputBuilder + "]";
    }
}
