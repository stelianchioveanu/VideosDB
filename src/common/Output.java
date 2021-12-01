package common;

import actor.Actor;
import entertainment.Video;
import user.User;

import java.util.ArrayList;

public final class Output {

    private Output() {
    }

    /**
     * com
     */
    public static String outputVideoCommand(final ArrayList<Video> sortedArray,
                                            final Action action) {
        StringBuilder outputBuilder = new StringBuilder();
        if (action.getSortType().equals("asc")) {
            int start = 0;
            int end = Math.min(action.getNumber(), sortedArray.size());
            while (start < end) {
                outputBuilder.append(sortedArray.get(start).getTitle());
                if (start != end - 1) {
                    outputBuilder.append(", ");
                }
                start++;
            }
        } else {
            int start = sortedArray.size() - 1;
            int end = Math.max(sortedArray.size() - action.getNumber(), 0);
            while (start >= end) {
                outputBuilder.append(sortedArray.get(start).getTitle());
                if (start != end) {
                    outputBuilder.append(", ");
                }
                start--;
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
            int start = 0;
            int end = Math.min(action.getNumber(), sortedArray.size());
            while (start < end) {
                outputBuilder.append(sortedArray.get(start).getName());
                if (start != end - 1) {
                    outputBuilder.append(", ");
                }
                start++;
            }
        } else {
            int start = sortedArray.size() - 1;
            int end = Math.max(sortedArray.size() - action.getNumber(), 0);
            while (start >= end) {
                outputBuilder.append(sortedArray.get(start).getName());
                if (start != end) {
                    outputBuilder.append(", ");
                }
                start--;
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
            int start = 0;
            int end = Math.min(action.getNumber(), sortedArray.size());
            while (start < end) {
                outputBuilder.append(sortedArray.get(start).getUsername());
                if (start != end - 1) {
                    outputBuilder.append(", ");
                }
                start++;
            }
        } else {
            int start = sortedArray.size() - 1;
            int end = Math.max(sortedArray.size() - action.getNumber(), 0);
            while (start >= end) {
                outputBuilder.append(sortedArray.get(start).getUsername());
                if (start != end) {
                    outputBuilder.append(", ");
                }
                start--;
            }
        }
        return "Query result: [" + outputBuilder + "]";
    }
}
