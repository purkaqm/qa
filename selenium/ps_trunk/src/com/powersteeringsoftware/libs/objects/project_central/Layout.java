package com.powersteeringsoftware.libs.objects.project_central;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.11.2010
 * Time: 16:45:10
 */
public class Layout {
    private String name;
    private List<String> options;
    private String filterNameExactly;

    public Layout(String name, List<String> options, String filterNameExactly) {
        this.name = name;
        this.options = options;
        this.filterNameExactly = filterNameExactly;
    }

    public Layout set(Layout layout) {
        this.options = layout.options;
        this.filterNameExactly = layout.filterNameExactly;
        return this;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Layout : '" + name + "'";
    }

    public String getFilterNameExactly() {
        return filterNameExactly;
    }
}
