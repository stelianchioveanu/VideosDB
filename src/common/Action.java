package common;

import fileio.ActionInputData;

import java.util.List;

/**
 * Information about an action
 */
public final class Action {
    /**
     * ID of an action
     */
    private final int actionId;
    /**
     * Type of action
     */
    private final String actionType;
    /**
     * Type of command, query or recommendation
     */
    private final String type;
    /**
     * Username contained in the action
     */
    private final String username;
    /**
     * Type of object contained in the action
     */
    private final String objectType;
    /**
     * Type of sort contained in the action
     */
    private final String sortType;
    /**
     * Criteria contained in the action
     */
    private final String criteria;
    /**
     * Title contained in the action
     */
    private final String title;
    /**
     * Genre contained in the action
     */
    private final String genre;
    /**
     * Size of output list
     */
    private final int number;
    /**
     * Grade contained in the action
     */
    private final double grade;
    /**
     * Season's number contained in the action
     */
    private final int seasonNumber;
    /**
     * Filters contained in the action
     */
    private final List<List<String>> filters;

    public Action(final ActionInputData actionInputData) {
        this.actionId = actionInputData.getActionId();
        this.actionType = actionInputData.getActionType();
        this.type = actionInputData.getType();
        this.username = actionInputData.getUsername();
        this.objectType = actionInputData.getObjectType();
        this.sortType = actionInputData.getSortType();
        this.criteria = actionInputData.getCriteria();
        this.title = actionInputData.getTitle();
        this.genre = actionInputData.getGenre();
        this.number = actionInputData.getNumber();
        this.grade = actionInputData.getGrade();
        this.seasonNumber = actionInputData.getSeasonNumber();
        this.filters = actionInputData.getFilters();
    }

    public int getActionId() {
        return actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getSortType() {
        return sortType;
    }

    public String getCriteria() {
        return criteria;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getNumber() {
        return number;
    }

    public double getGrade() {
        return grade;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public List<List<String>> getFilters() {
        return filters;
    }
}
