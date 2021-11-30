package common;

import fileio.ActionInputData;

import java.util.List;

public final class Action {
    private final int actionId;
    private final String actionType;
    private final String type;
    private final String username;
    private final String objectType;
    private final String sortType;
    private final String criteria;
    private final String title;
    private final String genre;
    private final int number;
    private final double grade;
    private final int seasonNumber;
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
