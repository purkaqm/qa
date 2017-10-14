package com.powersteeringsoftware.libs.elements;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 10.10.13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public interface SearchEngine {
    enum Type {
        ALL,
        PROJECTS,
        PEOPLE,
        IDEAS,
        ISSUES,
        DOCUMENTS,;
    }

    public SearchResult makeSearch(String txt, Type type);
}
